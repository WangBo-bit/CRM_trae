package com.xindongli.crm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.system.dto.response.*;
import com.xindongli.crm.system.entity.Contact;
import com.xindongli.crm.system.entity.Customer;
import com.xindongli.crm.system.entity.CustomerFollow;
import com.xindongli.crm.system.entity.Dept;
import com.xindongli.crm.system.mapper.ContactMapper;
import com.xindongli.crm.system.mapper.CustomerFollowMapper;
import com.xindongli.crm.system.mapper.CustomerMapper;
import com.xindongli.crm.system.mapper.DeptMapper;
import com.xindongli.crm.system.service.CustomerPanoramaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户全景视图服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerPanoramaServiceImpl implements CustomerPanoramaService {

    private final CustomerMapper customerMapper;
    private final ContactMapper contactMapper;
    private final CustomerFollowMapper customerFollowMapper;
    private final DeptMapper deptMapper;

    @Override
    public CustomerPanoramaResponse getCustomerPanorama(Long customerId) {
        // 1. 查询客户基本信息
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 2. 构建响应对象
        CustomerPanoramaResponse response = new CustomerPanoramaResponse();
        BeanUtils.copyProperties(customer, response);

        // 3. 设置客户等级名称
        response.setCustomerLevelName(getCustomerLevelName(customer.getCustomerLevel()));
        
        // 4. 设置客户状态名称
        response.setCustomerStatusName(getCustomerStatusName(customer.getCustomerStatus()));

        // 5. 查询部门名称
        if (customer.getDeptId() != null) {
            Dept dept = deptMapper.selectById(customer.getDeptId());
            if (dept != null) {
                response.setDeptName(dept.getDeptName());
            }
        }

        // 6. 聚合联系人信息
        aggregateContacts(response, customerId);

        // 7. 聚合跟进记录信息
        aggregateFollowRecords(response, customerId);

        // 8. 聚合商机信息（预留接口）
        aggregateOpportunities(response, customerId);

        // 9. 聚合POC信息（预留接口）
        aggregatePocs(response, customerId);

        // 10. 聚合技术评估信息（预留接口）
        aggregateAssessments(response, customerId);

        // 11. 聚合合作数据（预留接口）
        aggregateCooperationData(response, customerId);

        // 12. 聚合动态时间线
        aggregateTimeline(response, customerId);

        // 13. 聚合统计数据
        aggregateStatistics(response, customerId);

        log.info("获取客户全景视图成功, 客户ID: {}", customerId);
        return response;
    }

    @Override
    public CustomerStatisticsResponse getCustomerStatistics(Long customerId) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        CustomerStatisticsResponse response = new CustomerStatisticsResponse();
        response.setCustomerId(customerId);

        // 跟进次数
        LambdaQueryWrapper<CustomerFollow> followWrapper = new LambdaQueryWrapper<>();
        followWrapper.eq(CustomerFollow::getCustomerId, customerId);
        Long followCount = customerFollowMapper.selectCount(followWrapper);
        response.setFollowCount(followCount.intValue());

        // 联系人数量
        LambdaQueryWrapper<Contact> contactWrapper = new LambdaQueryWrapper<>();
        contactWrapper.eq(Contact::getCustomerId, customerId);
        Long contactCount = contactMapper.selectCount(contactWrapper);
        response.setContactCount(contactCount.intValue());

        // 商机数量和金额（预留）
        response.setOpportunityCount(0);
        response.setOpportunityAmount(BigDecimal.ZERO);

        // POC数量（预留）
        response.setPocCount(0);

        // 技术评估数量（预留）
        response.setAssessmentCount(0);

        // 年采购额（预留）
        response.setAnnualPurchaseAmount(BigDecimal.ZERO);

        // 合同数量（预留）
        response.setContractCount(0);

        log.info("获取客户统计数据成功, 客户ID: {}", customerId);
        return response;
    }

    @Override
    public List<CustomerTimelineResponse> getCustomerTimeline(Long customerId, Integer limit) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 默认查询最近20条动态,最大100条
        if (limit == null || limit <= 0) {
            limit = 20;
        }
        if (limit > 100) {
            limit = 100;
        }

        List<CustomerTimelineResponse> timeline = new ArrayList<>();

        // 1. 聚合跟进记录动态 - 使用分页代替LIMIT拼接,防止SQL注入
        LambdaQueryWrapper<CustomerFollow> followWrapper = new LambdaQueryWrapper<>();
        followWrapper.eq(CustomerFollow::getCustomerId, customerId)
                .orderByDesc(CustomerFollow::getFollowTime);
        
        Page<CustomerFollow> page = new Page<>(1, limit, false);
        List<CustomerFollow> follows = customerFollowMapper.selectPage(page, followWrapper).getRecords();

        for (CustomerFollow follow : follows) {
            CustomerTimelineResponse item = new CustomerTimelineResponse();
            item.setId(follow.getId());
            item.setCustomerId(customerId);
            item.setTimelineType("follow");
            item.setTimelineTypeName("跟进记录");
            item.setTitle("客户跟进");
            item.setContent(follow.getFollowContent());
            item.setOperatorId(follow.getUserId());
            item.setOperatorName(follow.getUserName());
            item.setTimelineTime(follow.getFollowTime());
            item.setRelatedId(follow.getId());
            item.setRelatedType("follow");
            item.setCreateTime(follow.getCreateTime());
            timeline.add(item);
        }

        // 2. 按时间倒序排序 - 添加null检查,防止空指针异常
        timeline.sort((a, b) -> {
            if (a.getTimelineTime() == null && b.getTimelineTime() == null) return 0;
            if (a.getTimelineTime() == null) return 1;
            if (b.getTimelineTime() == null) return -1;
            return b.getTimelineTime().compareTo(a.getTimelineTime());
        });

        // 3. 限制返回数量
        if (timeline.size() > limit) {
            timeline = timeline.subList(0, limit);
        }

        log.info("获取客户动态时间线成功, 客户ID: {}, 数量: {}", customerId, timeline.size());
        return timeline;
    }

    /**
     * 聚合联系人信息
     */
    private void aggregateContacts(CustomerPanoramaResponse response, Long customerId) {
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contact::getCustomerId, customerId)
                .orderByDesc(Contact::getIsPrimary)
                .orderByDesc(Contact::getCreateTime);

        List<Contact> contacts = contactMapper.selectList(wrapper);

        if (!contacts.isEmpty()) {
            // 转换为响应对象
            List<ContactResponse> contactResponses = contacts.stream()
                    .map(contact -> {
                        ContactResponse contactResponse = new ContactResponse();
                        BeanUtils.copyProperties(contact, contactResponse);
                        return contactResponse;
                    })
                    .collect(Collectors.toList());

            response.setContacts(contactResponses);
            response.setContactCount(contacts.size());

            // 设置主联系人
            Contact primaryContact = contacts.stream()
                    .filter(c -> c.getIsPrimary() != null && c.getIsPrimary() == 1)
                    .findFirst()
                    .orElse(contacts.get(0));

            ContactResponse primaryContactResponse = new ContactResponse();
            BeanUtils.copyProperties(primaryContact, primaryContactResponse);
            response.setPrimaryContact(primaryContactResponse);
        } else {
            response.setContacts(new ArrayList<>());
            response.setContactCount(0);
        }
    }

    /**
     * 聚合跟进记录信息
     */
    private void aggregateFollowRecords(CustomerPanoramaResponse response, Long customerId) {
        // 使用分页代替LIMIT拼接,防止SQL注入
        LambdaQueryWrapper<CustomerFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerFollow::getCustomerId, customerId)
                .orderByDesc(CustomerFollow::getFollowTime);
        
        Page<CustomerFollow> page = new Page<>(1, 10, false);
        List<CustomerFollow> follows = customerFollowMapper.selectPage(page, wrapper).getRecords();

        if (!follows.isEmpty()) {
            // 批量查询联系人信息,避免N+1查询问题
            Set<Long> contactIds = follows.stream()
                    .map(CustomerFollow::getContactId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<Long, String> contactNameMap = new HashMap<>();
            if (!contactIds.isEmpty()) {
                List<Contact> contacts = contactMapper.selectBatchIds(contactIds);
                contactNameMap = contacts.stream()
                        .collect(Collectors.toMap(Contact::getId, Contact::getName, (v1, v2) -> v1));
            }

            // 转换为响应对象
            Map<Long, String> finalContactNameMap = contactNameMap;
            List<CustomerFollowResponse> followResponses = follows.stream()
                    .map(follow -> {
                        CustomerFollowResponse followResponse = new CustomerFollowResponse();
                        BeanUtils.copyProperties(follow, followResponse);
                        followResponse.setFollowTypeName(getFollowTypeName(follow.getFollowType()));
                        
                        // 从Map中获取联系人姓名,避免循环查询数据库
                        if (follow.getContactId() != null) {
                            followResponse.setContactName(finalContactNameMap.get(follow.getContactId()));
                        }
                        
                        return followResponse;
                    })
                    .collect(Collectors.toList());

            response.setRecentFollows(followResponses);
            response.setRecentFollowTime(follows.get(0).getFollowTime());
        } else {
            response.setRecentFollows(new ArrayList<>());
        }

        // 统计跟进次数
        LambdaQueryWrapper<CustomerFollow> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(CustomerFollow::getCustomerId, customerId);
        Long followCount = customerFollowMapper.selectCount(countWrapper);
        response.setFollowCount(followCount.intValue());
    }

    /**
     * 聚合商机信息（预留接口）
     */
    private void aggregateOpportunities(CustomerPanoramaResponse response, Long customerId) {
        // TODO: 实现商机信息聚合
        response.setOpportunityCount(0);
        response.setOpportunityAmount(BigDecimal.ZERO);
        response.setOpportunities(new ArrayList<>());
    }

    /**
     * 聚合POC信息（预留接口）
     */
    private void aggregatePocs(CustomerPanoramaResponse response, Long customerId) {
        // TODO: 实现POC信息聚合
        response.setPocCount(0);
        response.setPocs(new ArrayList<>());
    }

    /**
     * 聚合技术评估信息（预留接口）
     */
    private void aggregateAssessments(CustomerPanoramaResponse response, Long customerId) {
        // TODO: 实现技术评估信息聚合
        response.setAssessmentCount(0);
        response.setAssessments(new ArrayList<>());
    }

    /**
     * 聚合合作数据（预留接口）
     */
    private void aggregateCooperationData(CustomerPanoramaResponse response, Long customerId) {
        // TODO: 实现合作数据聚合
        response.setAnnualPurchaseAmount(BigDecimal.ZERO);
        response.setContractCount(0);
    }

    /**
     * 聚合动态时间线
     */
    private void aggregateTimeline(CustomerPanoramaResponse response, Long customerId) {
        List<CustomerTimelineResponse> timeline = getCustomerTimeline(customerId, 20);
        response.setTimeline(timeline);
    }

    /**
     * 聚合统计数据
     */
    private void aggregateStatistics(CustomerPanoramaResponse response, Long customerId) {
        CustomerPanoramaResponse.CustomerStatistics statistics = new CustomerPanoramaResponse.CustomerStatistics();
        
        statistics.setFollowCount(response.getFollowCount());
        statistics.setContactCount(response.getContactCount());
        statistics.setOpportunityCount(response.getOpportunityCount());
        statistics.setOpportunityAmount(response.getOpportunityAmount());
        statistics.setPocCount(response.getPocCount());
        statistics.setAssessmentCount(response.getAssessmentCount());
        statistics.setAnnualPurchaseAmount(response.getAnnualPurchaseAmount());
        statistics.setContractCount(response.getContractCount());
        
        response.setStatistics(statistics);
    }

    /**
     * 获取客户等级名称
     */
    private String getCustomerLevelName(String level) {
        if (level == null) {
            return "未知";
        }
        switch (level) {
            case "A":
                return "战略客户";
            case "B":
                return "重点客户";
            case "C":
                return "潜力客户";
            case "D":
                return "一般客户";
            default:
                return "未知";
        }
    }

    /**
     * 获取客户状态名称
     */
    private String getCustomerStatusName(String status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case "potential":
                return "潜在客户";
            case "intentional":
                return "意向客户";
            case "won":
                return "成交客户";
            case "lost":
                return "流失客户";
            default:
                return "未知";
        }
    }

    /**
     * 获取跟进方式名称
     */
    private String getFollowTypeName(String followType) {
        if (followType == null) {
            return "其他";
        }
        switch (followType) {
            case "phone":
                return "电话";
            case "visit":
                return "上门";
            case "wechat":
                return "微信";
            case "email":
                return "邮件";
            case "other":
                return "其他";
            default:
                return "其他";
        }
    }

}

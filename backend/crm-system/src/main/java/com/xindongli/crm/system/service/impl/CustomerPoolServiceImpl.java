package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.*;
import com.xindongli.crm.system.dto.response.CustomerPoolCustomerResponse;
import com.xindongli.crm.system.dto.response.CustomerPoolRuleResponse;
import com.xindongli.crm.system.entity.Customer;
import com.xindongli.crm.system.entity.CustomerPoolRule;
import com.xindongli.crm.system.enums.PoolRuleTypeEnum;
import com.xindongli.crm.system.mapper.CustomerMapper;
import com.xindongli.crm.system.mapper.CustomerPoolRuleMapper;
import com.xindongli.crm.system.service.CustomerPoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户公海池服务实现类
 *
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerPoolServiceImpl implements CustomerPoolService {

    private final CustomerMapper customerMapper;
    private final CustomerPoolRuleMapper customerPoolRuleMapper;

    @Override
    public PageResult<CustomerPoolCustomerResponse> getPoolCustomerPage(CustomerQueryRequest request) {
        // 构建查询条件 - 只查询公海池客户
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getIsPool, 1);

        // 企业名称模糊查询
        if (StrUtil.isNotBlank(request.getCompanyName())) {
            String escapedCompanyName = escapeLikeSpecialChars(request.getCompanyName());
            wrapper.like(Customer::getCompanyName, escapedCompanyName);
        }

        // 关键字查询(企业名称或企业简称)
        if (StrUtil.isNotBlank(request.getKeyword())) {
            String escapedKeyword = escapeLikeSpecialChars(request.getKeyword());
            wrapper.and(w -> w.like(Customer::getCompanyName, escapedKeyword)
                    .or()
                    .like(Customer::getShortName, escapedKeyword));
        }

        // 其他查询条件
        wrapper.eq(StrUtil.isNotBlank(request.getIndustry()), Customer::getIndustry, request.getIndustry())
                .eq(StrUtil.isNotBlank(request.getCustomerLevel()), Customer::getCustomerLevel, request.getCustomerLevel())
                .eq(StrUtil.isNotBlank(request.getCustomerStatus()), Customer::getCustomerStatus, request.getCustomerStatus())
                .eq(StrUtil.isNotBlank(request.getProvince()), Customer::getProvince, request.getProvince())
                .eq(StrUtil.isNotBlank(request.getCity()), Customer::getCity, request.getCity())
                .orderByDesc(Customer::getPoolTime);

        // 分页查询
        Page<Customer> page = new Page<>(request.getPageNum().longValue(), request.getPageSize().longValue());
        Page<Customer> customerPage = customerMapper.selectPage(page, wrapper);

        // 转换为响应对象
        List<CustomerPoolCustomerResponse> responseList = customerPage.getRecords().stream()
                .map(customer -> {
                    CustomerPoolCustomerResponse response = new CustomerPoolCustomerResponse();
                    BeanUtils.copyProperties(customer, response);
                    return response;
                })
                .collect(Collectors.toList());

        return PageResult.build(responseList, customerPage.getTotal(), customerPage.getCurrent(), customerPage.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimCustomer(CustomerPoolClaimRequest request) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 检查客户是否在公海池
        if (customer.getIsPool() != 1) {
            throw new BusinessException("客户不在公海池中,无法认领");
        }

        // 使用乐观锁更新客户状态
        customer.setIsPool(0);
        customer.setPoolTime(null);
        customer.setOwnerId(request.getOwnerId());
        customer.setOwnerName(request.getOwnerName());
        
        int updateCount = customerMapper.updateById(customer);
        if (updateCount == 0) {
            throw new BusinessException("认领失败,客户可能已被其他用户认领");
        }

        log.info("从公海池认领客户成功, 客户ID: {}, 负责人: {}", request.getCustomerId(), request.getOwnerName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchClaimCustomers(CustomerPoolBatchClaimRequest request) {
        // 参数校验
        if (request.getCustomerIds() == null || request.getCustomerIds().isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }

        // 过滤空值并限制批量操作数量
        List<Long> validIds = request.getCustomerIds().stream()
                .filter(id -> id != null)
                .distinct()
                .limit(100) // 限制每次最多认领100个客户
                .collect(Collectors.toList());

        if (validIds.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }

        // 批量查询客户
        List<Customer> customers = customerMapper.selectBatchIds(validIds);
        
        // 过滤出公海池中的客户
        List<Customer> poolCustomers = customers.stream()
                .filter(c -> c.getIsPool() == 1)
                .collect(Collectors.toList());

        if (poolCustomers.isEmpty()) {
            throw new BusinessException("没有可认领的客户");
        }

        // 批量更新客户状态
        LocalDateTime now = LocalDateTime.now();
        for (Customer customer : poolCustomers) {
            customer.setIsPool(0);
            customer.setPoolTime(null);
            customer.setOwnerId(request.getOwnerId());
            customer.setOwnerName(request.getOwnerName());
        }

        // 使用批量更新
        int successCount = 0;
        for (Customer customer : poolCustomers) {
            try {
                int updateCount = customerMapper.updateById(customer);
                if (updateCount > 0) {
                    successCount++;
                }
            } catch (Exception e) {
                log.error("认领客户失败, 客户ID: {}", customer.getId(), e);
            }
        }

        log.info("批量认领客户完成, 成功数量: {}/{}, 负责人: {}", successCount, poolCustomers.size(), request.getOwnerName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void putToPool(Long customerId) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 检查客户是否已在公海池
        if (customer.getIsPool() == 1) {
            throw new BusinessException("客户已在公海池中");
        }

        // 更新客户状态
        customer.setIsPool(1);
        customer.setPoolTime(LocalDateTime.now());
        customer.setOwnerId(null);
        customer.setOwnerName(null);
        customerMapper.updateById(customer);

        log.info("客户放入公海池成功, 客户ID: {}", customerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchPutToPool(List<Long> customerIds) {
        // 参数校验
        if (customerIds == null || customerIds.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }

        // 过滤空值并限制批量操作数量
        List<Long> validIds = customerIds.stream()
                .filter(id -> id != null)
                .distinct()
                .limit(100) // 限制每次最多放入100个客户
                .collect(Collectors.toList());

        if (validIds.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }

        // 批量查询客户
        List<Customer> customers = customerMapper.selectBatchIds(validIds);
        
        // 过滤出非公海池中的客户
        List<Customer> nonPoolCustomers = customers.stream()
                .filter(c -> c.getIsPool() != 1)
                .collect(Collectors.toList());

        if (nonPoolCustomers.isEmpty()) {
            throw new BusinessException("没有可放入公海池的客户");
        }

        // 批量更新客户状态
        LocalDateTime now = LocalDateTime.now();
        int successCount = 0;
        for (Customer customer : nonPoolCustomers) {
            try {
                customer.setIsPool(1);
                customer.setPoolTime(now);
                customer.setOwnerId(null);
                customer.setOwnerName(null);
                int updateCount = customerMapper.updateById(customer);
                if (updateCount > 0) {
                    successCount++;
                }
            } catch (Exception e) {
                log.error("放入公海池失败, 客户ID: {}", customer.getId(), e);
            }
        }

        log.info("批量放入公海池完成, 成功数量: {}/{}", successCount, nonPoolCustomers.size());
    }

    @Override
    public PageResult<CustomerPoolRuleResponse> getPoolRulePage(CustomerPoolRuleQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<CustomerPoolRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(request.getRuleName()), CustomerPoolRule::getRuleName, request.getRuleName())
                .eq(StrUtil.isNotBlank(request.getRuleType()), CustomerPoolRule::getRuleType, request.getRuleType())
                .eq(request.getStatus() != null, CustomerPoolRule::getStatus, request.getStatus())
                .orderByAsc(CustomerPoolRule::getPriority)
                .orderByDesc(CustomerPoolRule::getCreateTime);

        // 分页查询
        Page<CustomerPoolRule> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<CustomerPoolRule> rulePage = customerPoolRuleMapper.selectPage(page, wrapper);

        // 转换为响应对象
        List<CustomerPoolRuleResponse> responseList = rulePage.getRecords().stream()
                .map(rule -> {
                    CustomerPoolRuleResponse response = new CustomerPoolRuleResponse();
                    BeanUtils.copyProperties(rule, response);
                    return response;
                })
                .collect(Collectors.toList());

        return PageResult.build(responseList, rulePage.getTotal(), rulePage.getCurrent(), rulePage.getSize());
    }

    @Override
    public List<CustomerPoolRuleResponse> getPoolRuleList() {
        // 查询所有启用的规则,按优先级排序
        LambdaQueryWrapper<CustomerPoolRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerPoolRule::getStatus, 1)
                .orderByAsc(CustomerPoolRule::getPriority);

        List<CustomerPoolRule> ruleList = customerPoolRuleMapper.selectList(wrapper);

        // 转换为响应对象
        return ruleList.stream()
                .map(rule -> {
                    CustomerPoolRuleResponse response = new CustomerPoolRuleResponse();
                    BeanUtils.copyProperties(rule, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPoolRule(CustomerPoolRuleCreateRequest request) {
        // 校验规则类型是否有效
        PoolRuleTypeEnum ruleTypeEnum = PoolRuleTypeEnum.getByCode(request.getRuleType());
        if (ruleTypeEnum == null) {
            throw new BusinessException("无效的规则类型");
        }

        // 检查规则名称是否已存在
        CustomerPoolRule existRule = customerPoolRuleMapper.selectByRuleName(request.getRuleName());
        if (existRule != null) {
            throw new BusinessException("规则名称已存在");
        }

        // 创建规则
        CustomerPoolRule rule = new CustomerPoolRule();
        BeanUtils.copyProperties(request, rule);

        // 设置默认值
        if (rule.getStatus() == null) {
            rule.setStatus(1);
        }
        if (rule.getPriority() == null) {
            rule.setPriority(100);
        }

        customerPoolRuleMapper.insert(rule);

        log.info("创建公海池规则成功, 规则ID: {}, 规则名称: {}", rule.getId(), rule.getRuleName());
        return rule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePoolRule(CustomerPoolRuleUpdateRequest request) {
        // 检查规则是否存在
        CustomerPoolRule rule = customerPoolRuleMapper.selectById(request.getId());
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }

        // 校验规则类型是否有效
        if (StrUtil.isNotBlank(request.getRuleType())) {
            PoolRuleTypeEnum ruleTypeEnum = PoolRuleTypeEnum.getByCode(request.getRuleType());
            if (ruleTypeEnum == null) {
                throw new BusinessException("无效的规则类型");
            }
        }

        // 检查规则名称是否重复
        if (StrUtil.isNotBlank(request.getRuleName()) && !rule.getRuleName().equals(request.getRuleName())) {
            CustomerPoolRule existRule = customerPoolRuleMapper.selectByRuleName(request.getRuleName());
            if (existRule != null) {
                throw new BusinessException("规则名称已存在");
            }
        }

        // 更新规则
        BeanUtils.copyProperties(request, rule);
        customerPoolRuleMapper.updateById(rule);

        log.info("更新公海池规则成功, 规则ID: {}", rule.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePoolRule(Long id) {
        // 检查规则是否存在
        CustomerPoolRule rule = customerPoolRuleMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }

        // 删除规则
        customerPoolRuleMapper.deleteById(id);

        log.info("删除公海池规则成功, 规则ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executePoolRules() {
        log.info("开始执行公海池规则...");

        // 获取所有启用的规则,按优先级排序
        List<CustomerPoolRule> rules = customerPoolRuleMapper.selectList(
                new LambdaQueryWrapper<CustomerPoolRule>()
                        .eq(CustomerPoolRule::getStatus, 1)
                        .orderByAsc(CustomerPoolRule::getPriority)
        );

        if (rules.isEmpty()) {
            log.info("没有启用的公海池规则");
            return;
        }

        int totalProcessed = 0;

        // 执行每条规则
        for (CustomerPoolRule rule : rules) {
            try {
                int processed = executeRule(rule);
                totalProcessed += processed;
                log.info("执行规则[{}]完成, 处理客户数量: {}", rule.getRuleName(), processed);
            } catch (Exception e) {
                log.error("执行规则[{}]失败", rule.getRuleName(), e);
            }
        }

        log.info("公海池规则执行完成, 总处理客户数量: {}", totalProcessed);
    }

    /**
     * 执行单条规则
     *
     * @param rule 公海池规则
     * @return 处理的客户数量
     */
    private int executeRule(CustomerPoolRule rule) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getIsPool, 0); // 只处理非公海池客户

        LocalDateTime thresholdTime = LocalDateTime.now().minusDays(rule.getDaysThreshold());

        switch (rule.getRuleType()) {
            case "no_follow":
                // 未跟进天数超过N天
                wrapper.and(w -> w.isNull(Customer::getLastFollowTime)
                        .or()
                        .lt(Customer::getLastFollowTime, thresholdTime));
                break;

            case "customer_lost":
                // 客户状态为"流失"
                wrapper.eq(Customer::getCustomerStatus, "lost");
                break;

            case "opportunity_closed":
                // 商机关闭后N天未重新开启(需要关联商机表查询,这里简化处理)
                // 实际项目中需要关联查询商机表
                log.warn("商机关闭规则暂未实现,需要关联商机表查询");
                return 0;

            default:
                log.warn("未知的规则类型: {}", rule.getRuleType());
                return 0;
        }

        // 查询符合条件的客户
        List<Customer> customers = customerMapper.selectList(wrapper);

        // 批量更新客户状态
        int count = 0;
        for (Customer customer : customers) {
            try {
                customer.setIsPool(1);
                customer.setPoolTime(LocalDateTime.now());
                customer.setOwnerId(null);
                customer.setOwnerName(null);
                customerMapper.updateById(customer);
                count++;
            } catch (Exception e) {
                log.error("更新客户状态失败, 客户ID: {}", customer.getId(), e);
            }
        }

        return count;
    }

    /**
     * 转义 LIKE 查询中的特殊字符
     *
     * @param keyword 关键字
     * @return 转义后的关键字
     */
    private String escapeLikeSpecialChars(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return keyword;
        }
        // 转义 % 和 _ 特殊字符
        return keyword.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

}

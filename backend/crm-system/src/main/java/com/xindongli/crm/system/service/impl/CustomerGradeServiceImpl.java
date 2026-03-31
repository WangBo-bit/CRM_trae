package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.*;
import com.xindongli.crm.system.dto.response.CustomerGradeRuleResponse;
import com.xindongli.crm.system.entity.Customer;
import com.xindongli.crm.system.entity.CustomerGradeRule;
import com.xindongli.crm.system.mapper.CustomerGradeRuleMapper;
import com.xindongli.crm.system.mapper.CustomerMapper;
import com.xindongli.crm.system.service.CustomerGradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户分级服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerGradeServiceImpl implements CustomerGradeService {

    private final CustomerGradeRuleMapper gradeRuleMapper;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGradeRule(CustomerGradeRuleCreateRequest request) {
        // 参数校验
        validateGradeRule(request.getRuleType(), request.getMinValue(), request.getMaxValue(), request.getTargetGrade());

        // 检查规则名称是否重复
        LambdaQueryWrapper<CustomerGradeRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerGradeRule::getRuleName, request.getRuleName())
               .eq(CustomerGradeRule::getDeleted, 0);
        if (gradeRuleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("规则名称已存在");
        }

        // 创建规则
        CustomerGradeRule rule = new CustomerGradeRule();
        BeanUtils.copyProperties(request, rule);
        
        // 设置默认状态
        if (rule.getStatus() == null) {
            rule.setStatus(1);
        }
        
        gradeRuleMapper.insert(rule);
        
        log.info("创建分级规则成功, 规则ID: {}, 规则名称: {}", rule.getRuleId(), rule.getRuleName());
        return rule.getRuleId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGradeRule(CustomerGradeRuleUpdateRequest request) {
        // 检查规则是否存在
        CustomerGradeRule existRule = gradeRuleMapper.selectById(request.getRuleId());
        if (existRule == null) {
            throw new BusinessException("分级规则不存在");
        }

        // 参数校验
        validateGradeRule(request.getRuleType(), request.getMinValue(), request.getMaxValue(), request.getTargetGrade());

        // 检查规则名称是否重复
        LambdaQueryWrapper<CustomerGradeRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerGradeRule::getRuleName, request.getRuleName())
               .ne(CustomerGradeRule::getRuleId, request.getRuleId())
               .eq(CustomerGradeRule::getDeleted, 0);
        if (gradeRuleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("规则名称已存在");
        }

        // 更新规则
        CustomerGradeRule rule = new CustomerGradeRule();
        BeanUtils.copyProperties(request, rule);
        gradeRuleMapper.updateById(rule);
        
        log.info("更新分级规则成功, 规则ID: {}", rule.getRuleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGradeRule(Long ruleId) {
        // 检查规则是否存在
        CustomerGradeRule rule = gradeRuleMapper.selectById(ruleId);
        if (rule == null) {
            throw new BusinessException("分级规则不存在");
        }

        // 删除规则
        gradeRuleMapper.deleteById(ruleId);
        
        log.info("删除分级规则成功, 规则ID: {}", ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGradeRules(List<Long> ruleIds) {
        // 参数校验
        if (ruleIds == null || ruleIds.isEmpty()) {
            throw new BusinessException("规则ID列表不能为空");
        }
        
        // 限制批量删除数量，防止误操作
        if (ruleIds.size() > 100) {
            throw new BusinessException("单次批量删除规则数量不能超过100");
        }

        // 过滤空值
        List<Long> validIds = ruleIds.stream()
                .filter(id -> id != null)
                .collect(Collectors.toList());

        if (validIds.isEmpty()) {
            throw new BusinessException("规则ID列表不能为空");
        }

        // 批量删除
        int deletedCount = gradeRuleMapper.deleteBatchIds(validIds);
        log.info("批量删除分级规则成功, 删除数量: {}, 规则ID: {}", deletedCount, validIds);
    }

    @Override
    public CustomerGradeRuleResponse getGradeRuleById(Long ruleId) {
        CustomerGradeRule rule = gradeRuleMapper.selectById(ruleId);
        if (rule == null) {
            throw new BusinessException("分级规则不存在");
        }

        CustomerGradeRuleResponse response = new CustomerGradeRuleResponse();
        BeanUtils.copyProperties(rule, response);
        return response;
    }

    @Override
    public PageResult<CustomerGradeRuleResponse> getGradeRulePage(CustomerGradeRuleQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<CustomerGradeRule> wrapper = buildQueryWrapper(request);

        // 分页查询
        Page<CustomerGradeRule> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<CustomerGradeRule> rulePage = gradeRuleMapper.selectPage(page, wrapper);

        // 转换为响应对象
        List<CustomerGradeRuleResponse> responseList = rulePage.getRecords().stream()
                .map(rule -> {
                    CustomerGradeRuleResponse response = new CustomerGradeRuleResponse();
                    BeanUtils.copyProperties(rule, response);
                    return response;
                })
                .collect(Collectors.toList());

        return PageResult.build(responseList, rulePage.getTotal(), rulePage.getCurrent(), rulePage.getSize());
    }

    @Override
    public List<CustomerGradeRuleResponse> getGradeRuleList(CustomerGradeRuleQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<CustomerGradeRule> wrapper = buildQueryWrapper(request);

        List<CustomerGradeRule> ruleList = gradeRuleMapper.selectList(wrapper);

        // 转换为响应对象
        return ruleList.stream()
                .map(rule -> {
                    CustomerGradeRuleResponse response = new CustomerGradeRuleResponse();
                    BeanUtils.copyProperties(rule, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGradeRuleStatus(Long ruleId, Integer status) {
        // 校验状态参数
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException("状态值无效,必须是0或1");
        }
        
        // 检查规则是否存在
        CustomerGradeRule rule = gradeRuleMapper.selectById(ruleId);
        if (rule == null) {
            throw new BusinessException("分级规则不存在");
        }

        // 更新状态
        rule.setStatus(status);
        gradeRuleMapper.updateById(rule);

        log.info("更新分级规则状态成功, 规则ID: {}, 状态: {}", ruleId, status);
    }

    @Override
    public String calculateCustomerGrade(CustomerGradeCalculateRequest request) {
        // 获取所有启用的分级规则(按优先级排序)
        List<CustomerGradeRule> rules = gradeRuleMapper.selectEnabledRulesOrderByPriority();

        return calculateCustomerGradeWithRules(request, rules);
    }

    /**
     * 使用预查询的规则计算客户等级（避免重复查询规则）
     * 
     * @param request 计算请求
     * @param rules 预查询的规则列表
     * @return 客户等级
     */
    private String calculateCustomerGradeWithRules(CustomerGradeCalculateRequest request, List<CustomerGradeRule> rules) {
        if (rules.isEmpty()) {
            // 如果没有配置规则,使用默认规则
            return calculateDefaultGrade(request.getAnnualPurchaseAmount());
        }

        // 按优先级匹配规则
        for (CustomerGradeRule rule : rules) {
            if (matchRule(rule, request)) {
                log.info("客户ID: {} 匹配到规则: {}, 等级: {}", 
                        request.getCustomerId(), rule.getRuleName(), rule.getTargetGrade());
                return rule.getTargetGrade();
            }
        }

        // 如果没有匹配到规则,返回默认等级D
        log.info("客户ID: {} 未匹配到任何规则, 返回默认等级D", request.getCustomerId());
        return "D";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Long, String> batchCalculateCustomerGrade(List<Long> customerIds) {
        // 参数校验
        if (customerIds == null || customerIds.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }
        
        // 限制批量处理数量，防止性能问题
        if (customerIds.size() > 1000) {
            throw new BusinessException("单次批量计算客户数量不能超过1000");
        }
        
        // 过滤空值
        List<Long> validIds = customerIds.stream()
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }
        
        Map<Long, String> result = new HashMap<>();
        
        // 批量查询客户信息（解决N+1查询问题）
        List<Customer> customers = customerMapper.selectBatchIds(validIds);
        if (customers == null || customers.isEmpty()) {
            throw new BusinessException("未找到有效的客户信息");
        }
        
        // 提前查询所有启用的规则（只查询一次，解决N+1查询问题）
        List<CustomerGradeRule> rules = gradeRuleMapper.selectEnabledRulesOrderByPriority();
        
        // 批量计算客户等级
        for (Customer customer : customers) {
            // 构建计算请求
            CustomerGradeCalculateRequest request = new CustomerGradeCalculateRequest();
            request.setCustomerId(customer.getId());
            request.setIndustry(customer.getIndustry());
            request.setCustomerSource(customer.getCustomerSource());
            // TODO: 从其他数据源获取年采购额、合作伙伴类型等信息
            request.setAnnualPurchaseAmount(BigDecimal.ZERO);
            
            // 计算等级
            String grade = calculateCustomerGradeWithRules(request, rules);
            
            // 更新客户等级
            customer.setCustomerLevel(grade);
            
            result.put(customer.getId(), grade);
        }
        
        // 批量更新客户等级
        for (Customer customer : customers) {
            customerMapper.updateById(customer);
        }

        log.info("批量计算客户等级完成, 客户数量: {}", validIds.size());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustCustomerGrade(CustomerGradeAdjustRequest request) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 验证目标等级
        if (!isValidGrade(request.getTargetGrade())) {
            throw new BusinessException("目标等级无效,必须是A/B/C/D之一");
        }

        // 更新客户等级
        customer.setCustomerLevel(request.getTargetGrade());
        customerMapper.updateById(customer);

        log.info("手动调整客户等级成功, 客户ID: {}, 新等级: {}, 原因: {}", 
                request.getCustomerId(), request.getTargetGrade(), request.getAdjustReason());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String calculateAndUpdateCustomerGrade(Long customerId) {
        // 查询客户信息
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 构建计算请求
        CustomerGradeCalculateRequest request = new CustomerGradeCalculateRequest();
        request.setCustomerId(customerId);
        request.setIndustry(customer.getIndustry());
        request.setCustomerSource(customer.getCustomerSource());
        // TODO: 从其他数据源获取年采购额、合作伙伴类型等信息
        // 这里暂时使用默认值,实际项目中需要从订单、合同等模块获取
        request.setAnnualPurchaseAmount(BigDecimal.ZERO);

        // 计算等级
        String grade = calculateCustomerGrade(request);

        // 更新客户等级
        customer.setCustomerLevel(grade);
        customerMapper.updateById(customer);

        log.info("自动计算并更新客户等级成功, 客户ID: {}, 等级: {}", customerId, grade);
        return grade;
    }

    /**
     * 验证分级规则参数
     */
    private void validateGradeRule(String ruleType, BigDecimal minValue, BigDecimal maxValue, String targetGrade) {
        // 验证规则类型
        if (!isValidRuleType(ruleType)) {
            throw new BusinessException("规则类型无效,必须是amount/industry/source/partner_type之一");
        }

        // 验证目标等级
        if (!isValidGrade(targetGrade)) {
            throw new BusinessException("目标等级无效,必须是A/B/C/D之一");
        }

        // 金额规则需要验证最小值和最大值
        if ("amount".equals(ruleType)) {
            if (minValue == null || maxValue == null) {
                throw new BusinessException("金额规则必须设置最小值和最大值");
            }
            if (minValue.compareTo(maxValue) > 0) {
                throw new BusinessException("最小值不能大于最大值");
            }
        }
    }

    /**
     * 匹配规则
     */
    private boolean matchRule(CustomerGradeRule rule, CustomerGradeCalculateRequest request) {
        String ruleType = rule.getRuleType();

        switch (ruleType) {
            case "amount":
                return matchAmountRule(rule, request.getAnnualPurchaseAmount());
            case "industry":
                return matchIndustryRule(rule, request.getIndustry());
            case "source":
                return matchSourceRule(rule, request.getCustomerSource());
            case "partner_type":
                return matchPartnerTypeRule(rule, request.getPartnerType());
            default:
                return false;
        }
    }

    /**
     * 匹配金额规则
     */
    private boolean matchAmountRule(CustomerGradeRule rule, BigDecimal annualPurchaseAmount) {
        if (annualPurchaseAmount == null) {
            return false;
        }

        BigDecimal minValue = rule.getMinValue();
        BigDecimal maxValue = rule.getMaxValue();

        // 判断年采购额是否在[minValue, maxValue]范围内
        return annualPurchaseAmount.compareTo(minValue) >= 0 
            && annualPurchaseAmount.compareTo(maxValue) <= 0;
    }

    /**
     * 匹配行业规则
     * 规则描述格式：行业1,行业2,行业3（逗号分隔）
     */
    private boolean matchIndustryRule(CustomerGradeRule rule, String industry) {
        if (StrUtil.isBlank(industry) || StrUtil.isBlank(rule.getDescription())) {
            return false;
        }
        
        // 解析规则描述中的行业列表
        String[] industries = rule.getDescription().split(",");
        for (String ind : industries) {
            if (industry.trim().equals(ind.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 匹配来源规则
     * 规则描述格式：来源1,来源2,来源3（逗号分隔）
     */
    private boolean matchSourceRule(CustomerGradeRule rule, String customerSource) {
        if (StrUtil.isBlank(customerSource) || StrUtil.isBlank(rule.getDescription())) {
            return false;
        }
        
        // 解析规则描述中的来源列表
        String[] sources = rule.getDescription().split(",");
        for (String source : sources) {
            if (customerSource.trim().equals(source.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 匹配合作伙伴类型规则
     * 规则描述格式：类型1,类型2,类型3（逗号分隔）
     */
    private boolean matchPartnerTypeRule(CustomerGradeRule rule, String partnerType) {
        if (StrUtil.isBlank(partnerType) || StrUtil.isBlank(rule.getDescription())) {
            return false;
        }
        
        // 解析规则描述中的合作伙伴类型列表
        String[] types = rule.getDescription().split(",");
        for (String type : types) {
            if (partnerType.trim().equals(type.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 计算默认等级(基于年采购额)
     * 根据PRD文档的分级标准:
     * A类（战略客户）：年采购潜力≥1000万
     * B类（重点客户）：年采购潜力300-1000万
     * C类（潜力客户）：年采购潜力50-300万
     * D类（一般客户）：年采购潜力<50万
     */
    private String calculateDefaultGrade(BigDecimal annualPurchaseAmount) {
        if (annualPurchaseAmount == null) {
            return "D";
        }

        // 根据PRD文档的分级标准
        if (annualPurchaseAmount.compareTo(new BigDecimal("1000")) >= 0) {
            return "A"; // 年采购额>=1000万
        } else if (annualPurchaseAmount.compareTo(new BigDecimal("300")) >= 0) {
            return "B"; // 年采购额300-1000万
        } else if (annualPurchaseAmount.compareTo(new BigDecimal("50")) >= 0) {
            return "C"; // 年采购额50-300万
        } else {
            return "D"; // 年采购额<50万
        }
    }

    /**
     * 验证规则类型是否有效
     */
    private boolean isValidRuleType(String ruleType) {
        return "amount".equals(ruleType) 
            || "industry".equals(ruleType) 
            || "source".equals(ruleType) 
            || "partner_type".equals(ruleType);
    }

    /**
     * 验证等级是否有效
     */
    private boolean isValidGrade(String grade) {
        return "A".equals(grade) 
            || "B".equals(grade) 
            || "C".equals(grade) 
            || "D".equals(grade);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<CustomerGradeRule> buildQueryWrapper(CustomerGradeRuleQueryRequest request) {
        LambdaQueryWrapper<CustomerGradeRule> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotBlank(request.getRuleName()), CustomerGradeRule::getRuleName, request.getRuleName())
               .eq(StrUtil.isNotBlank(request.getRuleType()), CustomerGradeRule::getRuleType, request.getRuleType())
               .eq(StrUtil.isNotBlank(request.getTargetGrade()), CustomerGradeRule::getTargetGrade, request.getTargetGrade())
               .eq(request.getStatus() != null, CustomerGradeRule::getStatus, request.getStatus())
               .orderByAsc(CustomerGradeRule::getPriority)
               .orderByDesc(CustomerGradeRule::getCreateTime);

        return wrapper;
    }

}

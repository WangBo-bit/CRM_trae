package com.xindongli.crm.system.service;

import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.*;
import com.xindongli.crm.system.dto.response.CustomerGradeRuleResponse;

import java.util.List;

/**
 * 客户分级服务接口
 * 
 * @author 芯动力科技
 */
public interface CustomerGradeService {

    /**
     * 创建分级规则
     * 
     * @param request 创建请求
     * @return 规则ID
     */
    Long createGradeRule(CustomerGradeRuleCreateRequest request);

    /**
     * 更新分级规则
     * 
     * @param request 更新请求
     */
    void updateGradeRule(CustomerGradeRuleUpdateRequest request);

    /**
     * 删除分级规则
     * 
     * @param ruleId 规则ID
     */
    void deleteGradeRule(Long ruleId);

    /**
     * 批量删除分级规则
     * 
     * @param ruleIds 规则ID列表
     */
    void deleteGradeRules(List<Long> ruleIds);

    /**
     * 获取分级规则详情
     * 
     * @param ruleId 规则ID
     * @return 规则详情
     */
    CustomerGradeRuleResponse getGradeRuleById(Long ruleId);

    /**
     * 查询分级规则列表(分页)
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    PageResult<CustomerGradeRuleResponse> getGradeRulePage(CustomerGradeRuleQueryRequest request);

    /**
     * 查询分级规则列表
     * 
     * @param request 查询请求
     * @return 规则列表
     */
    List<CustomerGradeRuleResponse> getGradeRuleList(CustomerGradeRuleQueryRequest request);

    /**
     * 启用/禁用分级规则
     * 
     * @param ruleId 规则ID
     * @param status 状态(0:禁用 1:启用)
     */
    void updateGradeRuleStatus(Long ruleId, Integer status);

    /**
     * 计算客户等级
     * 
     * @param request 计算请求
     * @return 客户等级(A/B/C/D)
     */
    String calculateCustomerGrade(CustomerGradeCalculateRequest request);

    /**
     * 批量计算客户等级
     * 
     * @param customerIds 客户ID列表
     * @return 计算结果Map(客户ID -> 等级)
     */
    java.util.Map<Long, String> batchCalculateCustomerGrade(List<Long> customerIds);

    /**
     * 手动调整客户等级
     * 
     * @param request 调整请求
     */
    void adjustCustomerGrade(CustomerGradeAdjustRequest request);

    /**
     * 根据客户ID自动计算并更新等级
     * 
     * @param customerId 客户ID
     * @return 计算后的等级
     */
    String calculateAndUpdateCustomerGrade(Long customerId);

}

package com.xindongli.crm.system.service;

import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.*;
import com.xindongli.crm.system.dto.response.CustomerPoolCustomerResponse;
import com.xindongli.crm.system.dto.response.CustomerPoolRuleResponse;

import java.util.List;

/**
 * 客户公海池服务接口
 *
 * @author 芯动力科技
 */
public interface CustomerPoolService {

    /**
     * 获取公海池客户列表(分页)
     *
     * @param request 查询请求
     * @return 公海池客户列表
     */
    PageResult<CustomerPoolCustomerResponse> getPoolCustomerPage(CustomerQueryRequest request);

    /**
     * 从公海池认领客户
     *
     * @param request 认领请求
     */
    void claimCustomer(CustomerPoolClaimRequest request);

    /**
     * 批量认领客户
     *
     * @param request 批量认领请求
     */
    void batchClaimCustomers(CustomerPoolBatchClaimRequest request);

    /**
     * 手动放入公海池
     *
     * @param customerId 客户ID
     */
    void putToPool(Long customerId);

    /**
     * 批量放入公海池
     *
     * @param customerIds 客户ID列表
     */
    void batchPutToPool(List<Long> customerIds);

    /**
     * 获取公海池规则列表
     *
     * @param request 查询请求
     * @return 公海池规则列表
     */
    PageResult<CustomerPoolRuleResponse> getPoolRulePage(CustomerPoolRuleQueryRequest request);

    /**
     * 获取公海池规则列表(不分页)
     *
     * @return 公海池规则列表
     */
    List<CustomerPoolRuleResponse> getPoolRuleList();

    /**
     * 创建公海池规则
     *
     * @param request 创建请求
     * @return 规则ID
     */
    Long createPoolRule(CustomerPoolRuleCreateRequest request);

    /**
     * 更新公海池规则
     *
     * @param request 更新请求
     */
    void updatePoolRule(CustomerPoolRuleUpdateRequest request);

    /**
     * 删除公海池规则
     *
     * @param id 规则ID
     */
    void deletePoolRule(Long id);

    /**
     * 执行公海池规则(定时任务调用)
     * 将符合条件的客户自动放入公海池
     */
    void executePoolRules();

}

package com.xindongli.crm.system.service;

import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.CustomerCreateRequest;
import com.xindongli.crm.system.dto.request.CustomerQueryRequest;
import com.xindongli.crm.system.dto.request.CustomerUpdateRequest;
import com.xindongli.crm.system.dto.response.CustomerResponse;

import java.util.List;

/**
 * 客户服务接口
 * 
 * @author 芯动力科技
 */
public interface CustomerService {

    /**
     * 创建客户
     * 
     * @param request 客户创建请求
     * @return 客户ID
     */
    Long createCustomer(CustomerCreateRequest request);

    /**
     * 更新客户
     * 
     * @param request 客户更新请求
     */
    void updateCustomer(CustomerUpdateRequest request);

    /**
     * 删除客户
     * 
     * @param id 客户ID
     */
    void deleteCustomer(Long id);

    /**
     * 批量删除客户
     * 
     * @param ids 客户ID列表
     */
    void deleteCustomers(List<Long> ids);

    /**
     * 获取客户详情
     * 
     * @param id 客户ID
     * @return 客户详情
     */
    CustomerResponse getCustomerById(Long id);

    /**
     * 查询客户列表(分页)
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    PageResult<CustomerResponse> getCustomerPage(CustomerQueryRequest request);

    /**
     * 查询客户列表
     * 
     * @param request 查询请求
     * @return 客户列表
     */
    List<CustomerResponse> getCustomerList(CustomerQueryRequest request);

    /**
     * 转移客户
     * 
     * @param id 客户ID
     * @param ownerId 新负责人ID
     * @param ownerName 新负责人姓名
     */
    void transferCustomer(Long id, Long ownerId, String ownerName);

    /**
     * 客户放入公海池
     * 
     * @param id 客户ID
     */
    void putCustomerToPool(Long id);

    /**
     * 从公海池领取客户
     * 
     * @param id 客户ID
     * @param ownerId 负责人ID
     * @param ownerName 负责人姓名
     */
    void claimCustomerFromPool(Long id, Long ownerId, String ownerName);

}

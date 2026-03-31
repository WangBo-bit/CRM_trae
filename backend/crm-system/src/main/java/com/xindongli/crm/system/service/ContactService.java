package com.xindongli.crm.system.service;

import com.xindongli.crm.system.dto.request.ContactCreateRequest;
import com.xindongli.crm.system.dto.request.ContactQueryRequest;
import com.xindongli.crm.system.dto.request.ContactUpdateRequest;
import com.xindongli.crm.system.dto.response.ContactResponse;

import java.util.List;

/**
 * 联系人服务接口
 * 
 * @author 芯动力科技
 */
public interface ContactService {

    /**
     * 创建联系人
     * 
     * @param request 联系人创建请求
     * @return 联系人ID
     */
    Long createContact(ContactCreateRequest request);

    /**
     * 更新联系人
     * 
     * @param request 联系人更新请求
     */
    void updateContact(ContactUpdateRequest request);

    /**
     * 删除联系人
     * 
     * @param id 联系人ID
     */
    void deleteContact(Long id);

    /**
     * 批量删除联系人
     * 
     * @param ids 联系人ID列表
     */
    void deleteContacts(List<Long> ids);

    /**
     * 获取联系人详情
     * 
     * @param id 联系人ID
     * @return 联系人详情
     */
    ContactResponse getContactById(Long id);

    /**
     * 查询联系人列表
     * 
     * @param request 查询请求
     * @return 联系人列表
     */
    List<ContactResponse> getContactList(ContactQueryRequest request);

    /**
     * 根据客户ID查询联系人列表
     * 
     * @param customerId 客户ID
     * @return 联系人列表
     */
    List<ContactResponse> getContactsByCustomerId(Long customerId);

    /**
     * 设置主要联系人
     * 
     * @param id 联系人ID
     */
    void setPrimaryContact(Long id);

}

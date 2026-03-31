package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.system.dto.request.ContactCreateRequest;
import com.xindongli.crm.system.dto.request.ContactQueryRequest;
import com.xindongli.crm.system.dto.request.ContactUpdateRequest;
import com.xindongli.crm.system.dto.response.ContactResponse;
import com.xindongli.crm.system.entity.Contact;
import com.xindongli.crm.system.entity.Customer;
import com.xindongli.crm.system.mapper.ContactMapper;
import com.xindongli.crm.system.mapper.CustomerMapper;
import com.xindongli.crm.system.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 联系人服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactMapper contactMapper;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createContact(ContactCreateRequest request) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 创建联系人
        Contact contact = new Contact();
        BeanUtils.copyProperties(request, contact);
        
        // 设置默认值
        if (contact.getGender() == null) {
            contact.setGender(0);
        }
        if (contact.getIsPrimary() == null) {
            contact.setIsPrimary(0);
        }
        if (StrUtil.isBlank(contact.getDecisionLevel())) {
            contact.setDecisionLevel("executor");
        }
        
        // 如果设置为主要联系人,需要先取消该客户的其他主要联系人
        if (contact.getIsPrimary() == 1) {
            cancelOtherPrimaryContacts(request.getCustomerId());
        }
        
        contactMapper.insert(contact);
        
        log.info("创建联系人成功, 联系人ID: {}, 姓名: {}", contact.getId(), contact.getName());
        return contact.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContact(ContactUpdateRequest request) {
        // 检查联系人是否存在
        Contact contact = contactMapper.selectById(request.getId());
        if (contact == null) {
            throw new BusinessException("联系人不存在");
        }

        // 检查客户是否存在
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 如果设置为主要联系人,需要先取消该客户的其他主要联系人
        if (request.getIsPrimary() != null && request.getIsPrimary() == 1) {
            cancelOtherPrimaryContacts(request.getCustomerId());
        }

        // 更新联系人
        BeanUtils.copyProperties(request, contact);
        contactMapper.updateById(contact);
        
        log.info("更新联系人成功, 联系人ID: {}", contact.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContact(Long id) {
        // 检查联系人是否存在
        Contact contact = contactMapper.selectById(id);
        if (contact == null) {
            throw new BusinessException("联系人不存在");
        }

        // 删除联系人
        contactMapper.deleteById(id);
        
        log.info("删除联系人成功, 联系人ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContacts(List<Long> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("联系人ID列表不能为空");
        }
        
        // 过滤空值
        List<Long> validIds = ids.stream()
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            throw new BusinessException("联系人ID列表不能为空");
        }
        
        // 批量删除联系人
        int deletedCount = contactMapper.deleteBatchIds(validIds);
        log.info("批量删除联系人成功, 删除数量: {}, 联系人ID: {}", deletedCount, validIds);
    }

    @Override
    public ContactResponse getContactById(Long id) {
        Contact contact = contactMapper.selectById(id);
        if (contact == null) {
            throw new BusinessException("联系人不存在");
        }

        ContactResponse response = new ContactResponse();
        BeanUtils.copyProperties(contact, response);
        return response;
    }

    @Override
    public List<ContactResponse> getContactList(ContactQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Contact> wrapper = buildQueryWrapper(request);
        
        List<Contact> contactList = contactMapper.selectList(wrapper);

        // 转换为响应对象
        return contactList.stream()
                .map(contact -> {
                    ContactResponse response = new ContactResponse();
                    BeanUtils.copyProperties(contact, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactResponse> getContactsByCustomerId(Long customerId) {
        List<Contact> contactList = contactMapper.selectByCustomerId(customerId);

        // 转换为响应对象
        return contactList.stream()
                .map(contact -> {
                    ContactResponse response = new ContactResponse();
                    BeanUtils.copyProperties(contact, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setPrimaryContact(Long id) {
        // 检查联系人是否存在
        Contact contact = contactMapper.selectById(id);
        if (contact == null) {
            throw new BusinessException("联系人不存在");
        }

        // 取消该客户的其他主要联系人
        cancelOtherPrimaryContacts(contact.getCustomerId());

        // 设置为主要联系人
        contact.setIsPrimary(1);
        contactMapper.updateById(contact);
        
        log.info("设置主要联系人成功, 联系人ID: {}", id);
    }

    /**
     * 取消客户的其他主要联系人
     * 
     * @param customerId 客户ID
     */
    private void cancelOtherPrimaryContacts(Long customerId) {
        LambdaUpdateWrapper<Contact> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Contact::getCustomerId, customerId)
                     .eq(Contact::getIsPrimary, 1)
                     .set(Contact::getIsPrimary, 0);
        contactMapper.update(null, updateWrapper);
    }

    /**
     * 构建查询条件
     * 
     * @param request 查询请求
     * @return 查询条件
     */
    private LambdaQueryWrapper<Contact> buildQueryWrapper(ContactQueryRequest request) {
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        
        // 安全处理模糊查询：转义特殊字符
        if (StrUtil.isNotBlank(request.getName())) {
            String escapedName = escapeLikeSpecialChars(request.getName());
            wrapper.like(Contact::getName, escapedName);
        }
        
        wrapper.eq(request.getCustomerId() != null, Contact::getCustomerId, request.getCustomerId())
               .eq(StrUtil.isNotBlank(request.getPosition()), Contact::getPosition, request.getPosition())
               .eq(StrUtil.isNotBlank(request.getDepartment()), Contact::getDepartment, request.getDepartment())
               .eq(request.getIsPrimary() != null, Contact::getIsPrimary, request.getIsPrimary())
               .eq(StrUtil.isNotBlank(request.getDecisionLevel()), Contact::getDecisionLevel, request.getDecisionLevel())
               .orderByDesc(Contact::getIsPrimary)
               .orderByDesc(Contact::getCreateTime);
        
        return wrapper;
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

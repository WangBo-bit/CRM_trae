package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.CustomerCreateRequest;
import com.xindongli.crm.system.dto.request.CustomerQueryRequest;
import com.xindongli.crm.system.dto.request.CustomerUpdateRequest;
import com.xindongli.crm.system.dto.response.CustomerResponse;
import com.xindongli.crm.system.entity.Customer;
import com.xindongli.crm.system.mapper.CustomerMapper;
import com.xindongli.crm.system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCustomer(CustomerCreateRequest request) {
        // 检查企业名称是否已存在
        Customer existCustomer = customerMapper.selectByCompanyName(request.getCompanyName());
        if (existCustomer != null) {
            throw new BusinessException("企业名称已存在");
        }

        // 创建客户
        Customer customer = new Customer();
        BeanUtils.copyProperties(request, customer);
        
        // 生成客户编码
        customer.setCustomerCode(generateCustomerCode());
        
        // 设置默认值
        if (StrUtil.isBlank(customer.getCustomerLevel())) {
            customer.setCustomerLevel("D");
        }
        if (StrUtil.isBlank(customer.getCustomerStatus())) {
            customer.setCustomerStatus("potential");
        }
        if (customer.getIsPool() == null) {
            customer.setIsPool(0);
        }
        
        customerMapper.insert(customer);
        
        log.info("创建客户成功, 客户ID: {}, 企业名称: {}", customer.getId(), customer.getCompanyName());
        return customer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomer(CustomerUpdateRequest request) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(request.getId());
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 检查企业名称是否重复
        if (!customer.getCompanyName().equals(request.getCompanyName())) {
            Customer existCustomer = customerMapper.selectByCompanyName(request.getCompanyName());
            if (existCustomer != null) {
                throw new BusinessException("企业名称已存在");
            }
        }

        // 更新客户
        BeanUtils.copyProperties(request, customer);
        customerMapper.updateById(customer);
        
        log.info("更新客户成功, 客户ID: {}", customer.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(Long id) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 删除客户
        customerMapper.deleteById(id);
        
        log.info("删除客户成功, 客户ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomers(List<Long> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }
        
        // 过滤空值
        List<Long> validIds = ids.stream()
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            throw new BusinessException("客户ID列表不能为空");
        }
        
        // 批量删除客户
        int deletedCount = customerMapper.deleteBatchIds(validIds);
        log.info("批量删除客户成功, 删除数量: {}, 客户ID: {}", deletedCount, validIds);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        CustomerResponse response = new CustomerResponse();
        BeanUtils.copyProperties(customer, response);
        return response;
    }

    @Override
    public PageResult<CustomerResponse> getCustomerPage(CustomerQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Customer> wrapper = buildQueryWrapper(request);
        
        // 分页查询
        Page<Customer> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Customer> customerPage = customerMapper.selectPage(page, wrapper);
        
        // 转换为响应对象
        List<CustomerResponse> responseList = customerPage.getRecords().stream()
                .map(customer -> {
                    CustomerResponse response = new CustomerResponse();
                    BeanUtils.copyProperties(customer, response);
                    return response;
                })
                .collect(Collectors.toList());
        
        return PageResult.build(responseList, customerPage.getTotal(), customerPage.getCurrent(), customerPage.getSize());
    }

    @Override
    public List<CustomerResponse> getCustomerList(CustomerQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Customer> wrapper = buildQueryWrapper(request);
        
        List<Customer> customerList = customerMapper.selectList(wrapper);

        // 转换为响应对象
        return customerList.stream()
                .map(customer -> {
                    CustomerResponse response = new CustomerResponse();
                    BeanUtils.copyProperties(customer, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferCustomer(Long id, Long ownerId, String ownerName) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 更新负责人
        customer.setOwnerId(ownerId);
        customer.setOwnerName(ownerName);
        customerMapper.updateById(customer);
        
        log.info("转移客户成功, 客户ID: {}, 新负责人ID: {}", id, ownerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void putCustomerToPool(Long id) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 更新公海池状态
        customer.setIsPool(1);
        customer.setPoolTime(LocalDateTime.now());
        customer.setOwnerId(null);
        customer.setOwnerName(null);
        customerMapper.updateById(customer);
        
        log.info("客户放入公海池成功, 客户ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimCustomerFromPool(Long id, Long ownerId, String ownerName) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 检查客户是否在公海池
        if (customer.getIsPool() != 1) {
            throw new BusinessException("客户不在公海池中");
        }

        // 更新负责人和公海池状态
        customer.setIsPool(0);
        customer.setPoolTime(null);
        customer.setOwnerId(ownerId);
        customer.setOwnerName(ownerName);
        customerMapper.updateById(customer);
        
        log.info("从公海池领取客户成功, 客户ID: {}, 负责人ID: {}", id, ownerId);
    }

    /**
     * 生成客户编码
     * 格式: CUST + 年月日 + 3位序号
     * 
     * @return 客户编码
     */
    private String generateCustomerCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "CUST" + dateStr;
        
        // 查询当天最大的编码
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(Customer::getCustomerCode, prefix)
               .orderByDesc(Customer::getCustomerCode)
               .last("LIMIT 1");
        Customer lastCustomer = customerMapper.selectOne(wrapper);
        
        int sequence = 1;
        if (lastCustomer != null && lastCustomer.getCustomerCode() != null) {
            String lastCode = lastCustomer.getCustomerCode();
            String lastSequence = lastCode.substring(lastCode.length() - 3);
            sequence = Integer.parseInt(lastSequence) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    /**
     * 构建查询条件
     * 
     * @param request 查询请求
     * @return 查询条件
     */
    private LambdaQueryWrapper<Customer> buildQueryWrapper(CustomerQueryRequest request) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        
        // 安全处理模糊查询：转义特殊字符
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
        
        wrapper.eq(StrUtil.isNotBlank(request.getIndustry()), Customer::getIndustry, request.getIndustry())
               .eq(StrUtil.isNotBlank(request.getCustomerLevel()), Customer::getCustomerLevel, request.getCustomerLevel())
               .eq(StrUtil.isNotBlank(request.getCustomerStatus()), Customer::getCustomerStatus, request.getCustomerStatus())
               .eq(StrUtil.isNotBlank(request.getCustomerSource()), Customer::getCustomerSource, request.getCustomerSource())
               .eq(request.getOwnerId() != null, Customer::getOwnerId, request.getOwnerId())
               .eq(request.getDeptId() != null, Customer::getDeptId, request.getDeptId())
               .eq(request.getIsPool() != null, Customer::getIsPool, request.getIsPool())
               .eq(StrUtil.isNotBlank(request.getProvince()), Customer::getProvince, request.getProvince())
               .eq(StrUtil.isNotBlank(request.getCity()), Customer::getCity, request.getCity())
               .orderByDesc(Customer::getCreateTime);
        
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

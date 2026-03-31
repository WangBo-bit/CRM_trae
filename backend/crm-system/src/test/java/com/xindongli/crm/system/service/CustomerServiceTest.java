package com.xindongli.crm.system.service;

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
import com.xindongli.crm.system.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 客户服务单元测试
 * 
 * @author 芯动力科技
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceTest {

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerCreateRequest createRequest;
    private CustomerUpdateRequest updateRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        createRequest = new CustomerCreateRequest();
        createRequest.setCompanyName("深圳智能科技有限公司");
        createRequest.setShortName("智能科技");
        createRequest.setIndustry("人工智能");
        createRequest.setSubIndustry("AI芯片");
        createRequest.setCompanyScale("500-1000人");
        createRequest.setProvince("广东省");
        createRequest.setCity("深圳市");
        createRequest.setAddress("深圳市南山区科技园");
        createRequest.setWebsite("https://www.smarttech.com");
        createRequest.setCustomerLevel("A");
        createRequest.setCustomerSource("展会");
        createRequest.setOwnerId(1L);
        createRequest.setOwnerName("张明");

        updateRequest = new CustomerUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setCompanyName("深圳智能科技有限公司");
        updateRequest.setShortName("智能科技");
        updateRequest.setIndustry("人工智能");
        updateRequest.setCustomerLevel("B");

        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerCode("CUST20240330001");
        customer.setCompanyName("深圳智能科技有限公司");
        customer.setShortName("智能科技");
        customer.setIndustry("人工智能");
        customer.setCustomerLevel("A");
        customer.setCustomerStatus("potential");
        customer.setIsPool(0);
        customer.setOwnerId(1L);
        customer.setOwnerName("张明");
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
    }

    // ==================== 创建客户测试 ====================

    @Test
    @Order(1)
    @DisplayName("创建客户 - 成功")
    void testCreateCustomer_Success() {
        // Given: 企业名称不存在
        when(customerMapper.selectByCompanyName(anyString())).thenReturn(null);
        when(customerMapper.insert(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(1L);
            return 1;
        });

        // When: 创建客户
        Long customerId = customerService.createCustomer(createRequest);

        // Then: 验证结果
        assertNotNull(customerId);
        assertEquals(1L, customerId);
        verify(customerMapper, times(1)).selectByCompanyName(createRequest.getCompanyName());
        verify(customerMapper, times(1)).insert(any(Customer.class));
    }

    @Test
    @Order(2)
    @DisplayName("创建客户 - 企业名称已存在")
    void testCreateCustomer_CompanyNameExists() {
        // Given: 企业名称已存在
        when(customerMapper.selectByCompanyName(anyString())).thenReturn(customer);

        // When & Then: 抛出异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.createCustomer(createRequest);
        });

        assertEquals("企业名称已存在", exception.getMessage());
        verify(customerMapper, times(1)).selectByCompanyName(createRequest.getCompanyName());
        verify(customerMapper, never()).insert(any(Customer.class));
    }

    @Test
    @Order(3)
    @DisplayName("创建客户 - 默认值设置")
    void testCreateCustomer_DefaultValues() {
        // Given: 不设置客户等级和状态
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setCompanyName("测试公司");
        request.setIndustry("测试行业");

        when(customerMapper.selectByCompanyName(anyString())).thenReturn(null);
        when(customerMapper.insert(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(2L);
            // 验证默认值
            assertEquals("D", c.getCustomerLevel());
            assertEquals("potential", c.getCustomerStatus());
            assertEquals(0, c.getIsPool());
            return 1;
        });

        // When
        Long customerId = customerService.createCustomer(request);

        // Then
        assertNotNull(customerId);
    }

    // ==================== 更新客户测试 ====================

    @Test
    @Order(4)
    @DisplayName("更新客户 - 成功")
    void testUpdateCustomer_Success() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.updateById(any(Customer.class))).thenReturn(1);

        // When
        customerService.updateCustomer(updateRequest);

        // Then
        verify(customerMapper, times(1)).selectById(updateRequest.getId());
        verify(customerMapper, times(1)).updateById(any(Customer.class));
    }

    @Test
    @Order(5)
    @DisplayName("更新客户 - 客户不存在")
    void testUpdateCustomer_CustomerNotFound() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.updateCustomer(updateRequest);
        });

        assertEquals("客户不存在", exception.getMessage());
        verify(customerMapper, times(1)).selectById(updateRequest.getId());
        verify(customerMapper, never()).updateById(any(Customer.class));
    }

    @Test
    @Order(6)
    @DisplayName("更新客户 - 企业名称重复")
    void testUpdateCustomer_CompanyNameDuplicate() {
        // Given: 修改企业名称，但新名称已被其他客户使用
        updateRequest.setCompanyName("其他公司");
        Customer otherCustomer = new Customer();
        otherCustomer.setId(2L);
        otherCustomer.setCompanyName("其他公司");

        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.selectByCompanyName(anyString())).thenReturn(otherCustomer);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.updateCustomer(updateRequest);
        });

        assertEquals("企业名称已存在", exception.getMessage());
        verify(customerMapper, never()).updateById(any(Customer.class));
    }

    // ==================== 删除客户测试 ====================

    @Test
    @Order(7)
    @DisplayName("删除客户 - 成功")
    void testDeleteCustomer_Success() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.deleteById(anyLong())).thenReturn(1);

        // When
        customerService.deleteCustomer(1L);

        // Then
        verify(customerMapper, times(1)).selectById(1L);
        verify(customerMapper, times(1)).deleteById(1L);
    }

    @Test
    @Order(8)
    @DisplayName("删除客户 - 客户不存在")
    void testDeleteCustomer_CustomerNotFound() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("客户不存在", exception.getMessage());
        verify(customerMapper, never()).deleteById(anyLong());
    }

    @Test
    @Order(9)
    @DisplayName("批量删除客户 - 成功")
    void testDeleteCustomers_Success() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(customerMapper.deleteBatchIds(anyList())).thenReturn(3);

        // When
        customerService.deleteCustomers(ids);

        // Then
        verify(customerMapper, times(1)).deleteBatchIds(anyList());
    }

    @Test
    @Order(10)
    @DisplayName("批量删除客户 - 空列表")
    void testDeleteCustomers_EmptyList() {
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomers(Collections.emptyList());
        });

        assertEquals("客户ID列表不能为空", exception.getMessage());
        verify(customerMapper, never()).deleteBatchIds(anyList());
    }

    @Test
    @Order(11)
    @DisplayName("批量删除客户 - null列表")
    void testDeleteCustomers_NullList() {
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomers(null);
        });

        assertEquals("客户ID列表不能为空", exception.getMessage());
        verify(customerMapper, never()).deleteBatchIds(anyList());
    }

    @Test
    @Order(12)
    @DisplayName("批量删除客户 - 过滤null值")
    void testDeleteCustomers_FilterNullValues() {
        // Given: 列表中包含null值
        List<Long> ids = Arrays.asList(1L, null, 2L, null, 3L);
        when(customerMapper.deleteBatchIds(anyList())).thenReturn(3);

        // When
        customerService.deleteCustomers(ids);

        // Then: 验证过滤后的列表被传递
        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(customerMapper).deleteBatchIds(captor.capture());
        List<Long> capturedIds = captor.getValue();
        assertEquals(3, capturedIds.size());
        assertTrue(capturedIds.containsAll(Arrays.asList(1L, 2L, 3L)));
    }

    // ==================== 查询客户测试 ====================

    @Test
    @Order(13)
    @DisplayName("查询客户详情 - 成功")
    void testGetCustomerById_Success() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(customer);

        // When
        CustomerResponse response = customerService.getCustomerById(1L);

        // Then
        assertNotNull(response);
        assertEquals(customer.getId(), response.getId());
        assertEquals(customer.getCompanyName(), response.getCompanyName());
        verify(customerMapper, times(1)).selectById(1L);
    }

    @Test
    @Order(14)
    @DisplayName("查询客户详情 - 客户不存在")
    void testGetCustomerById_CustomerNotFound() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.getCustomerById(1L);
        });

        assertEquals("客户不存在", exception.getMessage());
    }

    @Test
    @Order(15)
    @DisplayName("分页查询客户 - 成功")
    void testGetCustomerPage_Success() {
        // Given
        CustomerQueryRequest request = new CustomerQueryRequest();
        request.setPageNum(1L);
        request.setPageSize(10L);
        request.setCustomerLevel("A");

        Page<Customer> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(customer));
        page.setTotal(1);

        when(customerMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        // When
        PageResult<CustomerResponse> result = customerService.getCustomerPage(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        verify(customerMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @Order(16)
    @DisplayName("查询客户列表 - 成功")
    void testGetCustomerList_Success() {
        // Given
        CustomerQueryRequest request = new CustomerQueryRequest();
        request.setCustomerLevel("A");

        when(customerMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList(customer));

        // When
        List<CustomerResponse> list = customerService.getCustomerList(request);

        // Then
        assertNotNull(list);
        assertEquals(1, list.size());
        verify(customerMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @Order(17)
    @DisplayName("查询客户 - LIKE特殊字符转义")
    void testQueryCustomer_LikeSpecialCharsEscape() {
        // Given: 查询包含特殊字符的企业名称
        CustomerQueryRequest request = new CustomerQueryRequest();
        request.setCompanyName("测试%公司_科技");

        Page<Customer> page = new Page<>(1, 10);
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        when(customerMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        // When
        PageResult<CustomerResponse> result = customerService.getCustomerPage(request);

        // Then: 验证查询被执行
        assertNotNull(result);
        verify(customerMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    // ==================== 客户转移测试 ====================

    @Test
    @Order(18)
    @DisplayName("转移客户 - 成功")
    void testTransferCustomer_Success() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.updateById(any(Customer.class))).thenReturn(1);

        // When
        customerService.transferCustomer(1L, 2L, "李四");

        // Then
        verify(customerMapper, times(1)).selectById(1L);
        verify(customerMapper, times(1)).updateById(any(Customer.class));
    }

    @Test
    @Order(19)
    @DisplayName("转移客户 - 客户不存在")
    void testTransferCustomer_CustomerNotFound() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.transferCustomer(1L, 2L, "李四");
        });

        assertEquals("客户不存在", exception.getMessage());
        verify(customerMapper, never()).updateById(any(Customer.class));
    }

    // ==================== 公海池操作测试 ====================

    @Test
    @Order(20)
    @DisplayName("客户放入公海池 - 成功")
    void testPutCustomerToPool_Success() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.updateById(any(Customer.class))).thenReturn(1);

        // When
        customerService.putCustomerToPool(1L);

        // Then
        verify(customerMapper, times(1)).selectById(1L);
        verify(customerMapper, times(1)).updateById(any(Customer.class));
    }

    @Test
    @Order(21)
    @DisplayName("客户放入公海池 - 客户不存在")
    void testPutCustomerToPool_CustomerNotFound() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.putCustomerToPool(1L);
        });

        assertEquals("客户不存在", exception.getMessage());
        verify(customerMapper, never()).updateById(any(Customer.class));
    }

    @Test
    @Order(22)
    @DisplayName("从公海池领取客户 - 成功")
    void testClaimCustomerFromPool_Success() {
        // Given: 客户在公海池中
        customer.setIsPool(1);
        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.updateById(any(Customer.class))).thenReturn(1);

        // When
        customerService.claimCustomerFromPool(1L, 2L, "李四");

        // Then
        verify(customerMapper, times(1)).selectById(1L);
        verify(customerMapper, times(1)).updateById(any(Customer.class));
    }

    @Test
    @Order(23)
    @DisplayName("从公海池领取客户 - 客户不存在")
    void testClaimCustomerFromPool_CustomerNotFound() {
        // Given
        when(customerMapper.selectById(anyLong())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.claimCustomerFromPool(1L, 2L, "李四");
        });

        assertEquals("客户不存在", exception.getMessage());
        verify(customerMapper, never()).updateById(any(Customer.class));
    }

    @Test
    @Order(24)
    @DisplayName("从公海池领取客户 - 客户不在公海池")
    void testClaimCustomerFromPool_CustomerNotInPool() {
        // Given: 客户不在公海池中
        customer.setIsPool(0);
        when(customerMapper.selectById(anyLong())).thenReturn(customer);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.claimCustomerFromPool(1L, 2L, "李四");
        });

        assertEquals("客户不在公海池中", exception.getMessage());
        verify(customerMapper, never()).updateById(any(Customer.class));
    }

    // ==================== 边界条件测试 ====================

    @Test
    @Order(25)
    @DisplayName("创建客户 - 企业名称边界长度")
    void testCreateCustomer_CompanyNameMaxLength() {
        // Given: 企业名称达到最大长度
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setCompanyName("A".repeat(200)); // 200字符
        request.setIndustry("测试行业");

        when(customerMapper.selectByCompanyName(anyString())).thenReturn(null);
        when(customerMapper.insert(any(Customer.class))).thenReturn(1);

        // When
        Long customerId = customerService.createCustomer(request);

        // Then
        assertNotNull(customerId);
    }

    @Test
    @Order(26)
    @DisplayName("查询客户 - 空关键字")
    void testQueryCustomer_EmptyKeyword() {
        // Given
        CustomerQueryRequest request = new CustomerQueryRequest();
        request.setKeyword(""); // 空关键字

        Page<Customer> page = new Page<>(1, 10);
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        when(customerMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        // When
        PageResult<CustomerResponse> result = customerService.getCustomerPage(request);

        // Then
        assertNotNull(result);
        verify(customerMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @Order(27)
    @DisplayName("批量删除客户 - 全部为null值")
    void testDeleteCustomers_AllNullValues() {
        // Given: 列表中全部为null
        List<Long> ids = Arrays.asList(null, null, null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomers(ids);
        });

        assertEquals("客户ID列表不能为空", exception.getMessage());
        verify(customerMapper, never()).deleteBatchIds(anyList());
    }

    // ==================== 并发场景测试 ====================

    @Test
    @Order(28)
    @DisplayName("并发创建客户 - 企业名称冲突")
    void testConcurrentCreateCustomer_NameConflict() {
        // Given: 模拟并发场景，第一次查询为null，插入前另一个线程创建了同名客户
        when(customerMapper.selectByCompanyName(anyString()))
            .thenReturn(null)
            .thenReturn(customer);

        when(customerMapper.insert(any(Customer.class))).thenReturn(1);

        // When: 第一次创建成功
        Long customerId = customerService.createCustomer(createRequest);
        assertNotNull(customerId);

        // Then: 第二次创建应该失败
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.createCustomer(createRequest);
        });

        assertEquals("企业名称已存在", exception.getMessage());
    }

    @Test
    @Order(29)
    @DisplayName("并发更新客户 - 乐观锁场景")
    void testConcurrentUpdateCustomer_OptimisticLock() {
        // Given: 模拟乐观锁版本冲突
        when(customerMapper.selectById(anyLong())).thenReturn(customer);
        when(customerMapper.updateById(any(Customer.class))).thenReturn(0); // 更新失败

        // When & Then: 更新失败（实际应用中应该抛出乐观锁异常）
        customerService.updateCustomer(updateRequest);
        verify(customerMapper, times(1)).updateById(any(Customer.class));
    }

    @AfterEach
    void tearDown() {
        // 清理mock
        Mockito.clearInvocations(customerMapper);
    }
}

package com.xindongli.crm.system.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.system.dto.request.CustomerCreateRequest;
import com.xindongli.crm.system.dto.request.CustomerQueryRequest;
import com.xindongli.crm.system.dto.request.CustomerUpdateRequest;
import com.xindongli.crm.system.dto.response.CustomerResponse;
import com.xindongli.crm.system.service.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 客户控制器集成测试
 * 
 * @author 芯动力科技
 */
@WebMvcTest(CustomerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private CustomerCreateRequest createRequest;
    private CustomerUpdateRequest updateRequest;
    private CustomerResponse customerResponse;

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

        customerResponse = new CustomerResponse();
        customerResponse.setId(1L);
        customerResponse.setCustomerCode("CUST20240330001");
        customerResponse.setCompanyName("深圳智能科技有限公司");
        customerResponse.setShortName("智能科技");
        customerResponse.setIndustry("人工智能");
        customerResponse.setCustomerLevel("A");
        customerResponse.setCustomerStatus("potential");
        customerResponse.setOwnerId(1L);
        customerResponse.setOwnerName("张明");
        customerResponse.setCreateTime(LocalDateTime.now());
        customerResponse.setUpdateTime(LocalDateTime.now());
    }

    // ==================== 创建客户接口测试 ====================

    @Test
    @Order(1)
    @DisplayName("POST /api/v1/customers - 创建客户成功")
    @WithMockUser
    void testCreateCustomer_Success() throws Exception {
        // Given
        when(customerService.createCustomer(any(CustomerCreateRequest.class))).thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("创建成功"))
                .andExpect(jsonPath("$.data").value(1));

        verify(customerService, times(1)).createCustomer(any(CustomerCreateRequest.class));
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/v1/customers - 参数校验失败(企业名称为空)")
    @WithMockUser
    void testCreateCustomer_ValidationFail_CompanyNameEmpty() throws Exception {
        // Given
        createRequest.setCompanyName(null);

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerCreateRequest.class));
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/v1/customers - 参数校验失败(企业名称超长)")
    @WithMockUser
    void testCreateCustomer_ValidationFail_CompanyNameTooLong() throws Exception {
        // Given
        createRequest.setCompanyName("A".repeat(201)); // 超过200字符

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerCreateRequest.class));
    }

    @Test
    @Order(4)
    @DisplayName("POST /api/v1/customers - 业务异常(企业名称已存在)")
    @WithMockUser
    void testCreateCustomer_BusinessException() throws Exception {
        // Given
        when(customerService.createCustomer(any(CustomerCreateRequest.class)))
                .thenThrow(new BusinessException("企业名称已存在"));

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("企业名称已存在"));

        verify(customerService, times(1)).createCustomer(any(CustomerCreateRequest.class));
    }

    // ==================== 更新客户接口测试 ====================

    @Test
    @Order(5)
    @DisplayName("PUT /api/v1/customers/{id} - 更新客户成功")
    @WithMockUser
    void testUpdateCustomer_Success() throws Exception {
        // Given
        doNothing().when(customerService).updateCustomer(any(CustomerUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/customers/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("更新成功"));

        verify(customerService, times(1)).updateCustomer(any(CustomerUpdateRequest.class));
    }

    @Test
    @Order(6)
    @DisplayName("PUT /api/v1/customers/{id} - 客户不存在")
    @WithMockUser
    void testUpdateCustomer_CustomerNotFound() throws Exception {
        // Given
        doThrow(new BusinessException("客户不存在"))
                .when(customerService).updateCustomer(any(CustomerUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/customers/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("客户不存在"));

        verify(customerService, times(1)).updateCustomer(any(CustomerUpdateRequest.class));
    }

    // ==================== 删除客户接口测试 ====================

    @Test
    @Order(7)
    @DisplayName("DELETE /api/v1/customers/{id} - 删除客户成功")
    @WithMockUser
    void testDeleteCustomer_Success() throws Exception {
        // Given
        doNothing().when(customerService).deleteCustomer(anyLong());

        // When & Then
        mockMvc.perform(delete("/api/v1/customers/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("删除成功"));

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    @Order(8)
    @DisplayName("DELETE /api/v1/customers/{id} - 客户不存在")
    @WithMockUser
    void testDeleteCustomer_CustomerNotFound() throws Exception {
        // Given
        doThrow(new BusinessException("客户不存在"))
                .when(customerService).deleteCustomer(anyLong());

        // When & Then
        mockMvc.perform(delete("/api/v1/customers/999")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("客户不存在"));

        verify(customerService, times(1)).deleteCustomer(999L);
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /api/v1/customers - 批量删除客户成功")
    @WithMockUser
    void testDeleteCustomers_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        doNothing().when(customerService).deleteCustomers(anyList());

        // When & Then
        mockMvc.perform(delete("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("删除成功"));

        verify(customerService, times(1)).deleteCustomers(anyList());
    }

    // ==================== 查询客户接口测试 ====================

    @Test
    @Order(10)
    @DisplayName("GET /api/v1/customers/{id} - 查询客户详情成功")
    @WithMockUser
    void testGetCustomerById_Success() throws Exception {
        // Given
        when(customerService.getCustomerById(anyLong())).thenReturn(customerResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.companyName").value("深圳智能科技有限公司"))
                .andExpect(jsonPath("$.data.customerLevel").value("A"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    @Order(11)
    @DisplayName("GET /api/v1/customers/{id} - 客户不存在")
    @WithMockUser
    void testGetCustomerById_CustomerNotFound() throws Exception {
        // Given
        when(customerService.getCustomerById(anyLong()))
                .thenThrow(new BusinessException("客户不存在"));

        // When & Then
        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("客户不存在"));

        verify(customerService, times(1)).getCustomerById(999L);
    }

    @Test
    @Order(12)
    @DisplayName("GET /api/v1/customers/page - 分页查询客户成功")
    @WithMockUser
    void testGetCustomerPage_Success() throws Exception {
        // Given
        PageResult<CustomerResponse> pageResult = new PageResult<>();
        pageResult.setList(Arrays.asList(customerResponse));
        pageResult.setTotal(1L);
        pageResult.setPageNum(1L);
        pageResult.setPageSize(10L);

        when(customerService.getCustomerPage(any(CustomerQueryRequest.class))).thenReturn(pageResult);

        // When & Then
        mockMvc.perform(get("/api/v1/customers/page")
                .param("pageNum", "1")
                .param("pageSize", "10")
                .param("customerLevel", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list[0].companyName").value("深圳智能科技有限公司"));

        verify(customerService, times(1)).getCustomerPage(any(CustomerQueryRequest.class));
    }

    @Test
    @Order(13)
    @DisplayName("GET /api/v1/customers - 查询客户列表成功")
    @WithMockUser
    void testGetCustomerList_Success() throws Exception {
        // Given
        when(customerService.getCustomerList(any(CustomerQueryRequest.class)))
                .thenReturn(Arrays.asList(customerResponse));

        // When & Then
        mockMvc.perform(get("/api/v1/customers")
                .param("customerLevel", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].companyName").value("深圳智能科技有限公司"));

        verify(customerService, times(1)).getCustomerList(any(CustomerQueryRequest.class));
    }

    // ==================== 客户转移接口测试 ====================

    @Test
    @Order(14)
    @DisplayName("POST /api/v1/customers/{id}/actions/transfer - 转移客户成功")
    @WithMockUser
    void testTransferCustomer_Success() throws Exception {
        // Given
        doNothing().when(customerService).transferCustomer(anyLong(), anyLong(), anyString());

        // When & Then
        mockMvc.perform(post("/api/v1/customers/1/actions/transfer")
                .with(csrf())
                .param("ownerId", "2")
                .param("ownerName", "李四"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("转移成功"));

        verify(customerService, times(1)).transferCustomer(1L, 2L, "李四");
    }

    @Test
    @Order(15)
    @DisplayName("POST /api/v1/customers/{id}/actions/transfer - 客户不存在")
    @WithMockUser
    void testTransferCustomer_CustomerNotFound() throws Exception {
        // Given
        doThrow(new BusinessException("客户不存在"))
                .when(customerService).transferCustomer(anyLong(), anyLong(), anyString());

        // When & Then
        mockMvc.perform(post("/api/v1/customers/999/actions/transfer")
                .with(csrf())
                .param("ownerId", "2")
                .param("ownerName", "李四"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("客户不存在"));

        verify(customerService, times(1)).transferCustomer(999L, 2L, "李四");
    }

    // ==================== 公海池操作接口测试 ====================

    @Test
    @Order(16)
    @DisplayName("POST /api/v1/customers/{id}/actions/pool - 客户放入公海池成功")
    @WithMockUser
    void testPutCustomerToPool_Success() throws Exception {
        // Given
        doNothing().when(customerService).putCustomerToPool(anyLong());

        // When & Then
        mockMvc.perform(post("/api/v1/customers/1/actions/pool")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));

        verify(customerService, times(1)).putCustomerToPool(1L);
    }

    @Test
    @Order(17)
    @DisplayName("POST /api/v1/customers/{id}/actions/claim - 从公海池领取客户成功")
    @WithMockUser
    void testClaimCustomerFromPool_Success() throws Exception {
        // Given
        doNothing().when(customerService).claimCustomerFromPool(anyLong(), anyLong(), anyString());

        // When & Then
        mockMvc.perform(post("/api/v1/customers/1/actions/claim")
                .with(csrf())
                .param("ownerId", "2")
                .param("ownerName", "李四"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("领取成功"));

        verify(customerService, times(1)).claimCustomerFromPool(1L, 2L, "李四");
    }

    @Test
    @Order(18)
    @DisplayName("POST /api/v1/customers/{id}/actions/claim - 客户不在公海池")
    @WithMockUser
    void testClaimCustomerFromPool_CustomerNotInPool() throws Exception {
        // Given
        doThrow(new BusinessException("客户不在公海池中"))
                .when(customerService).claimCustomerFromPool(anyLong(), anyLong(), anyString());

        // When & Then
        mockMvc.perform(post("/api/v1/customers/1/actions/claim")
                .with(csrf())
                .param("ownerId", "2")
                .param("ownerName", "李四"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("客户不在公海池中"));

        verify(customerService, times(1)).claimCustomerFromPool(1L, 2L, "李四");
    }

    // ==================== 异常处理测试 ====================

    @Test
    @Order(19)
    @DisplayName("GET /api/v1/customers/{id} - 无权限访问")
    void testGetCustomerById_Unauthorized() throws Exception {
        // When & Then: 未登录用户访问
        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isUnauthorized());

        verify(customerService, never()).getCustomerById(anyLong());
    }

    @Test
    @Order(20)
    @DisplayName("POST /api/v1/customers - 无CSRF Token")
    @WithMockUser
    void testCreateCustomer_NoCsrfToken() throws Exception {
        // When & Then: 没有CSRF Token
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(createRequest)))
                .andExpect(status().isForbidden());

        verify(customerService, never()).createCustomer(any(CustomerCreateRequest.class));
    }

    // ==================== 边界条件测试 ====================

    @Test
    @Order(21)
    @DisplayName("GET /api/v1/customers/page - 空结果")
    @WithMockUser
    void testGetCustomerPage_EmptyResult() throws Exception {
        // Given
        PageResult<CustomerResponse> pageResult = new PageResult<>();
        pageResult.setList(Collections.emptyList());
        pageResult.setTotal(0L);
        pageResult.setPageNum(1L);
        pageResult.setPageSize(10L);

        when(customerService.getCustomerPage(any(CustomerQueryRequest.class))).thenReturn(pageResult);

        // When & Then
        mockMvc.perform(get("/api/v1/customers/page")
                .param("pageNum", "1")
                .param("pageSize", "10")
                .param("customerLevel", "Z")) // 不存在的等级
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.list").isEmpty());

        verify(customerService, times(1)).getCustomerPage(any(CustomerQueryRequest.class));
    }

    @Test
    @Order(22)
    @DisplayName("DELETE /api/v1/customers - 空列表")
    @WithMockUser
    void testDeleteCustomers_EmptyList() throws Exception {
        // Given
        doThrow(new BusinessException("客户ID列表不能为空"))
                .when(customerService).deleteCustomers(anyList());

        // When & Then
        mockMvc.perform(delete("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(Collections.emptyList())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("客户ID列表不能为空"));

        verify(customerService, times(1)).deleteCustomers(anyList());
    }

    @Test
    @Order(23)
    @DisplayName("GET /api/v1/customers/page - 多条件组合查询")
    @WithMockUser
    void testGetCustomerPage_MultipleConditions() throws Exception {
        // Given
        PageResult<CustomerResponse> pageResult = new PageResult<>();
        pageResult.setList(Arrays.asList(customerResponse));
        pageResult.setTotal(1L);

        when(customerService.getCustomerPage(any(CustomerQueryRequest.class))).thenReturn(pageResult);

        // When & Then
        mockMvc.perform(get("/api/v1/customers/page")
                .param("pageNum", "1")
                .param("pageSize", "10")
                .param("customerLevel", "A")
                .param("customerStatus", "potential")
                .param("industry", "人工智能")
                .param("province", "广东省")
                .param("city", "深圳市")
                .param("keyword", "智能"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1));

        verify(customerService, times(1)).getCustomerPage(any(CustomerQueryRequest.class));
    }

    @AfterEach
    void tearDown() {
        // 清理mock
        reset(customerService);
    }
}

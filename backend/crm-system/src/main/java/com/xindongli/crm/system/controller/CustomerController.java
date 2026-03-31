package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.request.CustomerCreateRequest;
import com.xindongli.crm.system.dto.request.CustomerQueryRequest;
import com.xindongli.crm.system.dto.request.CustomerUpdateRequest;
import com.xindongli.crm.system.dto.response.CustomerResponse;
import com.xindongli.crm.system.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "客户管理", description = "客户管理相关接口")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 创建客户
     */
    @Operation(summary = "创建客户")
    @PostMapping
    public Result<Long> createCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        Long customerId = customerService.createCustomer(request);
        return Result.success("创建成功", customerId);
    }

    /**
     * 更新客户
     */
    @Operation(summary = "更新客户")
    @PutMapping("/{id}")
    public Result<Void> updateCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request) {
        request.setId(id);
        customerService.updateCustomer(request);
        return Result.success("更新成功");
    }

    /**
     * 删除客户
     */
    @Operation(summary = "删除客户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@Parameter(description = "客户ID") @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除客户
     */
    @Operation(summary = "批量删除客户")
    @DeleteMapping
    public Result<Void> deleteCustomers(@RequestBody List<Long> ids) {
        customerService.deleteCustomers(ids);
        return Result.success("删除成功");
    }

    /**
     * 获取客户详情
     */
    @Operation(summary = "获取客户详情")
    @GetMapping("/{id}")
    public Result<CustomerResponse> getCustomerById(@Parameter(description = "客户ID") @PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return Result.success(response);
    }

    /**
     * 查询客户列表(分页)
     */
    @Operation(summary = "查询客户列表(分页)")
    @GetMapping("/page")
    public Result<PageResult<CustomerResponse>> getCustomerPage(@Valid CustomerQueryRequest request) {
        PageResult<CustomerResponse> pageResult = customerService.getCustomerPage(request);
        return Result.success(pageResult);
    }

    /**
     * 查询客户列表
     */
    @Operation(summary = "查询客户列表")
    @GetMapping
    public Result<List<CustomerResponse>> getCustomerList(@Valid CustomerQueryRequest request) {
        List<CustomerResponse> list = customerService.getCustomerList(request);
        return Result.success(list);
    }

    /**
     * 转移客户
     */
    @Operation(summary = "转移客户")
    @PostMapping("/{id}/actions/transfer")
    public Result<Void> transferCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @Parameter(description = "新负责人ID") @RequestParam Long ownerId,
            @Parameter(description = "新负责人姓名") @RequestParam String ownerName) {
        customerService.transferCustomer(id, ownerId, ownerName);
        return Result.success("转移成功");
    }

    /**
     * 客户放入公海池
     */
    @Operation(summary = "客户放入公海池")
    @PostMapping("/{id}/actions/pool")
    public Result<Void> putCustomerToPool(@Parameter(description = "客户ID") @PathVariable Long id) {
        customerService.putCustomerToPool(id);
        return Result.success("操作成功");
    }

    /**
     * 从公海池领取客户
     */
    @Operation(summary = "从公海池领取客户")
    @PostMapping("/{id}/actions/claim")
    public Result<Void> claimCustomerFromPool(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @Parameter(description = "负责人ID") @RequestParam Long ownerId,
            @Parameter(description = "负责人姓名") @RequestParam String ownerName) {
        customerService.claimCustomerFromPool(id, ownerId, ownerName);
        return Result.success("领取成功");
    }

}

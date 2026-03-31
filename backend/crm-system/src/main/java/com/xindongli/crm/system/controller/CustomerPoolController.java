package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.annotation.RequirePermission;
import com.xindongli.crm.system.dto.request.*;
import com.xindongli.crm.system.dto.response.CustomerPoolCustomerResponse;
import com.xindongli.crm.system.dto.response.CustomerPoolRuleResponse;
import com.xindongli.crm.system.service.CustomerPoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户公海池控制器
 *
 * @author 芯动力科技
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customer-pool")
@RequiredArgsConstructor
@Tag(name = "客户公海池管理", description = "客户公海池相关接口")
public class CustomerPoolController {

    private final CustomerPoolService customerPoolService;

    /**
     * 获取公海池客户列表(分页)
     */
    @GetMapping("/customers")
    @Operation(summary = "获取公海池客户列表", description = "分页查询公海池客户列表")
    @RequirePermission("customer:pool:view")
    public Result<PageResult<CustomerPoolCustomerResponse>> getPoolCustomerPage(
            @Parameter(description = "查询请求") CustomerQueryRequest request) {
        PageResult<CustomerPoolCustomerResponse> result = customerPoolService.getPoolCustomerPage(request);
        return Result.success(result);
    }

    /**
     * 从公海池认领客户
     */
    @PostMapping("/claim")
    @Operation(summary = "从公海池认领客户", description = "从公海池认领单个客户")
    @RequirePermission("customer:pool:claim")
    public Result<Void> claimCustomer(@Validated @RequestBody CustomerPoolClaimRequest request) {
        customerPoolService.claimCustomer(request);
        return Result.success("认领成功");
    }

    /**
     * 批量认领客户
     */
    @PostMapping("/batch-claim")
    @Operation(summary = "批量认领客户", description = "从公海池批量认领客户")
    @RequirePermission("customer:pool:claim")
    public Result<Void> batchClaimCustomers(@Validated @RequestBody CustomerPoolBatchClaimRequest request) {
        customerPoolService.batchClaimCustomers(request);
        return Result.success("批量认领成功");
    }

    /**
     * 手动放入公海池
     */
    @PostMapping("/put/{customerId}")
    @Operation(summary = "手动放入公海池", description = "将客户手动放入公海池")
    @RequirePermission("customer:pool:put")
    public Result<Void> putToPool(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        customerPoolService.putToPool(customerId);
        return Result.success("放入公海池成功");
    }

    /**
     * 批量放入公海池
     */
    @PostMapping("/batch-put")
    @Operation(summary = "批量放入公海池", description = "批量将客户放入公海池")
    @RequirePermission("customer:pool:put")
    public Result<Void> batchPutToPool(@Validated @RequestBody CustomerPoolBatchPutRequest request) {
        customerPoolService.batchPutToPool(request.getCustomerIds());
        return Result.success("批量放入公海池成功");
    }

    /**
     * 获取公海池规则列表(分页)
     */
    @GetMapping("/rules")
    @Operation(summary = "获取公海池规则列表", description = "分页查询公海池规则列表")
    @RequirePermission("customer:pool:rule:view")
    public Result<PageResult<CustomerPoolRuleResponse>> getPoolRulePage(
            @Parameter(description = "查询请求") CustomerPoolRuleQueryRequest request) {
        PageResult<CustomerPoolRuleResponse> result = customerPoolService.getPoolRulePage(request);
        return Result.success(result);
    }

    /**
     * 获取公海池规则列表(不分页)
     */
    @GetMapping("/rules/list")
    @Operation(summary = "获取公海池规则列表(不分页)", description = "查询所有启用的公海池规则")
    @RequirePermission("customer:pool:rule:view")
    public Result<List<CustomerPoolRuleResponse>> getPoolRuleList() {
        List<CustomerPoolRuleResponse> result = customerPoolService.getPoolRuleList();
        return Result.success(result);
    }

    /**
     * 创建公海池规则
     */
    @PostMapping("/rules")
    @Operation(summary = "创建公海池规则", description = "创建新的公海池规则")
    @RequirePermission("customer:pool:rule:create")
    public Result<Long> createPoolRule(@Validated @RequestBody CustomerPoolRuleCreateRequest request) {
        Long ruleId = customerPoolService.createPoolRule(request);
        return Result.success("创建成功", ruleId);
    }

    /**
     * 更新公海池规则
     */
    @PutMapping("/rules")
    @Operation(summary = "更新公海池规则", description = "更新公海池规则信息")
    @RequirePermission("customer:pool:rule:update")
    public Result<Void> updatePoolRule(@Validated @RequestBody CustomerPoolRuleUpdateRequest request) {
        customerPoolService.updatePoolRule(request);
        return Result.success("更新成功");
    }

    /**
     * 删除公海池规则
     */
    @DeleteMapping("/rules/{id}")
    @Operation(summary = "删除公海池规则", description = "删除指定的公海池规则")
    @RequirePermission("customer:pool:rule:delete")
    public Result<Void> deletePoolRule(
            @Parameter(description = "规则ID") @PathVariable Long id) {
        customerPoolService.deletePoolRule(id);
        return Result.success("删除成功");
    }

    /**
     * 执行公海池规则(手动触发)
     */
    @PostMapping("/execute")
    @Operation(summary = "执行公海池规则", description = "手动触发公海池规则执行")
    @RequirePermission("customer:pool:execute")
    public Result<Void> executePoolRules() {
        customerPoolService.executePoolRules();
        return Result.success("执行成功");
    }

}

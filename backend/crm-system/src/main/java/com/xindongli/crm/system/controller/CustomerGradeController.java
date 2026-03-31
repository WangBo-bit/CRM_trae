package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.PageResult;
import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.request.*;
import com.xindongli.crm.system.dto.response.CustomerGradeRuleResponse;
import com.xindongli.crm.system.service.CustomerGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客户分级管理控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "客户分级管理", description = "客户分级管理相关接口")
@RestController
@RequestMapping("/api/v1/customer-grades")
@RequiredArgsConstructor
@Validated
public class CustomerGradeController {

    private final CustomerGradeService customerGradeService;

    /**
     * 创建分级规则
     */
    @Operation(summary = "创建分级规则")
    @PostMapping("/rules")
    public Result<Long> createGradeRule(@Valid @RequestBody CustomerGradeRuleCreateRequest request) {
        Long ruleId = customerGradeService.createGradeRule(request);
        return Result.success("创建成功", ruleId);
    }

    /**
     * 更新分级规则
     */
    @Operation(summary = "更新分级规则")
    @PutMapping("/rules/{ruleId}")
    public Result<Void> updateGradeRule(
            @Parameter(description = "规则ID") @PathVariable Long ruleId,
            @Valid @RequestBody CustomerGradeRuleUpdateRequest request) {
        request.setRuleId(ruleId);
        customerGradeService.updateGradeRule(request);
        return Result.success("更新成功");
    }

    /**
     * 删除分级规则
     */
    @Operation(summary = "删除分级规则")
    @DeleteMapping("/rules/{ruleId}")
    public Result<Void> deleteGradeRule(@Parameter(description = "规则ID") @PathVariable Long ruleId) {
        customerGradeService.deleteGradeRule(ruleId);
        return Result.success("删除成功");
    }

    /**
     * 批量删除分级规则
     */
    @Operation(summary = "批量删除分级规则")
    @DeleteMapping("/rules")
    public Result<Void> deleteGradeRules(@RequestBody List<Long> ruleIds) {
        customerGradeService.deleteGradeRules(ruleIds);
        return Result.success("删除成功");
    }

    /**
     * 获取分级规则详情
     */
    @Operation(summary = "获取分级规则详情")
    @GetMapping("/rules/{ruleId}")
    public Result<CustomerGradeRuleResponse> getGradeRuleById(
            @Parameter(description = "规则ID") @PathVariable Long ruleId) {
        CustomerGradeRuleResponse response = customerGradeService.getGradeRuleById(ruleId);
        return Result.success(response);
    }

    /**
     * 查询分级规则列表(分页)
     */
    @Operation(summary = "查询分级规则列表(分页)")
    @GetMapping("/rules/page")
    public Result<PageResult<CustomerGradeRuleResponse>> getGradeRulePage(
            @Valid CustomerGradeRuleQueryRequest request) {
        PageResult<CustomerGradeRuleResponse> pageResult = customerGradeService.getGradeRulePage(request);
        return Result.success(pageResult);
    }

    /**
     * 查询分级规则列表
     */
    @Operation(summary = "查询分级规则列表")
    @GetMapping("/rules")
    public Result<List<CustomerGradeRuleResponse>> getGradeRuleList(
            @Valid CustomerGradeRuleQueryRequest request) {
        List<CustomerGradeRuleResponse> list = customerGradeService.getGradeRuleList(request);
        return Result.success(list);
    }

    /**
     * 启用/禁用分级规则
     */
    @Operation(summary = "启用/禁用分级规则")
    @PutMapping("/rules/{ruleId}/status")
    public Result<Void> updateGradeRuleStatus(
            @Parameter(description = "规则ID") @PathVariable Long ruleId,
            @Parameter(description = "状态(0:禁用 1:启用)") @RequestParam Integer status) {
        customerGradeService.updateGradeRuleStatus(ruleId, status);
        return Result.success("操作成功");
    }

    /**
     * 计算客户等级
     */
    @Operation(summary = "计算客户等级")
    @PostMapping("/calculate")
    public Result<String> calculateCustomerGrade(@Valid @RequestBody CustomerGradeCalculateRequest request) {
        String grade = customerGradeService.calculateCustomerGrade(request);
        return Result.success("计算成功", grade);
    }

    /**
     * 批量计算客户等级
     */
    @Operation(summary = "批量计算客户等级")
    @PostMapping("/batch-calculate")
    public Result<Map<Long, String>> batchCalculateCustomerGrade(@RequestBody List<Long> customerIds) {
        Map<Long, String> result = customerGradeService.batchCalculateCustomerGrade(customerIds);
        return Result.success("计算成功", result);
    }

    /**
     * 手动调整客户等级
     */
    @Operation(summary = "手动调整客户等级")
    @PostMapping("/adjust")
    public Result<Void> adjustCustomerGrade(@Valid @RequestBody CustomerGradeAdjustRequest request) {
        customerGradeService.adjustCustomerGrade(request);
        return Result.success("调整成功");
    }

    /**
     * 根据客户ID自动计算并更新等级
     */
    @Operation(summary = "根据客户ID自动计算并更新等级")
    @PostMapping("/customers/{customerId}/auto-calculate")
    public Result<String> calculateAndUpdateCustomerGrade(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        String grade = customerGradeService.calculateAndUpdateCustomerGrade(customerId);
        return Result.success("计算成功", grade);
    }

}

package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.request.DeptBatchDeleteRequest;
import com.xindongli.crm.system.dto.request.DeptCreateRequest;
import com.xindongli.crm.system.dto.request.DeptQueryRequest;
import com.xindongli.crm.system.dto.request.DeptUpdateRequest;
import com.xindongli.crm.system.dto.response.DeptResponse;
import com.xindongli.crm.system.dto.response.DeptTreeResponse;
import com.xindongli.crm.system.entity.Dept;
import com.xindongli.crm.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "部门管理", description = "部门管理相关接口")
@RestController
@RequestMapping("/api/v1/depts")
@RequiredArgsConstructor
@Validated
public class DeptController {

    private final DeptService deptService;

    /**
     * 创建部门
     */
    @Operation(summary = "创建部门")
    @PostMapping
    public Result<Long> createDept(@Valid @RequestBody DeptCreateRequest request) {
        Long deptId = deptService.createDept(request);
        return Result.success("创建成功", deptId);
    }

    /**
     * 更新部门
     */
    @Operation(summary = "更新部门")
    @PutMapping("/{id}")
    public Result<Void> updateDept(
            @Parameter(description = "部门ID") @PathVariable Long id,
            @Valid @RequestBody DeptUpdateRequest request) {
        request.setId(id);
        deptService.updateDept(request);
        return Result.success("更新成功");
    }

    /**
     * 删除部门
     */
    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDept(@Parameter(description = "部门ID") @PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除部门
     */
    @Operation(summary = "批量删除部门")
    @DeleteMapping
    public Result<Void> deleteDepts(@Valid @RequestBody DeptBatchDeleteRequest request) {
        deptService.deleteDepts(request.getIds());
        return Result.success("删除成功");
    }

    /**
     * 获取部门详情
     */
    @Operation(summary = "获取部门详情")
    @GetMapping("/{id}")
    public Result<DeptResponse> getDeptById(@Parameter(description = "部门ID") @PathVariable Long id) {
        DeptResponse response = deptService.getDeptById(id);
        return Result.success(response);
    }

    /**
     * 查询部门列表
     */
    @Operation(summary = "查询部门列表")
    @GetMapping
    public Result<List<DeptResponse>> getDeptList(@Valid DeptQueryRequest request) {
        List<DeptResponse> list = deptService.getDeptList(request);
        return Result.success(list);
    }

    /**
     * 查询部门树形结构
     */
    @Operation(summary = "查询部门树形结构")
    @GetMapping("/tree")
    public Result<List<DeptTreeResponse>> getDeptTree() {
        List<DeptTreeResponse> tree = deptService.getDeptTree();
        return Result.success(tree);
    }

    /**
     * 查询所有部门列表
     */
    @Operation(summary = "查询所有部门列表")
    @GetMapping("/all")
    public Result<List<Dept>> getAllDepts() {
        List<Dept> depts = deptService.getAllDepts();
        return Result.success(depts);
    }

    /**
     * 更新部门状态
     */
    @Operation(summary = "更新部门状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateDeptStatus(
            @Parameter(description = "部门ID") @PathVariable Long id,
            @Parameter(description = "状态(0:正常 1:停用)") @RequestParam Integer status) {
        deptService.updateDeptStatus(id, status);
        return Result.success("更新成功");
    }

    /**
     * 查询部门子部门ID列表
     */
    @Operation(summary = "查询部门子部门ID列表")
    @GetMapping("/{id}/children-ids")
    public Result<List<Long>> getDeptChildrenIds(@Parameter(description = "部门ID") @PathVariable Long id) {
        List<Long> childrenIds = deptService.selectDeptChildrenIds(id);
        return Result.success(childrenIds);
    }

}

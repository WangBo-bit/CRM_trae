package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.request.PermissionCreateRequest;
import com.xindongli.crm.system.dto.request.PermissionQueryRequest;
import com.xindongli.crm.system.dto.request.PermissionUpdateRequest;
import com.xindongli.crm.system.dto.response.PermissionResponse;
import com.xindongli.crm.system.entity.Permission;
import com.xindongli.crm.system.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "权限管理", description = "权限管理相关接口")
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 创建权限
     */
    @Operation(summary = "创建权限")
    @PostMapping
    public Result<String> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
        String permissionId = permissionService.createPermission(request);
        return Result.success("创建成功", permissionId);
    }

    /**
     * 更新权限
     */
    @Operation(summary = "更新权限")
    @PutMapping("/{id}")
    public Result<Void> updatePermission(
            @Parameter(description = "权限ID") @PathVariable String id,
            @Valid @RequestBody PermissionUpdateRequest request) {
        request.setId(id);
        permissionService.updatePermission(request);
        return Result.success("更新成功");
    }

    /**
     * 删除权限
     */
    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(@Parameter(description = "权限ID") @PathVariable String id) {
        permissionService.deletePermission(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除权限
     */
    @Operation(summary = "批量删除权限")
    @DeleteMapping
    public Result<Void> deletePermissions(@RequestBody List<String> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            return Result.fail("权限ID列表不能为空");
        }
        permissionService.deletePermissions(ids);
        return Result.success("删除成功");
    }

    /**
     * 获取权限详情
     */
    @Operation(summary = "获取权限详情")
    @GetMapping("/{id}")
    public Result<PermissionResponse> getPermissionById(@Parameter(description = "权限ID") @PathVariable String id) {
        PermissionResponse response = permissionService.getPermissionById(id);
        return Result.success(response);
    }

    /**
     * 查询权限列表
     */
    @Operation(summary = "查询权限列表")
    @GetMapping
    public Result<List<PermissionResponse>> getPermissionList(PermissionQueryRequest request) {
        List<PermissionResponse> list = permissionService.getPermissionList(request);
        return Result.success(list);
    }

    /**
     * 查询权限树
     */
    @Operation(summary = "查询权限树")
    @GetMapping("/tree")
    public Result<List<PermissionResponse>> getPermissionTree(PermissionQueryRequest request) {
        List<PermissionResponse> tree = permissionService.getPermissionTree(request);
        return Result.success(tree);
    }

    /**
     * 查询所有权限列表
     */
    @Operation(summary = "查询所有权限列表")
    @GetMapping("/all")
    public Result<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    /**
     * 更新权限状态
     */
    @Operation(summary = "更新权限状态")
    @PutMapping("/{id}/status")
    public Result<Void> updatePermissionStatus(
            @Parameter(description = "权限ID") @PathVariable String id,
            @Parameter(description = "状态(0:禁用 1:启用)") @RequestParam Integer status) {
        permissionService.updatePermissionStatus(id, status);
        return Result.success("更新成功");
    }

}

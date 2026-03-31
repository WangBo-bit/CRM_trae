package com.xindongli.crm.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.request.RoleCreateRequest;
import com.xindongli.crm.system.dto.request.RoleQueryRequest;
import com.xindongli.crm.system.dto.request.RoleUpdateRequest;
import com.xindongli.crm.system.dto.response.RoleResponse;
import com.xindongli.crm.system.entity.Role;
import com.xindongli.crm.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 创建角色
     */
    @Operation(summary = "创建角色")
    @PostMapping
    public Result<String> createRole(@Valid @RequestBody RoleCreateRequest request) {
        String roleId = roleService.createRole(request);
        return Result.success("创建成功", roleId);
    }

    /**
     * 更新角色
     */
    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public Result<Void> updateRole(
            @Parameter(description = "角色ID") @PathVariable String id,
            @Valid @RequestBody RoleUpdateRequest request) {
        request.setId(id);
        roleService.updateRole(request);
        return Result.success("更新成功");
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@Parameter(description = "角色ID") @PathVariable String id) {
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除角色
     */
    @Operation(summary = "批量删除角色")
    @DeleteMapping
    public Result<Void> deleteRoles(@RequestBody List<String> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            return Result.fail("角色ID列表不能为空");
        }
        roleService.deleteRoles(ids);
        return Result.success("删除成功");
    }

    /**
     * 获取角色详情
     */
    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    public Result<RoleResponse> getRoleById(@Parameter(description = "角色ID") @PathVariable String id) {
        RoleResponse response = roleService.getRoleById(id);
        return Result.success(response);
    }

    /**
     * 分页查询角色列表
     */
    @Operation(summary = "分页查询角色列表")
    @GetMapping
    public Result<Page<RoleResponse>> getRolePage(RoleQueryRequest request) {
        Page<RoleResponse> page = roleService.getRolePage(request);
        return Result.success(page);
    }

    /**
     * 查询所有角色列表
     */
    @Operation(summary = "查询所有角色列表")
    @GetMapping("/all")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 更新角色状态
     */
    @Operation(summary = "更新角色状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateRoleStatus(
            @Parameter(description = "角色ID") @PathVariable String id,
            @Parameter(description = "状态(0:禁用 1:启用)") @RequestParam Integer status) {
        roleService.updateRoleStatus(id, status);
        return Result.success("更新成功");
    }

}

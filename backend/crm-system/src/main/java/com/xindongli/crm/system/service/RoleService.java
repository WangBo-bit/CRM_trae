package com.xindongli.crm.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.system.dto.request.RoleCreateRequest;
import com.xindongli.crm.system.dto.request.RoleQueryRequest;
import com.xindongli.crm.system.dto.request.RoleUpdateRequest;
import com.xindongli.crm.system.dto.response.RoleResponse;
import com.xindongli.crm.system.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author 芯动力科技
 */
public interface RoleService {

    /**
     * 创建角色
     * 
     * @param request 角色创建请求
     * @return 角色ID
     */
    String createRole(RoleCreateRequest request);

    /**
     * 更新角色
     * 
     * @param request 角色更新请求
     */
    void updateRole(RoleUpdateRequest request);

    /**
     * 删除角色
     * 
     * @param id 角色ID
     */
    void deleteRole(String id);

    /**
     * 批量删除角色
     * 
     * @param ids 角色ID列表
     */
    void deleteRoles(List<String> ids);

    /**
     * 获取角色详情
     * 
     * @param id 角色ID
     * @return 角色详情
     */
    RoleResponse getRoleById(String id);

    /**
     * 分页查询角色列表
     * 
     * @param request 查询请求
     * @return 角色分页列表
     */
    Page<RoleResponse> getRolePage(RoleQueryRequest request);

    /**
     * 查询所有角色列表
     * 
     * @return 角色列表
     */
    List<Role> getAllRoles();

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(String userId);

    /**
     * 更新角色状态
     * 
     * @param id 角色ID
     * @param status 状态
     */
    void updateRoleStatus(String id, Integer status);

}

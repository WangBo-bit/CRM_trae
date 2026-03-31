package com.xindongli.crm.system.service;

import com.xindongli.crm.system.dto.request.PermissionCreateRequest;
import com.xindongli.crm.system.dto.request.PermissionQueryRequest;
import com.xindongli.crm.system.dto.request.PermissionUpdateRequest;
import com.xindongli.crm.system.dto.response.PermissionResponse;
import com.xindongli.crm.system.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 * 
 * @author 芯动力科技
 */
public interface PermissionService {

    /**
     * 创建权限
     * 
     * @param request 权限创建请求
     * @return 权限ID
     */
    String createPermission(PermissionCreateRequest request);

    /**
     * 更新权限
     * 
     * @param request 权限更新请求
     */
    void updatePermission(PermissionUpdateRequest request);

    /**
     * 删除权限
     * 
     * @param id 权限ID
     */
    void deletePermission(String id);

    /**
     * 批量删除权限
     * 
     * @param ids 权限ID列表
     */
    void deletePermissions(List<String> ids);

    /**
     * 获取权限详情
     * 
     * @param id 权限ID
     * @return 权限详情
     */
    PermissionResponse getPermissionById(String id);

    /**
     * 查询权限列表
     * 
     * @param request 查询请求
     * @return 权限列表
     */
    List<PermissionResponse> getPermissionList(PermissionQueryRequest request);

    /**
     * 查询权限树
     * 
     * @param request 查询请求
     * @return 权限树
     */
    List<PermissionResponse> getPermissionTree(PermissionQueryRequest request);

    /**
     * 查询所有权限列表
     * 
     * @return 权限列表
     */
    List<Permission> getAllPermissions();

    /**
     * 根据用户ID查询权限列表
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByUserId(String userId);

    /**
     * 根据角色ID查询权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByRoleId(String roleId);

    /**
     * 更新权限状态
     * 
     * @param id 权限ID
     * @param status 状态
     */
    void updatePermissionStatus(String id, Integer status);

}

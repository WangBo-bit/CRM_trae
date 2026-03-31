package com.xindongli.crm.system.service;

import java.util.List;

/**
 * 角色权限关联服务接口
 * 
 * @author 芯动力科技
 */
public interface RolePermissionService {

    /**
     * 分配权限给角色
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissionsToRole(String roleId, List<String> permissionIds);

    /**
     * 分配角色给权限
     * 
     * @param permissionId 权限ID
     * @param roleIds 角色ID列表
     */
    void assignRolesToPermission(String permissionId, List<String> roleIds);

    /**
     * 删除角色的权限
     * 
     * @param roleId 角色ID
     */
    void removeRolePermissions(String roleId);

    /**
     * 删除权限的角色
     * 
     * @param permissionId 权限ID
     */
    void removePermissionRoles(String permissionId);

    /**
     * 查询角色的权限ID列表
     * 
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<String> getPermissionIdsByRoleId(String roleId);

    /**
     * 查询权限的角色ID列表
     * 
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    List<String> getRoleIdsByPermissionId(String permissionId);

}

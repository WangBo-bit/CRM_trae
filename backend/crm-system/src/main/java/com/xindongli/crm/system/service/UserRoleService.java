package com.xindongli.crm.system.service;

import com.xindongli.crm.system.entity.UserRole;

import java.util.List;

/**
 * 用户角色关联服务接口
 * 
 * @author 芯动力科技
 */
public interface UserRoleService {

    /**
     * 分配角色给用户
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void assignRolesToUser(String userId, List<String> roleIds);

    /**
     * 分配用户给角色
     * 
     * @param roleId 角色ID
     * @param userIds 用户ID列表
     */
    void assignUsersToRole(String roleId, List<String> userIds);

    /**
     * 删除用户的角色
     * 
     * @param userId 用户ID
     */
    void removeUserRoles(String userId);

    /**
     * 删除角色的用户
     * 
     * @param roleId 角色ID
     */
    void removeRoleUsers(String roleId);

    /**
     * 查询用户的角色ID列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<String> getRoleIdsByUserId(String userId);

    /**
     * 查询角色的用户ID列表
     * 
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<String> getUserIdsByRoleId(String roleId);

}

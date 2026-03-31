package com.xindongli.crm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xindongli.crm.system.entity.RolePermission;
import com.xindongli.crm.system.mapper.RolePermissionMapper;
import com.xindongli.crm.system.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色权限关联服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissionsToRole(String roleId, List<String> permissionIds) {
        // 删除角色原有的权限
        rolePermissionMapper.deleteByRoleId(roleId);

        // 批量插入新的角色权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<RolePermission> rolePermissions = permissionIds.stream()
                    .map(permissionId -> {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        rolePermission.setCreatedTime(LocalDateTime.now());
                        return rolePermission;
                    })
                    .collect(Collectors.toList());
            rolePermissionMapper.batchInsert(rolePermissions);
        }
        
        log.info("分配权限给角色成功, 角色ID: {}, 权限数量: {}", roleId, permissionIds != null ? permissionIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToPermission(String permissionId, List<String> roleIds) {
        // 删除权限原有的角色
        rolePermissionMapper.deleteByPermissionId(permissionId);

        // 批量插入新的角色权限关联
        if (roleIds != null && !roleIds.isEmpty()) {
            List<RolePermission> rolePermissions = roleIds.stream()
                    .map(roleId -> {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        rolePermission.setCreatedTime(LocalDateTime.now());
                        return rolePermission;
                    })
                    .collect(Collectors.toList());
            rolePermissionMapper.batchInsert(rolePermissions);
        }
        
        log.info("分配角色给权限成功, 权限ID: {}, 角色数量: {}", permissionId, roleIds != null ? roleIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRolePermissions(String roleId) {
        rolePermissionMapper.deleteByRoleId(roleId);
        log.info("删除角色的权限成功, 角色ID: {}", roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePermissionRoles(String permissionId) {
        rolePermissionMapper.deleteByPermissionId(permissionId);
        log.info("删除权限的角色成功, 权限ID: {}", permissionId);
    }

    @Override
    public List<String> getPermissionIdsByRoleId(String roleId) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(wrapper);
        return rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleIdsByPermissionId(String permissionId) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getPermissionId, permissionId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(wrapper);
        return rolePermissions.stream()
                .map(RolePermission::getRoleId)
                .collect(Collectors.toList());
    }

}

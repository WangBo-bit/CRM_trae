package com.xindongli.crm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xindongli.crm.system.entity.UserRole;
import com.xindongli.crm.system.mapper.UserRoleMapper;
import com.xindongli.crm.system.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(String userId, List<String> roleIds) {
        // 删除用户原有的角色
        userRoleMapper.deleteByUserId(userId);

        // 批量插入新的用户角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRole.setCreatedTime(LocalDateTime.now());
                        return userRole;
                    })
                    .collect(Collectors.toList());
            userRoleMapper.batchInsert(userRoles);
        }
        
        log.info("分配角色给用户成功, 用户ID: {}, 角色数量: {}", userId, roleIds != null ? roleIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUsersToRole(String roleId, List<String> userIds) {
        // 删除角色原有的用户
        userRoleMapper.deleteByRoleId(roleId);

        // 批量插入新的用户角色关联
        if (userIds != null && !userIds.isEmpty()) {
            List<UserRole> userRoles = userIds.stream()
                    .map(userId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRole.setCreatedTime(LocalDateTime.now());
                        return userRole;
                    })
                    .collect(Collectors.toList());
            userRoleMapper.batchInsert(userRoles);
        }
        
        log.info("分配用户给角色成功, 角色ID: {}, 用户数量: {}", roleId, userIds != null ? userIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserRoles(String userId) {
        userRoleMapper.deleteByUserId(userId);
        log.info("删除用户的角色成功, 用户ID: {}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRoleUsers(String roleId) {
        userRoleMapper.deleteByRoleId(roleId);
        log.info("删除角色的用户成功, 角色ID: {}", roleId);
    }

    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserIdsByRoleId(String roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        return userRoles.stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

}

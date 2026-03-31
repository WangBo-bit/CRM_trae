package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.system.dto.request.PermissionCreateRequest;
import com.xindongli.crm.system.dto.request.PermissionQueryRequest;
import com.xindongli.crm.system.dto.request.PermissionUpdateRequest;
import com.xindongli.crm.system.dto.response.PermissionResponse;
import com.xindongli.crm.system.entity.Permission;
import com.xindongli.crm.system.entity.RolePermission;
import com.xindongli.crm.system.mapper.PermissionMapper;
import com.xindongli.crm.system.mapper.RolePermissionMapper;
import com.xindongli.crm.system.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPermission(PermissionCreateRequest request) {
        // 检查权限标识是否已存在
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPermissionCode, request.getPermissionCode());
        if (permissionMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("权限标识已存在");
        }

        // 创建权限
        Permission permission = new Permission();
        BeanUtils.copyProperties(request, permission);
        if (StrUtil.isBlank(permission.getParentId())) {
            permission.setParentId("0");
        }
        if (permission.getSort() == null) {
            permission.setSort(0);
        }
        if (permission.getVisible() == null) {
            permission.setVisible(1);
        }
        if (permission.getStatus() == null) {
            permission.setStatus(1);
        }
        permissionMapper.insert(permission);
        
        log.info("创建权限成功, 权限ID: {}, 权限标识: {}", permission.getId(), permission.getPermissionCode());
        return permission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(PermissionUpdateRequest request) {
        // 检查权限是否存在
        Permission permission = permissionMapper.selectById(request.getId());
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        // 检查权限标识是否重复
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPermissionCode, request.getPermissionCode())
               .ne(Permission::getId, request.getId());
        if (permissionMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("权限标识已存在");
        }

        // 更新权限
        BeanUtils.copyProperties(request, permission);
        if (StrUtil.isBlank(permission.getParentId())) {
            permission.setParentId("0");
        }
        permissionMapper.updateById(permission);
        
        log.info("更新权限成功, 权限ID: {}", permission.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(String id) {
        // 检查是否有子权限
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getParentId, id);
        if (permissionMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("存在子权限,无法删除");
        }

        // 检查权限是否已分配给角色
        LambdaQueryWrapper<RolePermission> rpWrapper = new LambdaQueryWrapper<>();
        rpWrapper.eq(RolePermission::getPermissionId, id);
        if (rolePermissionMapper.selectCount(rpWrapper) > 0) {
            throw new BusinessException("权限已分配给角色,无法删除");
        }

        // 删除权限
        permissionMapper.deleteById(id);
        
        log.info("删除权限成功, 权限ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissions(List<String> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("权限ID列表不能为空");
        }
        
        // 过滤空值
        List<String> validIds = ids.stream()
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            throw new BusinessException("权限ID列表不能为空");
        }
        
        // 检查是否有子权限
        LambdaQueryWrapper<Permission> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.in(Permission::getParentId, validIds);
        long childCount = permissionMapper.selectCount(childWrapper);
        if (childCount > 0) {
            throw new BusinessException("存在子权限,无法删除");
        }
        
        // 检查权限是否已分配给角色
        LambdaQueryWrapper<RolePermission> rpWrapper = new LambdaQueryWrapper<>();
        rpWrapper.in(RolePermission::getPermissionId, validIds);
        long assignedCount = rolePermissionMapper.selectCount(rpWrapper);
        if (assignedCount > 0) {
            throw new BusinessException("部分权限已分配给角色,无法删除");
        }
        
        // 批量删除权限
        int deletedCount = permissionMapper.deleteBatchIds(validIds);
        log.info("批量删除权限成功, 删除数量: {}, 权限ID: {}", deletedCount, validIds);
    }

    @Override
    public PermissionResponse getPermissionById(String id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        PermissionResponse response = new PermissionResponse();
        BeanUtils.copyProperties(permission, response);
        return response;
    }

    @Override
    public List<PermissionResponse> getPermissionList(PermissionQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(request.getParentId()), Permission::getParentId, request.getParentId())
               .like(StrUtil.isNotBlank(request.getPermissionName()), Permission::getPermissionName, request.getPermissionName())
               .like(StrUtil.isNotBlank(request.getPermissionCode()), Permission::getPermissionCode, request.getPermissionCode())
               .eq(request.getPermissionType() != null, Permission::getPermissionType, request.getPermissionType())
               .eq(request.getStatus() != null, Permission::getStatus, request.getStatus())
               .orderByAsc(Permission::getSort)
               .orderByDesc(Permission::getCreatedTime);

        List<Permission> permissions = permissionMapper.selectList(wrapper);
        return permissions.stream()
                .map(permission -> {
                    PermissionResponse response = new PermissionResponse();
                    BeanUtils.copyProperties(permission, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionResponse> getPermissionTree(PermissionQueryRequest request) {
        // 查询所有权限
        List<Permission> allPermissions = permissionMapper.selectList(
                new LambdaQueryWrapper<Permission>()
                        .eq(request.getStatus() != null, Permission::getStatus, request.getStatus())
                        .orderByAsc(Permission::getSort)
        );

        // 转换为响应对象
        List<PermissionResponse> permissionResponses = allPermissions.stream()
                .map(permission -> {
                    PermissionResponse response = new PermissionResponse();
                    BeanUtils.copyProperties(permission, response);
                    return response;
                })
                .collect(Collectors.toList());

        // 构建树形结构
        Map<String, List<PermissionResponse>> parentMap = permissionResponses.stream()
                .collect(Collectors.groupingBy(PermissionResponse::getParentId));

        permissionResponses.forEach(permission -> {
            permission.setChildren(parentMap.get(permission.getId()));
        });

        // 返回顶级权限
        return permissionResponses.stream()
                .filter(permission -> "0".equals(permission.getParentId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getAllPermissions() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getStatus, 1)
               .orderByAsc(Permission::getSort);
        return permissionMapper.selectList(wrapper);
    }

    @Override
    public List<Permission> getPermissionsByUserId(String userId) {
        return permissionMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(String roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermissionStatus(String id, Integer status) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        permission.setStatus(status);
        permissionMapper.updateById(permission);
        
        log.info("更新权限状态成功, 权限ID: {}, 状态: {}", id, status);
    }

}

package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.system.dto.request.RoleCreateRequest;
import com.xindongli.crm.system.dto.request.RoleQueryRequest;
import com.xindongli.crm.system.dto.request.RoleUpdateRequest;
import com.xindongli.crm.system.dto.response.RoleResponse;
import com.xindongli.crm.system.entity.Role;
import com.xindongli.crm.system.entity.UserRole;
import com.xindongli.crm.system.mapper.RoleMapper;
import com.xindongli.crm.system.mapper.UserRoleMapper;
import com.xindongli.crm.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRole(RoleCreateRequest request) {
        // 检查角色编码是否已存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, request.getRoleCode());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 创建角色
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        if (role.getSort() == null) {
            role.setSort(0);
        }
        roleMapper.insert(role);
        
        log.info("创建角色成功, 角色ID: {}, 角色编码: {}", role.getId(), role.getRoleCode());
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateRequest request) {
        // 检查角色是否存在
        Role role = roleMapper.selectById(request.getId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查角色编码是否重复
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, request.getRoleCode())
               .ne(Role::getId, request.getId());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 更新角色
        BeanUtils.copyProperties(request, role);
        roleMapper.updateById(role);
        
        log.info("更新角色成功, 角色ID: {}", role.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String id) {
        // 检查角色是否已分配给用户
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, id);
        if (userRoleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色已分配给用户,无法删除");
        }

        // 删除角色
        roleMapper.deleteById(id);
        
        log.info("删除角色成功, 角色ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(List<String> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("角色ID列表不能为空");
        }
        
        // 过滤空值
        List<String> validIds = ids.stream()
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            throw new BusinessException("角色ID列表不能为空");
        }
        
        // 检查角色是否已分配给用户
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRole::getRoleId, validIds);
        long assignedCount = userRoleMapper.selectCount(wrapper);
        if (assignedCount > 0) {
            throw new BusinessException("部分角色已分配给用户,无法删除");
        }
        
        // 批量删除角色
        int deletedCount = roleMapper.deleteBatchIds(validIds);
        log.info("批量删除角色成功, 删除数量: {}, 角色ID: {}", deletedCount, validIds);
    }

    @Override
    public RoleResponse getRoleById(String id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(role, response);
        return response;
    }

    @Override
    public Page<RoleResponse> getRolePage(RoleQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(request.getRoleName()), Role::getRoleName, request.getRoleName())
               .like(StrUtil.isNotBlank(request.getRoleCode()), Role::getRoleCode, request.getRoleCode())
               .eq(request.getRoleType() != null, Role::getRoleType, request.getRoleType())
               .eq(request.getStatus() != null, Role::getStatus, request.getStatus())
               .orderByAsc(Role::getSort)
               .orderByDesc(Role::getCreatedTime);

        // 分页查询
        Page<Role> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Role> rolePage = roleMapper.selectPage(page, wrapper);

        // 转换为响应对象
        Page<RoleResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(rolePage, responsePage, "records");
        List<RoleResponse> records = rolePage.getRecords().stream()
                .map(role -> {
                    RoleResponse response = new RoleResponse();
                    BeanUtils.copyProperties(role, response);
                    return response;
                })
                .collect(Collectors.toList());
        responsePage.setRecords(records);

        return responsePage;
    }

    @Override
    public List<Role> getAllRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, 1)
               .orderByAsc(Role::getSort);
        return roleMapper.selectList(wrapper);
    }

    @Override
    public List<Role> getRolesByUserId(String userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleStatus(String id, Integer status) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        role.setStatus(status);
        roleMapper.updateById(role);
        
        log.info("更新角色状态成功, 角色ID: {}, 状态: {}", id, status);
    }

}

package com.xindongli.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.system.dto.request.DeptCreateRequest;
import com.xindongli.crm.system.dto.request.DeptQueryRequest;
import com.xindongli.crm.system.dto.request.DeptUpdateRequest;
import com.xindongli.crm.system.dto.response.DeptResponse;
import com.xindongli.crm.system.dto.response.DeptTreeResponse;
import com.xindongli.crm.system.entity.Dept;
import com.xindongli.crm.system.mapper.DeptMapper;
import com.xindongli.crm.system.service.DeptService;
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
 * 部门服务实现类
 * 
 * @author 芯动力科技
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDept(DeptCreateRequest request) {
        // 检查部门名称是否已存在
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getDeptName, request.getDeptName());
        if (request.getParentId() != null) {
            wrapper.eq(Dept::getParentId, request.getParentId());
        }
        if (deptMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("部门名称已存在");
        }

        // 创建部门
        Dept dept = new Dept();
        BeanUtils.copyProperties(request, dept);
        
        // 设置父部门ID默认值
        if (dept.getParentId() == null) {
            dept.setParentId(0L);
        }
        
        // 设置默认状态
        if (dept.getStatus() == null) {
            dept.setStatus(0);
        }
        
        // 设置默认排序
        if (dept.getOrderNum() == null) {
            dept.setOrderNum(0);
        }
        
        // 设置祖级列表（ancestors）
        dept.setAncestors(buildAncestors(dept.getParentId()));
        
        deptMapper.insert(dept);
        
        log.info("创建部门成功, 部门ID: {}, 部门名称: {}", dept.getId(), dept.getDeptName());
        return dept.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptUpdateRequest request) {
        // 检查部门是否存在
        Dept dept = deptMapper.selectById(request.getId());
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 检查部门名称是否重复
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getDeptName, request.getDeptName())
               .ne(Dept::getId, request.getId());
        if (request.getParentId() != null) {
            wrapper.eq(Dept::getParentId, request.getParentId());
        }
        if (deptMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("部门名称已存在");
        }

        // 检查父部门是否有效（不能将自己设置为父部门）
        if (request.getParentId() != null && request.getParentId().equals(request.getId())) {
            throw new BusinessException("父部门不能是自己");
        }

        // 保存旧的父部门ID，用于判断是否需要更新子部门的 ancestors
        Long oldParentId = dept.getParentId();
        Long newParentId = request.getParentId() != null ? request.getParentId() : oldParentId;
        boolean parentChanged = !newParentId.equals(oldParentId);

        // 检查父部门是否存在
        if (newParentId != null && newParentId != 0) {
            Dept parentDept = deptMapper.selectById(newParentId);
            if (parentDept == null) {
                throw new BusinessException("父部门不存在");
            }
            // 检查父部门是否是自己的子部门
            if (isChildDept(request.getId(), newParentId)) {
                throw new BusinessException("父部门不能是自己的子部门");
            }
        }

        // 更新部门
        BeanUtils.copyProperties(request, dept);
        
        // 更新祖级列表
        if (request.getParentId() != null) {
            dept.setAncestors(buildAncestors(request.getParentId()));
        }
        
        deptMapper.updateById(dept);
        
        // 如果父部门发生变化，需要更新所有子部门的 ancestors
        if (parentChanged) {
            updateChildrenAncestors(dept.getId(), dept.getAncestors());
        }
        
        log.info("更新部门成功, 部门ID: {}", dept.getId());
    }

    /**
     * 更新子部门的祖级列表
     * 
     * @param deptId 部门ID
     * @param newAncestors 新的祖级列表
     */
    private void updateChildrenAncestors(Long deptId, String newAncestors) {
        List<Dept> children = deptMapper.selectChildrenByParentId(deptId);
        for (Dept child : children) {
            // 更新子部门的 ancestors
            child.setAncestors(newAncestors + "," + deptId);
            deptMapper.updateById(child);
            
            // 递归更新子部门的子部门
            updateChildrenAncestors(child.getId(), child.getAncestors());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long id) {
        // 检查部门是否存在
        Dept dept = deptMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 检查是否存在子部门
        int childCount = deptMapper.selectChildrenCountByParentId(id);
        if (childCount > 0) {
            throw new BusinessException("存在子部门,无法删除");
        }

        // 检查是否存在关联用户
        int userCount = deptMapper.selectUserCountByDeptId(id);
        if (userCount > 0) {
            throw new BusinessException("部门存在关联用户,无法删除");
        }

        // 删除部门
        deptMapper.deleteById(id);
        
        log.info("删除部门成功, 部门ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepts(List<Long> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("部门ID列表不能为空");
        }
        
        // 过滤空值
        List<Long> validIds = ids.stream()
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            throw new BusinessException("部门ID列表不能为空");
        }
        
        // 检查每个部门
        for (Long id : validIds) {
            // 检查是否存在子部门
            int childCount = deptMapper.selectChildrenCountByParentId(id);
            if (childCount > 0) {
                throw new BusinessException("部分部门存在子部门,无法删除");
            }
            
            // 检查是否存在关联用户
            int userCount = deptMapper.selectUserCountByDeptId(id);
            if (userCount > 0) {
                throw new BusinessException("部分部门存在关联用户,无法删除");
            }
        }
        
        // 批量删除部门
        int deletedCount = deptMapper.deleteBatchIds(validIds);
        log.info("批量删除部门成功, 删除数量: {}, 部门ID: {}", deletedCount, validIds);
    }

    @Override
    public DeptResponse getDeptById(Long id) {
        Dept dept = deptMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        DeptResponse response = new DeptResponse();
        BeanUtils.copyProperties(dept, response);
        return response;
    }

    @Override
    public List<DeptResponse> getDeptList(DeptQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        
        // 安全处理模糊查询：转义特殊字符
        if (StrUtil.isNotBlank(request.getDeptName())) {
            String escapedDeptName = escapeLikeSpecialChars(request.getDeptName());
            wrapper.like(Dept::getDeptName, escapedDeptName);
        }
        
        wrapper.eq(request.getParentId() != null, Dept::getParentId, request.getParentId())
               .eq(request.getStatus() != null, Dept::getStatus, request.getStatus())
               .orderByAsc(Dept::getOrderNum);

        List<Dept> deptList = deptMapper.selectList(wrapper);

        // 转换为响应对象
        return deptList.stream()
                .map(dept -> {
                    DeptResponse response = new DeptResponse();
                    BeanUtils.copyProperties(dept, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 转义 LIKE 查询中的特殊字符
     * 
     * @param keyword 关键字
     * @return 转义后的关键字
     */
    private String escapeLikeSpecialChars(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return keyword;
        }
        // 转义 % 和 _ 特殊字符
        return keyword.replace("\\", "\\\\")
                      .replace("%", "\\%")
                      .replace("_", "\\_");
    }

    @Override
    public List<DeptTreeResponse> getDeptTree() {
        // 查询所有部门
        List<Dept> allDepts = deptMapper.selectDeptTree();
        
        // 转换为树形结构响应对象
        List<DeptTreeResponse> deptTreeList = allDepts.stream()
                .map(dept -> {
                    DeptTreeResponse response = new DeptTreeResponse();
                    BeanUtils.copyProperties(dept, response);
                    return response;
                })
                .collect(Collectors.toList());
        
        // 构建树形结构
        return buildDeptTree(deptTreeList, 0L);
    }

    @Override
    public List<Dept> getAllDepts() {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getStatus, 0)
               .orderByAsc(Dept::getOrderNum);
        return deptMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeptStatus(Long id, Integer status) {
        Dept dept = deptMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 如果是停用部门，检查是否存在正常状态的子部门
        if (status == 1) {
            LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Dept::getParentId, id)
                   .eq(Dept::getStatus, 0)
                   .eq(Dept::getDeleted, 0);
            long normalChildCount = deptMapper.selectCount(wrapper);
            if (normalChildCount > 0) {
                throw new BusinessException("存在正常状态的子部门,无法停用");
            }
        }

        dept.setStatus(status);
        deptMapper.updateById(dept);
        
        log.info("更新部门状态成功, 部门ID: {}, 状态: {}", id, status);
    }

    @Override
    public List<Long> selectDeptChildrenIds(Long deptId) {
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        
        // 递归查询子部门ID
        findChildrenDeptIds(deptId, deptIds);
        
        return deptIds;
    }

    /**
     * 构建祖级列表
     * 
     * @param parentId 父部门ID
     * @return 祖级列表
     */
    private String buildAncestors(Long parentId) {
        if (parentId == null || parentId == 0) {
            return "0";
        }
        
        Dept parentDept = deptMapper.selectById(parentId);
        if (parentDept == null) {
            return "0";
        }
        
        // 祖级列表 = 父部门的祖级列表 + 父部门ID
        // 防止 NPE：如果 ancestors 为空，使用默认值 "0"
        String parentAncestors = StrUtil.isBlank(parentDept.getAncestors()) ? "0" : parentDept.getAncestors();
        return parentAncestors + "," + parentId;
    }

    /**
     * 构建部门树形结构（优化版本，时间复杂度 O(n)）
     * 
     * @param deptList 部门列表
     * @param parentId 父部门ID
     * @return 部门树形结构
     */
    private List<DeptTreeResponse> buildDeptTree(List<DeptTreeResponse> deptList, Long parentId) {
        // 使用 Map 优化查找性能，时间复杂度从 O(n^2) 降低到 O(n)
        Map<Long, List<DeptTreeResponse>> parentMap = deptList.stream()
                .collect(Collectors.groupingBy(DeptTreeResponse::getParentId));
        
        return buildTreeRecursive(parentMap, parentId);
    }

    /**
     * 递归构建树形结构
     * 
     * @param parentMap 父部门ID到子部门列表的映射
     * @param parentId 父部门ID
     * @return 子部门树形结构
     */
    private List<DeptTreeResponse> buildTreeRecursive(Map<Long, List<DeptTreeResponse>> parentMap, Long parentId) {
        List<DeptTreeResponse> children = parentMap.get(parentId);
        if (children == null || children.isEmpty()) {
            return new ArrayList<>();
        }
        
        for (DeptTreeResponse child : children) {
            child.setChildren(buildTreeRecursive(parentMap, child.getId()));
        }
        
        return children;
    }

    /**
     * 检查是否是子部门（优化版本，避免 N+1 查询问题）
     * 
     * @param parentId 父部门ID
     * @param childId 子部门ID
     * @return 是否是子部门
     */
    private boolean isChildDept(Long parentId, Long childId) {
        // 使用 ancestors 字段判断，避免递归查询数据库
        Dept childDept = deptMapper.selectById(childId);
        if (childDept == null) {
            return false;
        }
        
        String ancestors = childDept.getAncestors();
        if (StrUtil.isBlank(ancestors)) {
            return false;
        }
        
        // 检查 parentId 是否在 ancestors 中
        String[] ancestorIds = ancestors.split(",");
        for (String ancestorId : ancestorIds) {
            if (parentId.toString().equals(ancestorId.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 递归查询子部门ID（优化版本，避免 N+1 查询问题）
     * 
     * @param parentId 父部门ID
     * @param deptIds 部门ID列表
     */
    private void findChildrenDeptIds(Long parentId, List<Long> deptIds) {
        // 使用 ancestors 字段查询所有子部门，避免递归查询数据库
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(Dept::getAncestors, parentId.toString())
               .or()
               .eq(Dept::getParentId, parentId);
        
        List<Dept> children = deptMapper.selectList(wrapper);
        for (Dept child : children) {
            if (!deptIds.contains(child.getId())) {
                deptIds.add(child.getId());
            }
        }
    }

}

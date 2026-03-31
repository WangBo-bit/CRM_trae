package com.xindongli.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xindongli.crm.system.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 * 
 * @author 芯动力科技
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 查询部门树形结构
     * 
     * @return 部门列表
     */
    List<Dept> selectDeptTree();

    /**
     * 根据父部门ID查询子部门数量
     * 
     * @param parentId 父部门ID
     * @return 子部门数量
     */
    int selectChildrenCountByParentId(@Param("parentId") Long parentId);

    /**
     * 根据部门ID查询是否存在用户
     * 
     * @param deptId 部门ID
     * @return 用户数量
     */
    int selectUserCountByDeptId(@Param("deptId") Long deptId);

    /**
     * 查询子部门列表
     * 
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<Dept> selectChildrenByParentId(@Param("parentId") Long parentId);

}

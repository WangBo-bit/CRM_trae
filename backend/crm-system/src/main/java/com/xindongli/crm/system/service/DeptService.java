package com.xindongli.crm.system.service;

import com.xindongli.crm.system.dto.request.DeptCreateRequest;
import com.xindongli.crm.system.dto.request.DeptQueryRequest;
import com.xindongli.crm.system.dto.request.DeptUpdateRequest;
import com.xindongli.crm.system.dto.response.DeptResponse;
import com.xindongli.crm.system.dto.response.DeptTreeResponse;
import com.xindongli.crm.system.entity.Dept;

import java.util.List;

/**
 * 部门服务接口
 * 
 * @author 芯动力科技
 */
public interface DeptService {

    /**
     * 创建部门
     * 
     * @param request 部门创建请求
     * @return 部门ID
     */
    Long createDept(DeptCreateRequest request);

    /**
     * 更新部门
     * 
     * @param request 部门更新请求
     */
    void updateDept(DeptUpdateRequest request);

    /**
     * 删除部门
     * 
     * @param id 部门ID
     */
    void deleteDept(Long id);

    /**
     * 批量删除部门
     * 
     * @param ids 部门ID列表
     */
    void deleteDepts(List<Long> ids);

    /**
     * 获取部门详情
     * 
     * @param id 部门ID
     * @return 部门详情
     */
    DeptResponse getDeptById(Long id);

    /**
     * 查询部门列表
     * 
     * @param request 查询请求
     * @return 部门列表
     */
    List<DeptResponse> getDeptList(DeptQueryRequest request);

    /**
     * 查询部门树形结构
     * 
     * @return 部门树形结构
     */
    List<DeptTreeResponse> getDeptTree();

    /**
     * 查询所有部门列表
     * 
     * @return 部门列表
     */
    List<Dept> getAllDepts();

    /**
     * 更新部门状态
     * 
     * @param id 部门ID
     * @param status 状态
     */
    void updateDeptStatus(Long id, Integer status);

    /**
     * 根据部门ID查询子部门ID列表（包含当前部门）
     * 
     * @param deptId 部门ID
     * @return 子部门ID列表
     */
    List<Long> selectDeptChildrenIds(Long deptId);

}

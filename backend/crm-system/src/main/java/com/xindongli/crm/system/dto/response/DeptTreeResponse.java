package com.xindongli.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 部门树形结构响应
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "部门树形结构响应")
public class DeptTreeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long id;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID")
    private Long parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /**
     * 负责人
     */
    @Schema(description = "负责人")
    private String leader;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 部门状态(0:正常 1:停用)
     */
    @Schema(description = "部门状态(0:正常 1:停用)")
    private Integer status;

    /**
     * 子部门列表
     */
    @Schema(description = "子部门列表")
    private List<DeptTreeResponse> children;

}

package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门查询请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "部门查询请求")
public class DeptQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID")
    private Long parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @Size(max = 50, message = "部门名称长度不能超过50个字符")
    private String deptName;

    /**
     * 部门状态(0:正常 1:停用)
     */
    @Schema(description = "部门状态(0:正常 1:停用)")
    private Integer status;

}

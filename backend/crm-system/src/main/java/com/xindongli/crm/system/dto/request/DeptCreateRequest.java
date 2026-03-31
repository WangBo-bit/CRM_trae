package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门创建请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "部门创建请求")
public class DeptCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID")
    private Long parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称", required = true)
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 1, max = 50, message = "部门名称长度必须在1-50个字符之间")
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
    @Size(max = 30, message = "负责人名称长度不能超过30个字符")
    private String leader;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 部门状态(0:正常 1:停用)
     */
    @Schema(description = "部门状态(0:正常 1:停用)")
    @Pattern(regexp = "^[01]$", message = "部门状态只能是0或1")
    private Integer status;

}

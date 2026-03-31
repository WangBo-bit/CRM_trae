package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色更新请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "角色更新请求")
public class RoleUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", required = true)
    @NotBlank(message = "角色ID不能为空")
    private String id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", required = true)
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", required = true)
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    /**
     * 角色类型(1:系统角色 2:自定义角色)
     */
    @Schema(description = "角色类型(1:系统角色 2:自定义角色)", required = true)
    @NotNull(message = "角色类型不能为空")
    private Integer roleType;

    /**
     * 数据权限(1:全部 2:本部门 3:本部门及下级 4:仅本人 5:自定义)
     */
    @Schema(description = "数据权限(1:全部 2:本部门 3:本部门及下级 4:仅本人 5:自定义)", required = true)
    @NotNull(message = "数据权限不能为空")
    private Integer dataScope;

    /**
     * 状态(0:禁用 1:启用)
     */
    @Schema(description = "状态(0:禁用 1:启用)")
    private Integer status;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}

package com.xindongli.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色响应
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "角色响应")
public class RoleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private String id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;

    /**
     * 角色类型(1:系统角色 2:自定义角色)
     */
    @Schema(description = "角色类型(1:系统角色 2:自定义角色)")
    private Integer roleType;

    /**
     * 数据权限(1:全部 2:本部门 3:本部门及下级 4:仅本人 5:自定义)
     */
    @Schema(description = "数据权限(1:全部 2:本部门 3:本部门及下级 4:仅本人 5:自定义)")
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

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

}

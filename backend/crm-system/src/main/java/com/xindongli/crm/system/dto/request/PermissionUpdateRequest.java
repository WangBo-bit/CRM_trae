package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限更新请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "权限更新请求")
public class PermissionUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID", required = true)
    @NotBlank(message = "权限ID不能为空")
    private String id;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID")
    private String parentId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称", required = true)
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识", required = true)
    @NotBlank(message = "权限标识不能为空")
    private String permissionCode;

    /**
     * 类型(1:菜单 2:按钮 3:接口)
     */
    @Schema(description = "类型(1:菜单 2:按钮 3:接口)", required = true)
    @NotNull(message = "权限类型不能为空")
    private Integer permissionType;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    private String component;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 是否可见(0:否 1:是)
     */
    @Schema(description = "是否可见(0:否 1:是)")
    private Integer visible;

    /**
     * 状态(0:禁用 1:启用)
     */
    @Schema(description = "状态(0:禁用 1:启用)")
    private Integer status;

}

package com.xindongli.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限响应
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "权限响应")
public class PermissionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private String id;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID")
    private String parentId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String permissionName;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String permissionCode;

    /**
     * 类型(1:菜单 2:按钮 3:接口)
     */
    @Schema(description = "类型(1:菜单 2:按钮 3:接口)")
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

    /**
     * 子权限列表
     */
    @Schema(description = "子权限列表")
    private List<PermissionResponse> children;

}

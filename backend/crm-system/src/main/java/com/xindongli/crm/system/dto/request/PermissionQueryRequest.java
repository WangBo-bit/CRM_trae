package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限查询请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "权限查询请求")
public class PermissionQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 状态(0:禁用 1:启用)
     */
    @Schema(description = "状态(0:禁用 1:启用)")
    private Integer status;

}

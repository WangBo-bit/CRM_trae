package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色查询请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "角色查询请求")
public class RoleQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 状态(0:禁用 1:启用)
     */
    @Schema(description = "状态(0:禁用 1:启用)")
    private Integer status;

    /**
     * 页码
     */
    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

}

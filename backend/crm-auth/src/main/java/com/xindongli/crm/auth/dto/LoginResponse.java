package com.xindongli.crm.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 登录响应DTO
 */
@Data
@Schema(description = "登录响应")
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌")
    private String token;

    /**
     * 刷新令牌
     */
    @Schema(description = "刷新令牌")
    private String refreshToken;

    /**
     * 过期时间(秒)
     */
    @Schema(description = "过期时间(秒)")
    private Long expiresIn;

    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private UserInfo user;

    /**
     * 用户信息
     */
    @Data
    @Schema(description = "用户信息")
    public static class UserInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 用户ID
         */
        @Schema(description = "用户ID")
        private Long id;

        /**
         * 用户名
         */
        @Schema(description = "用户名")
        private String username;

        /**
         * 真实姓名
         */
        @Schema(description = "真实姓名")
        private String realName;

        /**
         * 手机号
         */
        @Schema(description = "手机号")
        private String phone;

        /**
         * 邮箱
         */
        @Schema(description = "邮箱")
        private String email;

        /**
         * 头像
         */
        @Schema(description = "头像")
        private String avatar;

        /**
         * 部门ID
         */
        @Schema(description = "部门ID")
        private Long deptId;

        /**
         * 部门名称
         */
        @Schema(description = "部门名称")
        private String deptName;

        /**
         * 角色列表
         */
        @Schema(description = "角色列表")
        private List<RoleInfo> roles;

        /**
         * 权限列表
         */
        @Schema(description = "权限列表")
        private List<String> permissions;

    }

    /**
     * 角色信息
     */
    @Data
    @Schema(description = "角色信息")
    public static class RoleInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 角色ID
         */
        @Schema(description = "角色ID")
        private Long id;

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

    }

}

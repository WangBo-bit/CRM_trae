package com.xindongli.crm.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 联系人更新请求
 * 
 * @author 芯动力科技
 */
@Data
public class ContactUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人ID
     */
    @NotNull(message = "联系人ID不能为空")
    private Long id;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    /**
     * 性别(0:未知 1:男 2:女)
     */
    private Integer gender;

    /**
     * 职位
     */
    @Size(max = 50, message = "职位长度不能超过50个字符")
    private String position;

    /**
     * 部门
     */
    @Size(max = 100, message = "部门长度不能超过100个字符")
    private String department;

    /**
     * 电话
     */
    @Size(max = 20, message = "电话长度不能超过20个字符")
    private String phone;

    /**
     * 手机
     */
    @Size(max = 20, message = "手机长度不能超过20个字符")
    private String mobile;

    /**
     * 邮箱
     */
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 微信号
     */
    @Size(max = 50, message = "微信号长度不能超过50个字符")
    private String wechat;

    /**
     * QQ号
     */
    @Size(max = 20, message = "QQ号长度不能超过20个字符")
    private String qq;

    /**
     * 地址
     */
    @Size(max = 500, message = "地址长度不能超过500个字符")
    private String address;

    /**
     * 决策层级(decision_maker:决策者 influencer:影响者 executor:执行者)
     */
    @Size(max = 20, message = "决策层级长度不能超过20个字符")
    private String decisionLevel;

    /**
     * 是否主要联系人(0:否 1:是)
     */
    private Integer isPrimary;

    /**
     * 偏好标签(JSON)
     */
    @Size(max = 500, message = "偏好标签长度不能超过500个字符")
    private String preferenceTags;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 爱好
     */
    @Size(max = 500, message = "爱好长度不能超过500个字符")
    private String hobby;

    /**
     * 备注
     */
    private String remark;

}

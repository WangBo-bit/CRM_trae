package com.xindongli.crm.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 联系人实体类
 * 
 * @author 芯动力科技
 */
@Data
@TableName("crm_contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别(0:未知 1:男 2:女)
     */
    private Integer gender;

    /**
     * 职位
     */
    private String position;

    /**
     * 部门
     */
    private String department;

    /**
     * 电话
     */
    private String phone;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 地址
     */
    private String address;

    /**
     * 决策层级(decision_maker:决策者 influencer:影响者 executor:执行者)
     */
    private String decisionLevel;

    /**
     * 是否主要联系人(0:否 1:是)
     */
    private Integer isPrimary;

    /**
     * 偏好标签(JSON)
     */
    private String preferenceTags;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 爱好
     */
    private String hobby;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志(0:未删除 1:已删除)
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}

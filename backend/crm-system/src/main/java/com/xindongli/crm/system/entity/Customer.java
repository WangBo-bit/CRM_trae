package com.xindongli.crm.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 
 * @author 芯动力科技
 */
@Data
@TableName("crm_customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 行业
     */
    private String industry;

    /**
     * 子行业
     */
    private String subIndustry;

    /**
     * 企业规模
     */
    private String companyScale;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 企业官网
     */
    private String website;

    /**
     * 客户等级(A/B/C/D)
     */
    private String customerLevel;

    /**
     * 客户状态(potential:潜在 intentional:意向 won:成交 lost:流失)
     */
    private String customerStatus;

    /**
     * 客户来源
     */
    private String customerSource;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 负责人姓名
     */
    private String ownerName;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 团队成员(JSON)
     */
    private String teamMembers;

    /**
     * 标签(JSON)
     */
    private String tags;

    /**
     * 备注
     */
    private String remark;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 最后跟进时间
     */
    private LocalDateTime lastFollowTime;

    /**
     * 是否公海池(0:否 1:是)
     */
    private Integer isPool;

    /**
     * 进入公海池时间
     */
    private LocalDateTime poolTime;

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

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

}

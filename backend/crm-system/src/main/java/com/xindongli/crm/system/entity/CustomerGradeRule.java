package com.xindongli.crm.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户分级规则实体类
 * 
 * @author 芯动力科技
 */
@Data
@TableName("crm_customer_grade_rule")
public class CustomerGradeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型(amount:金额 industry:行业 source:来源 partner_type:合作伙伴类型)
     */
    private String ruleType;

    /**
     * 最小值(金额规则时为金额最小值,单位:万元)
     */
    private BigDecimal minValue;

    /**
     * 最大值(金额规则时为金额最大值,单位:万元)
     */
    private BigDecimal maxValue;

    /**
     * 目标等级(A/B/C/D)
     */
    private String targetGrade;

    /**
     * 优先级(数字越小优先级越高)
     */
    private Integer priority;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Integer status;

    /**
     * 规则描述
     */
    private String description;

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

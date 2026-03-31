package com.xindongli.crm.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户公海池规则实体类
 *
 * @author 芯动力科技
 */
@Data
@TableName("crm_customer_pool_rule")
public class CustomerPoolRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型(no_follow:未跟进 opportunity_closed:商机关闭 customer_lost:客户流失 manual:手动放入)
     */
    private String ruleType;

    /**
     * 天数阈值
     */
    private Integer daysThreshold;

    /**
     * 规则描述
     */
    private String ruleDescription;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Integer status;

    /**
     * 优先级(数字越小优先级越高)
     */
    private Integer priority;

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

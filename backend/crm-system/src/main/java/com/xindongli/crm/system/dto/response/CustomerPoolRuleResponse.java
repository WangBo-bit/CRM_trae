package com.xindongli.crm.system.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公海池规则响应
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolRuleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型
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
     * 优先级
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}

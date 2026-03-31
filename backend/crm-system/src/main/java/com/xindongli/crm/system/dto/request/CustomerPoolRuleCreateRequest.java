package com.xindongli.crm.system.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 公海池规则创建请求
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolRuleCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    /**
     * 规则类型(no_follow:未跟进 opportunity_closed:商机关闭 customer_lost:客户流失 manual:手动放入)
     */
    @NotBlank(message = "规则类型不能为空")
    private String ruleType;

    /**
     * 天数阈值
     */
    @NotNull(message = "天数阈值不能为空")
    @Min(value = 1, message = "天数阈值最小为1天")
    @Max(value = 365, message = "天数阈值最大为365天")
    private Integer daysThreshold;

    /**
     * 规则描述
     */
    private String ruleDescription;

    /**
     * 状态(0:禁用 1:启用)
     */
    @Min(value = 0, message = "状态值必须为0或1")
    @Max(value = 1, message = "状态值必须为0或1")
    private Integer status;

    /**
     * 优先级(数字越小优先级越高)
     */
    @Min(value = 1, message = "优先级最小为1")
    @Max(value = 1000, message = "优先级最大为1000")
    private Integer priority;

}

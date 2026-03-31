package com.xindongli.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户分级规则响应
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "客户分级规则响应")
public class CustomerGradeRuleResponse {

    @Schema(description = "规则ID")
    private Long ruleId;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "规则类型(amount:金额 industry:行业 source:来源 partner_type:合作伙伴类型)")
    private String ruleType;

    @Schema(description = "最小值(金额规则时为金额最小值,单位:万元)")
    private BigDecimal minValue;

    @Schema(description = "最大值(金额规则时为金额最大值,单位:万元)")
    private BigDecimal maxValue;

    @Schema(description = "目标等级(A/B/C/D)")
    private String targetGrade;

    @Schema(description = "优先级(数字越小优先级越高)")
    private Integer priority;

    @Schema(description = "状态(0:禁用 1:启用)")
    private Integer status;

    @Schema(description = "规则描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}

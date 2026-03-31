package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户分级规则创建请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "客户分级规则创建请求")
public class CustomerGradeRuleCreateRequest {

    @Schema(description = "规则名称", required = true)
    @NotBlank(message = "规则名称不能为空")
    @Size(max = 100, message = "规则名称长度不能超过100个字符")
    private String ruleName;

    @Schema(description = "规则类型(amount:金额 industry:行业 source:来源 partner_type:合作伙伴类型)", required = true)
    @NotBlank(message = "规则类型不能为空")
    @Pattern(regexp = "^(amount|industry|source|partner_type)$", message = "规则类型必须是amount、industry、source或partner_type之一")
    private String ruleType;

    @Schema(description = "最小值(金额规则时为金额最小值,单位:万元)")
    @DecimalMin(value = "0", message = "最小值不能为负数")
    private BigDecimal minValue;

    @Schema(description = "最大值(金额规则时为金额最大值,单位:万元)")
    @DecimalMin(value = "0", message = "最大值不能为负数")
    private BigDecimal maxValue;

    @Schema(description = "目标等级(A/B/C/D)", required = true)
    @NotBlank(message = "目标等级不能为空")
    @Pattern(regexp = "^[A-D]$", message = "目标等级必须是A、B、C或D")
    private String targetGrade;

    @Schema(description = "优先级(数字越小优先级越高)", required = true)
    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级最小值为1")
    @Max(value = 999, message = "优先级最大值为999")
    private Integer priority;

    @Schema(description = "状态(0:禁用 1:启用)")
    @Min(value = 0, message = "状态值必须为0或1")
    @Max(value = 1, message = "状态值必须为0或1")
    private Integer status;

    @Schema(description = "规则描述")
    @Size(max = 500, message = "规则描述长度不能超过500个字符")
    private String description;

}

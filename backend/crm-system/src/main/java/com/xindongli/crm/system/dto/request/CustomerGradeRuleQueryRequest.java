package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 客户分级规则查询请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "客户分级规则查询请求")
public class CustomerGradeRuleQueryRequest {

    @Schema(description = "规则名称(模糊查询)")
    private String ruleName;

    @Schema(description = "规则类型(amount:金额 industry:行业 source:来源 partner_type:合作伙伴类型)")
    @Pattern(regexp = "^(amount|industry|source|partner_type)$", message = "规则类型必须是amount、industry、source或partner_type之一")
    private String ruleType;

    @Schema(description = "目标等级(A/B/C/D)")
    @Pattern(regexp = "^[A-D]$", message = "目标等级必须是A、B、C或D")
    private String targetGrade;

    @Schema(description = "状态(0:禁用 1:启用)")
    @Min(value = 0, message = "状态值必须为0或1")
    @Max(value = 1, message = "状态值必须为0或1")
    private Integer status;

    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码最小值为1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小最小值为1")
    @Max(value = 100, message = "每页大小最大值为100")
    private Integer pageSize = 10;

}

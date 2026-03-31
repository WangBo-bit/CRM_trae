package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 计算客户等级请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "计算客户等级请求")
public class CustomerGradeCalculateRequest {

    @Schema(description = "客户ID", required = true)
    @NotNull(message = "客户ID不能为空")
    @Min(value = 1, message = "客户ID必须大于0")
    private Long customerId;

    @Schema(description = "年采购额(单位:万元)")
    @DecimalMin(value = "0", message = "年采购额不能为负数")
    private BigDecimal annualPurchaseAmount;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "客户来源")
    private String customerSource;

    @Schema(description = "合作伙伴类型")
    private String partnerType;

}

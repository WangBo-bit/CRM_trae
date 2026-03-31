package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 手动调整客户等级请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "手动调整客户等级请求")
public class CustomerGradeAdjustRequest {

    @Schema(description = "客户ID", required = true)
    @NotNull(message = "客户ID不能为空")
    @Min(value = 1, message = "客户ID必须大于0")
    private Long customerId;

    @Schema(description = "目标等级(A/B/C/D)", required = true)
    @NotBlank(message = "目标等级不能为空")
    @Pattern(regexp = "^[A-D]$", message = "目标等级必须是A、B、C或D")
    private String targetGrade;

    @Schema(description = "调整原因")
    private String adjustReason;

}

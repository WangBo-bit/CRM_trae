package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.response.CustomerPanoramaResponse;
import com.xindongli.crm.system.dto.response.CustomerStatisticsResponse;
import com.xindongli.crm.system.dto.response.CustomerTimelineResponse;
import com.xindongli.crm.system.service.CustomerPanoramaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 客户全景视图控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "客户全景视图", description = "客户全景视图相关接口")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerPanoramaController {

    private final CustomerPanoramaService customerPanoramaService;

    /**
     * 获取客户全景视图
     */
    @Operation(summary = "获取客户全景视图", description = "360度展示客户全貌，包括基本信息、联系人、跟进记录、商机、POC、技术评估等")
    @GetMapping("/{id}/panorama")
    public Result<CustomerPanoramaResponse> getCustomerPanorama(
            @Parameter(description = "客户ID") 
            @PathVariable @Min(value = 1, message = "客户ID必须大于0") Long id) {
        CustomerPanoramaResponse response = customerPanoramaService.getCustomerPanorama(id);
        return Result.success(response);
    }

    /**
     * 获取客户统计数据
     */
    @Operation(summary = "获取客户统计数据", description = "获取客户的统计数据，包括跟进次数、联系人数量、商机数量等")
    @GetMapping("/{id}/statistics")
    public Result<CustomerStatisticsResponse> getCustomerStatistics(
            @Parameter(description = "客户ID") 
            @PathVariable @Min(value = 1, message = "客户ID必须大于0") Long id) {
        CustomerStatisticsResponse response = customerPanoramaService.getCustomerStatistics(id);
        return Result.success(response);
    }

    /**
     * 获取客户动态时间线
     */
    @Operation(summary = "获取客户动态时间线", description = "获取客户的动态时间线，包括跟进记录、商机变更、POC进度等")
    @GetMapping("/{id}/timeline")
    public Result<List<CustomerTimelineResponse>> getCustomerTimeline(
            @Parameter(description = "客户ID") 
            @PathVariable @Min(value = 1, message = "客户ID必须大于0") Long id,
            @Parameter(description = "限制数量") 
            @RequestParam(required = false, defaultValue = "20") 
            @Min(value = 1, message = "限制数量必须大于0") 
            @Max(value = 100, message = "限制数量不能超过100") Integer limit) {
        List<CustomerTimelineResponse> response = customerPanoramaService.getCustomerTimeline(id, limit);
        return Result.success(response);
    }

}

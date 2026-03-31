package com.xindongli.crm.system.service;

import com.xindongli.crm.system.dto.response.CustomerPanoramaResponse;
import com.xindongli.crm.system.dto.response.CustomerStatisticsResponse;
import com.xindongli.crm.system.dto.response.CustomerTimelineResponse;

import java.util.List;

/**
 * 客户全景视图服务接口
 * 
 * @author 芯动力科技
 */
public interface CustomerPanoramaService {

    /**
     * 获取客户全景视图
     * 
     * @param customerId 客户ID
     * @return 客户全景视图
     */
    CustomerPanoramaResponse getCustomerPanorama(Long customerId);

    /**
     * 获取客户统计数据
     * 
     * @param customerId 客户ID
     * @return 客户统计数据
     */
    CustomerStatisticsResponse getCustomerStatistics(Long customerId);

    /**
     * 获取客户动态时间线
     * 
     * @param customerId 客户ID
     * @param limit 限制数量
     * @return 动态时间线列表
     */
    List<CustomerTimelineResponse> getCustomerTimeline(Long customerId, Integer limit);

}

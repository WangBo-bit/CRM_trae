package com.xindongli.crm.system.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户动态时间线响应
 * 
 * @author 芯动力科技
 */
@Data
public class CustomerTimelineResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 动态ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 动态类型(follow:跟进 opportunity:商机 poc:POC assessment:评估 other:其他)
     */
    private String timelineType;

    /**
     * 动态类型名称
     */
    private String timelineTypeName;

    /**
     * 动态标题
     */
    private String title;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 动态时间
     */
    private LocalDateTime timelineTime;

    /**
     * 关联ID(如跟进记录ID、商机ID等)
     */
    private Long relatedId;

    /**
     * 关联类型
     */
    private String relatedType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

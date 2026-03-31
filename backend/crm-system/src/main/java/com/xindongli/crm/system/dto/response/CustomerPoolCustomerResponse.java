package com.xindongli.crm.system.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公海池客户响应
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolCustomerResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    private Long id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 行业
     */
    private String industry;

    /**
     * 子行业
     */
    private String subIndustry;

    /**
     * 企业规模
     */
    private String companyScale;

    /**
     * 客户等级(A/B/C/D)
     */
    private String customerLevel;

    /**
     * 客户状态
     */
    private String customerStatus;

    /**
     * 客户来源
     */
    private String customerSource;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地址
     */
    private String address;

    /**
     * 进入公海池时间
     */
    private LocalDateTime poolTime;

    /**
     * 最后跟进时间
     */
    private LocalDateTime lastFollowTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

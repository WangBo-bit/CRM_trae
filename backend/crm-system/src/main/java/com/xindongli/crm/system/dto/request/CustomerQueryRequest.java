package com.xindongli.crm.system.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户查询请求
 * 
 * @author 芯动力科技
 */
@Data
public class CustomerQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long pageNum = 1L;

    /**
     * 每页记录数
     */
    private Long pageSize = 10L;

    /**
     * 企业名称(模糊查询)
     */
    private String companyName;

    /**
     * 行业
     */
    private String industry;

    /**
     * 客户等级(A/B/C/D)
     */
    private String customerLevel;

    /**
     * 客户状态(potential:潜在 intentional:意向 won:成交 lost:流失)
     */
    private String customerStatus;

    /**
     * 客户来源
     */
    private String customerSource;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 是否公海池(0:否 1:是)
     */
    private Integer isPool;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 关键字(企业名称/企业简称)
     */
    private String keyword;

}

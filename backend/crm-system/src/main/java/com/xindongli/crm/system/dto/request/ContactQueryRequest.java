package com.xindongli.crm.system.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 联系人查询请求
 * 
 * @author 芯动力科技
 */
@Data
public class ContactQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 姓名(模糊查询)
     */
    private String name;

    /**
     * 职位
     */
    private String position;

    /**
     * 部门
     */
    private String department;

    /**
     * 是否主要联系人(0:否 1:是)
     */
    private Integer isPrimary;

    /**
     * 决策层级(decision_maker:决策者 influencer:影响者 executor:执行者)
     */
    private String decisionLevel;

}

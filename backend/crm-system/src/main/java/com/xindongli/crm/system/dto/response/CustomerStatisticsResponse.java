package com.xindongli.crm.system.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户统计数据响应
 * 
 * @author 芯动力科技
 */
@Data
public class CustomerStatisticsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 跟进次数
     */
    private Integer followCount;

    /**
     * 联系人数量
     */
    private Integer contactCount;

    /**
     * 商机数量
     */
    private Integer opportunityCount;

    /**
     * 商机总金额
     */
    private BigDecimal opportunityAmount;

    /**
     * POC数量
     */
    private Integer pocCount;

    /**
     * 技术评估数量
     */
    private Integer assessmentCount;

    /**
     * 年采购额
     */
    private BigDecimal annualPurchaseAmount;

    /**
     * 合同数量
     */
    private Integer contractCount;

}

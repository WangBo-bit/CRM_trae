package com.xindongli.crm.system.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 从公海池认领客户请求
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolClaimRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 负责人ID
     */
    @NotNull(message = "负责人ID不能为空")
    private Long ownerId;

    /**
     * 负责人姓名
     */
    @NotNull(message = "负责人姓名不能为空")
    private String ownerName;

}

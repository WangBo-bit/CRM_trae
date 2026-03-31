package com.xindongli.crm.system.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 批量认领客户请求
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolBatchClaimRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID列表
     */
    @NotEmpty(message = "客户ID列表不能为空")
    @Size(max = 100, message = "单次最多认领100个客户")
    private List<Long> customerIds;

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

package com.xindongli.crm.system.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 批量放入公海池请求
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolBatchPutRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID列表
     */
    @NotEmpty(message = "客户ID列表不能为空")
    @Size(max = 100, message = "单次最多放入100个客户")
    private List<Long> customerIds;

}

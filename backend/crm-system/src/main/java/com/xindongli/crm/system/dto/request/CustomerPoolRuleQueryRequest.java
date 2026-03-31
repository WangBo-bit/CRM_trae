package com.xindongli.crm.system.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 公海池规则查询请求
 *
 * @author 芯动力科技
 */
@Data
public class CustomerPoolRuleQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Integer status;

    /**
     * 页码
     */
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    @Min(value = 1, message = "每页记录数最小为1")
    @Max(value = 100, message = "每页记录数最大为100")
    private Integer pageSize = 10;

}

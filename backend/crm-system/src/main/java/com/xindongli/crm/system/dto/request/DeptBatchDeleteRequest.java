package com.xindongli.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 部门批量删除请求
 * 
 * @author 芯动力科技
 */
@Data
@Schema(description = "部门批量删除请求")
public class DeptBatchDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID列表
     */
    @Schema(description = "部门ID列表", required = true)
    @NotEmpty(message = "部门ID列表不能为空")
    private List<Long> ids;

}

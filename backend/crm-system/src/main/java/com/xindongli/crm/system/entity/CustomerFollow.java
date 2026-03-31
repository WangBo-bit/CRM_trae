package com.xindongli.crm.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户跟进记录实体类
 * 
 * @author 芯动力科技
 */
@Data
@TableName("crm_customer_follow")
public class CustomerFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跟进记录ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 联系人ID
     */
    private Long contactId;

    /**
     * 跟进方式(phone:电话 visit:上门 wechat:微信 email:邮件 other:其他)
     */
    private String followType;

    /**
     * 跟进内容
     */
    private String followContent;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 跟进时间
     */
    private LocalDateTime followTime;

    /**
     * 附件(JSON)
     */
    private String attachments;

    /**
     * 跟进人ID
     */
    private Long userId;

    /**
     * 跟进人姓名
     */
    private String userName;

    /**
     * 删除标志(0:未删除 1:已删除)
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}

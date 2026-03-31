package com.xindongli.crm.system.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户跟进记录响应
 * 
 * @author 芯动力科技
 */
@Data
public class CustomerFollowResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跟进记录ID
     */
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
     * 联系人姓名
     */
    private String contactName;

    /**
     * 跟进方式(phone:电话 visit:上门 wechat:微信 email:邮件 other:其他)
     */
    private String followType;

    /**
     * 跟进方式名称
     */
    private String followTypeName;

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
     * 创建时间
     */
    private LocalDateTime createTime;

}

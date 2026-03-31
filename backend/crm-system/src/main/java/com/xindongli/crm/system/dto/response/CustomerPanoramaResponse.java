package com.xindongli.crm.system.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户全景视图响应
 * 
 * @author 芯动力科技
 */
@Data
public class CustomerPanoramaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 基本信息 ====================

    /**
     * 客户ID
     */
    private Long id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 行业
     */
    private String industry;

    /**
     * 子行业
     */
    private String subIndustry;

    /**
     * 企业规模
     */
    private String companyScale;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 企业官网
     */
    private String website;

    /**
     * 客户等级(A/B/C/D)
     */
    private String customerLevel;

    /**
     * 客户等级名称
     */
    private String customerLevelName;

    /**
     * 客户状态(potential:潜在 intentional:意向 won:成交 lost:流失)
     */
    private String customerStatus;

    /**
     * 客户状态名称
     */
    private String customerStatusName;

    /**
     * 客户来源
     */
    private String customerSource;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 负责人姓名
     */
    private String ownerName;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 所属部门名称
     */
    private String deptName;

    /**
     * 团队成员(JSON)
     */
    private String teamMembers;

    /**
     * 标签(JSON)
     */
    private String tags;

    /**
     * 备注
     */
    private String remark;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 最后跟进时间
     */
    private LocalDateTime lastFollowTime;

    /**
     * 是否公海池(0:否 1:是)
     */
    private Integer isPool;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // ==================== 联系人信息 ====================

    /**
     * 主联系人
     */
    private ContactResponse primaryContact;

    /**
     * 联系人数量
     */
    private Integer contactCount;

    /**
     * 联系人列表
     */
    private List<ContactResponse> contacts;

    // ==================== 跟进记录信息 ====================

    /**
     * 最近跟进时间
     */
    private LocalDateTime recentFollowTime;

    /**
     * 跟进次数
     */
    private Integer followCount;

    /**
     * 跟进记录列表(最近10条)
     */
    private List<CustomerFollowResponse> recentFollows;

    // ==================== 商机信息（预留） ====================

    /**
     * 商机数量
     */
    private Integer opportunityCount;

    /**
     * 商机总金额
     */
    private BigDecimal opportunityAmount;

    /**
     * 商机列表（预留）
     */
    private List<Object> opportunities;

    // ==================== POC信息（预留） ====================

    /**
     * POC数量
     */
    private Integer pocCount;

    /**
     * POC状态统计
     */
    private Object pocStatusStats;

    /**
     * POC列表（预留）
     */
    private List<Object> pocs;

    // ==================== 技术评估信息（预留） ====================

    /**
     * 技术评估数量
     */
    private Integer assessmentCount;

    /**
     * 技术评估列表（预留）
     */
    private List<Object> assessments;

    // ==================== 合作数据（预留） ====================

    /**
     * 年采购额
     */
    private BigDecimal annualPurchaseAmount;

    /**
     * 合同数量
     */
    private Integer contractCount;

    // ==================== 动态时间线 ====================

    /**
     * 客户动态时间线
     */
    private List<CustomerTimelineResponse> timeline;

    // ==================== 统计数据 ====================

    /**
     * 客户统计数据
     */
    private CustomerStatistics statistics;

    /**
     * 客户统计数据内部类
     */
    @Data
    public static class CustomerStatistics implements Serializable {

        private static final long serialVersionUID = 1L;

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

}

package com.xindongli.crm.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户更新请求
 * 
 * @author 芯动力科技
 */
@Data
public class CustomerUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long id;

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    @Size(max = 200, message = "企业名称长度不能超过200个字符")
    private String companyName;

    /**
     * 企业简称
     */
    @Size(max = 100, message = "企业简称长度不能超过100个字符")
    private String shortName;

    /**
     * 行业
     */
    @NotBlank(message = "行业不能为空")
    @Size(max = 50, message = "行业长度不能超过50个字符")
    private String industry;

    /**
     * 子行业
     */
    @Size(max = 50, message = "子行业长度不能超过50个字符")
    private String subIndustry;

    /**
     * 企业规模
     */
    @Size(max = 20, message = "企业规模长度不能超过20个字符")
    private String companyScale;

    /**
     * 企业地址
     */
    @Size(max = 500, message = "企业地址长度不能超过500个字符")
    private String address;

    /**
     * 省份
     */
    @Size(max = 50, message = "省份长度不能超过50个字符")
    private String province;

    /**
     * 城市
     */
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String city;

    /**
     * 区县
     */
    @Size(max = 50, message = "区县长度不能超过50个字符")
    private String district;

    /**
     * 企业官网
     */
    @Size(max = 255, message = "企业官网长度不能超过255个字符")
    private String website;

    /**
     * 客户等级(A/B/C/D)
     */
    @Size(max = 10, message = "客户等级长度不能超过10个字符")
    private String customerLevel;

    /**
     * 客户状态(potential:潜在 intentional:意向 won:成交 lost:流失)
     */
    @Size(max = 20, message = "客户状态长度不能超过20个字符")
    private String customerStatus;

    /**
     * 客户来源
     */
    @Size(max = 50, message = "客户来源长度不能超过50个字符")
    private String customerSource;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 负责人姓名
     */
    @Size(max = 50, message = "负责人姓名长度不能超过50个字符")
    private String ownerName;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 团队成员(JSON)
     */
    @Size(max = 500, message = "团队成员长度不能超过500个字符")
    private String teamMembers;

    /**
     * 标签(JSON)
     */
    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;

    /**
     * 备注
     */
    private String remark;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

}

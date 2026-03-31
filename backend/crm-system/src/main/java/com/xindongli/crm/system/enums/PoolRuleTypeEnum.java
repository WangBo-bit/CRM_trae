package com.xindongli.crm.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公海池规则类型枚举
 *
 * @author 芯动力科技
 */
@Getter
@AllArgsConstructor
public enum PoolRuleTypeEnum {

    /**
     * 未跟进
     */
    NO_FOLLOW("no_follow", "未跟进"),

    /**
     * 商机关闭
     */
    OPPORTUNITY_CLOSED("opportunity_closed", "商机关闭"),

    /**
     * 客户流失
     */
    CUSTOMER_LOST("customer_lost", "客户流失"),

    /**
     * 手动放入
     */
    MANUAL("manual", "手动放入");

    /**
     * 规则类型编码
     */
    private final String code;

    /**
     * 规则类型名称
     */
    private final String name;

    /**
     * 根据编码获取枚举
     *
     * @param code 规则类型编码
     * @return 枚举对象
     */
    public static PoolRuleTypeEnum getByCode(String code) {
        for (PoolRuleTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

}

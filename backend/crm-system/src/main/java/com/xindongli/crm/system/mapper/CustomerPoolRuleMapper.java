package com.xindongli.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xindongli.crm.system.entity.CustomerPoolRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户公海池规则Mapper接口
 *
 * @author 芯动力科技
 */
@Mapper
public interface CustomerPoolRuleMapper extends BaseMapper<CustomerPoolRule> {

    /**
     * 根据规则名称查询规则
     *
     * @param ruleName 规则名称
     * @return 公海池规则
     */
    CustomerPoolRule selectByRuleName(String ruleName);

}

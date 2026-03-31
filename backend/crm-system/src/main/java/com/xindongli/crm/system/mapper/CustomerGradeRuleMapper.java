package com.xindongli.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xindongli.crm.system.entity.CustomerGradeRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户分级规则Mapper接口
 * 
 * @author 芯动力科技
 */
@Mapper
public interface CustomerGradeRuleMapper extends BaseMapper<CustomerGradeRule> {

    /**
     * 查询启用的分级规则列表(按优先级排序)
     * 
     * @return 分级规则列表
     */
    List<CustomerGradeRule> selectEnabledRulesOrderByPriority();

    /**
     * 根据规则类型查询规则列表
     * 
     * @param ruleType 规则类型
     * @return 分级规则列表
     */
    List<CustomerGradeRule> selectByRuleType(@Param("ruleType") String ruleType);

    /**
     * 根据目标等级查询规则列表
     * 
     * @param targetGrade 目标等级
     * @return 分级规则列表
     */
    List<CustomerGradeRule> selectByTargetGrade(@Param("targetGrade") String targetGrade);

}

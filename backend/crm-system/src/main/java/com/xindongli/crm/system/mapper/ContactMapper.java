package com.xindongli.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xindongli.crm.system.entity.Contact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 联系人Mapper接口
 * 
 * @author 芯动力科技
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {

    /**
     * 根据客户ID查询联系人列表
     * 
     * @param customerId 客户ID
     * @return 联系人列表
     */
    List<Contact> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 根据客户ID查询主要联系人
     * 
     * @param customerId 客户ID
     * @return 主要联系人
     */
    Contact selectPrimaryContactByCustomerId(@Param("customerId") Long customerId);

}

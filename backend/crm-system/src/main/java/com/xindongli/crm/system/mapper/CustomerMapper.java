package com.xindongli.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xindongli.crm.system.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户Mapper接口
 * 
 * @author 芯动力科技
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 根据客户编码查询客户
     * 
     * @param customerCode 客户编码
     * @return 客户信息
     */
    Customer selectByCustomerCode(@Param("customerCode") String customerCode);

    /**
     * 根据企业名称查询客户
     * 
     * @param companyName 企业名称
     * @return 客户信息
     */
    Customer selectByCompanyName(@Param("companyName") String companyName);

}

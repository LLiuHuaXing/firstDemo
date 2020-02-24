package com.sxt.bus.mapper;

import com.sxt.bus.domain.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.bus.vo.CustomerVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *

 */
public interface CustomerMapper extends BaseMapper<Customer> {

    void updateDeptAvailable(@Param("customerVo") CustomerVo customerVo);
}

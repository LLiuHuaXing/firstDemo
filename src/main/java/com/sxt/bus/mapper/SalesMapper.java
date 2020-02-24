package com.sxt.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.bus.domain.Sales;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *

 */
public interface SalesMapper extends BaseMapper<Sales> {

    void deleteSalesBackForSalesid(Serializable id);

    int selectBysalesId(Serializable id);
}

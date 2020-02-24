package com.sxt.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.bus.domain.Customer;
import com.sxt.bus.vo.CustomerVo;
import com.sxt.stat.domain.CustomerAreaStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface CustomerAreaStatMapper extends BaseMapper<CustomerAreaStat> {

    List<CustomerAreaStat> loadAllCustomerAreaStat();

    List<Double> loadAllYearMonthSalesStart(@Param("value") String value);

    List<Double> loadAllYearMonthJudgeStart(@Param("value") String year);

    List<CustomerAreaStat> loadAllYearJudgeStart(@Param("startYear") String startYear,@Param("endYear") String endYear);

    List<CustomerAreaStat> loadAllYearSalesStart(@Param("startYear") String startYear,@Param("endYear") String endYear);

    List<CustomerAreaStat> loadAllSalesPersonSalesStart(@Param("customerAreaStat") CustomerAreaStat customerAreaStat);

    List<Object> selectDeptIdForId(@Param("id") Integer id);

    List<Object> selectUserJobnumberFordeptid(@Param("deptList") List<Object> deptIdList);

    List<CustomerAreaStat> loadAllSalesPersonJudgeStart(@Param("customerAreaStat") CustomerAreaStat customerAreaStat);
}

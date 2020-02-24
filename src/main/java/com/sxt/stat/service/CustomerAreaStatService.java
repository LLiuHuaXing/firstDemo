package com.sxt.stat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.bus.domain.Customer;
import com.sxt.bus.vo.CustomerVo;
import com.sxt.stat.domain.CustomerAreaStat;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface CustomerAreaStatService extends IService<CustomerAreaStat> {


    List<CustomerAreaStat> loadAllCustomerAreaStat();

    List<Double> loadAllYearMonthSalesStart(String year);

    List<Double> loadAllYearMonthJudgeStart(String year);

    List<CustomerAreaStat> loadAllYearSalesStart(String startYear, String endYear);

    List<CustomerAreaStat> loadAllYearJudgeStart(String startYear, String endYear);

    List<CustomerAreaStat> loadAllSalesPersonSalesStart(CustomerAreaStat customerAreaStat);

    List<Object> selectDeptIdForId(Integer deptId);

    List<Object> selectUserJobnumberFordeptid(List<Object> deptIdList);

    List<CustomerAreaStat> loadAllSalesPersonJudgeStart(CustomerAreaStat customerAreaStat);
}

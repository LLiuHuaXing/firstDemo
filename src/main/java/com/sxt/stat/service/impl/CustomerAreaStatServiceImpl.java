package com.sxt.stat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.bus.domain.Customer;
import com.sxt.bus.mapper.CustomerMapper;
import com.sxt.bus.service.CustomerService;
import com.sxt.bus.vo.CustomerVo;
import com.sxt.stat.domain.CustomerAreaStat;
import com.sxt.stat.mapper.CustomerAreaStatMapper;
import com.sxt.stat.service.CustomerAreaStatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class CustomerAreaStatServiceImpl extends ServiceImpl<CustomerAreaStatMapper, CustomerAreaStat> implements CustomerAreaStatService {

    /*
     * 查看客户统计数据
     */
    @Override
    public List<CustomerAreaStat> loadAllCustomerAreaStat() {
        return this.getBaseMapper().loadAllCustomerAreaStat();
    }

    /*
     * 年度月销售统计
     */
    @Override
    public List<Double> loadAllYearMonthSalesStart(String year) {
        return this.getBaseMapper().loadAllYearMonthSalesStart(year);
    }

    /*
     * 年度月退货量统计
     */
    @Override
    public List<Double> loadAllYearMonthJudgeStart(String year) {
        return this.getBaseMapper().loadAllYearMonthJudgeStart(year);
    }

    /*
     * 年度销售量统计
     */
    @Override
    public List<CustomerAreaStat> loadAllYearSalesStart(String startYear, String endYear) {
        return this.getBaseMapper().loadAllYearSalesStart(startYear,endYear);
    }

    /*
     * 年度退货量统计
     */
    @Override
    public List<CustomerAreaStat> loadAllYearJudgeStart(String startYear, String endYear) {
        return this.getBaseMapper().loadAllYearJudgeStart(startYear,endYear);
    }

    /*
     * 销售员业绩统计管理
     */
    @Override
    public List<CustomerAreaStat> loadAllSalesPersonSalesStart(CustomerAreaStat customerAreaStat) {
        return this.getBaseMapper().loadAllSalesPersonSalesStart(customerAreaStat);
    }

    /*
     * 销售员退货量统计管理
     */
    @Override
    public List<CustomerAreaStat> loadAllSalesPersonJudgeStart(CustomerAreaStat customerAreaStat) {
        return this.getBaseMapper().loadAllSalesPersonJudgeStart(customerAreaStat);
    }

    /*
    * 根据部门id到部门表查询该部门以及子部门的id
    */
    @Override
    public List<Object> selectDeptIdForId(Integer id) {
        return this.getBaseMapper().selectDeptIdForId(id);
    }

    /*
    * 根据查询的部门id到用户表查询所有的用户工号
    */
    @Override
    public List<Object> selectUserJobnumberFordeptid(List<Object> deptIdList) {
        return this.getBaseMapper().selectUserJobnumberFordeptid(deptIdList);
    }


}

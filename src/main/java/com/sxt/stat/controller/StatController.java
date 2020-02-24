package com.sxt.stat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 业务管理的路由器
 * @author LJH
 *
 */
@Controller
@RequestMapping("/stat")
public class StatController {

	/**
	 * 跳转到客户地区统计管理
	 */
	@RequestMapping("toCustomerAreaStatManager")
	public String toCustomerAreaStatManager() {
		return "stat/region/CustomerAreaStatManager";
	}

	/**
	 * 跳转到月度销售-退货统计管理
	 */
	@RequestMapping("toMonthStatManager")
	public String toCustomerManager() {
		return "stat/dataTime/MonthStatManager";
	}

	/**
	 * 跳转到年度销售—退货统计管理
	 */
	@RequestMapping("toYearStatManager")
	public String toYearStatManager() {
		return "stat/dataTime/YearStatManager";
	}

	/**
	 * 跳转到销售员业绩管理
	 */
	@RequestMapping("toSalesPersonStatManager")
	public String toSalesPersonStatManager() {
		return "stat/salesPerson/SalesPersonStatManager";
	}
}

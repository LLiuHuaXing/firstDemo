package com.sxt.stat.controller;

import com.sxt.stat.domain.CustomerAreaStat;
import com.sxt.stat.service.CustomerAreaStatService;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/CustomerAreaStart")
public class CustomerAreaStatController {

	String startCondition="";
	String endCondition="";
	String dates="";

	@Autowired
	private CustomerAreaStatService customerAreaStatService;

	/*
	* 查看客户统计数据
	*/
	@RequestMapping("loadAllCustomerAreaStat")
	public DataGridView loadAllCustomerAreaStat(){
		List<CustomerAreaStat> list = this.customerAreaStatService.loadAllCustomerAreaStat();
		if(list.size()>0){
			return new DataGridView(Constast.OK,list);
		}
		return new DataGridView(Constast.DATA_ERROR,Constast.ERROR);
	}

	/*
	 * 月度销售量统计
	 */
	@RequestMapping("loadAllYearMonthSalesStart")
	public List<Double> loadAllYearMonthSalesStart(String year){
		List<Double> list = this.customerAreaStatService.loadAllYearMonthSalesStart(year);
			for(int i=0;i<list.size();i++){
				if(null==list.get(i)){
					list.set(i,0.0);
				}
			}
		return list;
	}

	/*
	 * 月度退货量统计
	 */
	@RequestMapping("loadAllYearMonthJudgeStart")
	public List<Double> loadAllYearMonthJudgeStart(String year){
		List<Double> list = this.customerAreaStatService.loadAllYearMonthJudgeStart(year);
		for(int i=0;i<list.size();i++){
			if(null==list.get(i)){
				list.set(i,0.0);
			}
		}
		return list;
	}

	/*
	 * 年度销售量统计
	 */
	@RequestMapping("loadAllYearSalesStart")
	public Map<String,Object> loadAllYearSalesStart(String startYear,String endYear){
		HashMap<String, Object> map= new HashMap<>();
		ArrayList<Object> timeObject = new ArrayList<>();
		ArrayList<Object> dataObject = new ArrayList<>();
		List<CustomerAreaStat> list = this.customerAreaStatService.loadAllYearSalesStart(startYear,endYear);
		if(list.size()>0){
			for (CustomerAreaStat customerAreaStat:list){
				timeObject.add(customerAreaStat.getName()+"年");
				dataObject.add(customerAreaStat.getValue());
			}
			map.put("code",Constast.OK);
			map.put("name",timeObject);
			map.put("value",dataObject);
		}else{
			map.put("code",Constast.ERROR);
			map.put("msg",Constast.DATA_ERROR);
		}
		return map;
	}

	/*
	 * 年度退货量统计
	 */
	@RequestMapping("loadAllYearJudgeStart")
	public Map<String,Object>loadAllYearJudgeStart(String startYear,String endYear){
		HashMap<String, Object> map= new HashMap<>();
		ArrayList<Object> timeObject = new ArrayList<>();
		ArrayList<Object> dataObject = new ArrayList<>();
		List<CustomerAreaStat> list = this.customerAreaStatService.loadAllYearJudgeStart(startYear,endYear);
		if(list.size()>0){
			for (CustomerAreaStat customerAreaStat:list){
				timeObject.add(customerAreaStat.getName()+"年");
				dataObject.add(customerAreaStat.getValue());
			}
			map.put("code",Constast.OK);
			map.put("name",timeObject);
			map.put("value",dataObject);
		}else{
			map.put("code",Constast.ERROR);
			map.put("msg",Constast.DATA_ERROR);
		}
		return map;
	}

	/*
	 * 销售员业绩统计管理
	 */
	@RequestMapping("loadAllSalesPersonSalesStart")
	public Map<String,Object> loadAllSalesPersonSalesStart(CustomerAreaStat customerAreaStat){
//		System.out.println("customerAreaStat = " + customerAreaStat);
		HashMap<String, Object> map= new HashMap<>();
		ArrayList<Object> timeObject = new ArrayList<>();
		ArrayList<Object> dataObject = new ArrayList<>();
		//数据格式的转换和切割
		dataFormat(customerAreaStat);
		List<CustomerAreaStat> list = this.customerAreaStatService.loadAllSalesPersonSalesStart(customerAreaStat);
		if(list.size()>0){
			for (CustomerAreaStat custom:list){
				timeObject.add(custom.getName());
				dataObject.add(custom.getValue());
			}
			map.put("code",Constast.OK);
			map.put("name",timeObject);
			map.put("value",dataObject);
		}else{
			map.put("code",Constast.ERROR);
			map.put("msg",Constast.DATA_ERROR);
		}
		return map;
	}

	/*
	 * 销售员退货量统计管理
	 */
	@RequestMapping("loadAllSalesPersonJudgeStart")
	public Map<String,Object> loadAllSalesPersonJudgeStart(CustomerAreaStat customerAreaStat){
		System.out.println("customerAreaStat = " + customerAreaStat);
		HashMap<String, Object> map= new HashMap<>();
		ArrayList<Object> timeObject = new ArrayList<>();
		ArrayList<Object> dataObject = new ArrayList<>();
		//数据格式的转换和切割
		dataFormat(customerAreaStat);
			List<CustomerAreaStat> list = this.customerAreaStatService.loadAllSalesPersonJudgeStart(customerAreaStat);
			if(list.size()>0){
				for (CustomerAreaStat custom:list){
					timeObject.add(custom.getName());
					dataObject.add(custom.getValue());
				}
			map.put("code",Constast.OK);
			map.put("name",timeObject);
			map.put("value",dataObject);
		}else{
			map.put("code",Constast.ERROR);
			map.put("msg",Constast.DATA_ERROR);
		}
		return map;
	}


	//数据格式的转换和切割
	public void dataFormat(CustomerAreaStat customerAreaStat){
		//年范围查询
		if(null!=customerAreaStat.getYearRange() && customerAreaStat.getYearRange()!=""){
			dates=customerAreaStat.getYearRange();
			startCondition= dates.substring(0,dates.indexOf(" - "));
			customerAreaStat.setStartCondition(startCondition);
			endCondition=dates.substring(dates.indexOf(" - ")+3,dates.length());
			customerAreaStat.setEndCondition(endCondition);
		}
		//年月范围查询
		if(null!=customerAreaStat.getYearMonthRange() && customerAreaStat.getYearMonthRange()!=""){
			dates=customerAreaStat.getYearMonthRange();
			//例如2019-12 - 2021-11,分割成2019-12和2021-11
			startCondition=dates.substring(0,dates.indexOf(" - "));
			customerAreaStat.setStartCondition(startCondition);
			endCondition=dates.substring(dates.indexOf(" - ")+3,dates.length());
			customerAreaStat.setEndCondition(endCondition);
			customerAreaStat.setNotSign("SIGN");
		}
		//年查询
		if(null!=customerAreaStat.getEndYearTime() && customerAreaStat.getEndYearTime()!=""){
			dates=customerAreaStat.getEndYearTime();
			customerAreaStat.setStartCondition(dates);
			customerAreaStat.setEndCondition(dates);
		}
		//年月查询
		if(null!=customerAreaStat.getEndYearMonthTime() && customerAreaStat.getEndYearMonthTime()!=""){
			dates=customerAreaStat.getEndYearMonthTime();
			customerAreaStat.setStartCondition(dates);
			customerAreaStat.setEndCondition(dates);
			customerAreaStat.setNotSign("SIGN");
		}
		if(null!=customerAreaStat.getDeptId() && !customerAreaStat.getDeptId().equals("")){
			//根据部门id到部门表查询该部门以及子部门的id
			List<Object> deptIdList=customerAreaStatService.selectDeptIdForId(customerAreaStat.getDeptId());
			//根据查询的部门id到用户表查询所有的用户工号
			List<Object> list=customerAreaStatService.selectUserJobnumberFordeptid(deptIdList);
//			System.out.println("customerAreaStat = " + customerAreaStat.getDeptId());
			customerAreaStat.setList(list);
		}
	}
}

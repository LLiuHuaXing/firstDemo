package com.sxt.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.*;
import com.sxt.bus.service.*;
import com.sxt.bus.vo.SalesVo;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("sales")
public class SalesController {
	@Autowired
	private SalesService salesService;

	@Autowired
	private CustomerService customerService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private SalesBackService salesBackService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllSales")
	public DataGridView loadAllSales(SalesVo salesVo) {
		IPage<Sales> page = new Page<>(salesVo.getPage(), salesVo.getLimit());
		QueryWrapper<Sales> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(salesVo.getCustomerid()!=null&&salesVo.getCustomerid()!=0,"customerid",salesVo.getCustomerid());
		queryWrapper.eq(salesVo.getGoodsid()!=null&&salesVo.getGoodsid()!=0,"goodsid",salesVo.getGoodsid());
		queryWrapper.ge(salesVo.getStartTime()!=null, "salestime", salesVo.getStartTime());
		queryWrapper.le(salesVo.getEndTime()!=null, "salestime", salesVo.getEndTime());
		queryWrapper.like(StringUtils.isNotBlank(salesVo.getOperateperson()), "operateperson", salesVo.getOperateperson());
		queryWrapper.like(StringUtils.isNotBlank(salesVo.getRemark()), "remark", salesVo.getRemark());
		queryWrapper.orderByDesc("salestime");
		this.salesService.page(page, queryWrapper);
		List<Sales> records = page.getRecords();
		for (Sales sales : records) {
			Customer customer = this.customerService.getById(sales.getCustomerid());
			if(null!=customer) {
				sales.setCustomername(customer.getCustomername());
			}
			Goods goods = this.goodsService.getById(sales.getGoodsid());
			if(null!=goods) {
				sales.setGoodsname(goods.getGoodsname());
			}
		}
		return new DataGridView(page.getTotal(), records);
	}

	/**
	 * 添加
	 */
	@RequestMapping("addSales")
	public ResultObj addSales(@Valid SalesVo salesVo, BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			salesVo.setSalestime(new Date());
			User user=(User) WebUtils.getSession().getAttribute("user");
			salesVo.setOperateperson(user.getName());
            salesVo.setJobnumber(user.getJobnumber());
			this.salesService.save(salesVo);
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateSales")
	public ResultObj updateSales(@Valid SalesVo salesVo, BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			this.salesService.updateById(salesVo);
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}
	/**
	 * 删除
	 */
	@RequestMapping("deleteSales")
	public ResultObj deleteSales(Integer id) {
		try {
			this.salesService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
}


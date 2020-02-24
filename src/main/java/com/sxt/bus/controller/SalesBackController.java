package com.sxt.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.SalesBack;
import com.sxt.bus.domain.Customer;
import com.sxt.bus.service.GoodsService;
import com.sxt.bus.service.SalesBackService;
import com.sxt.bus.service.CustomerService;
import com.sxt.bus.vo.SalesBackVo;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("salesback")
public class SalesBackController {
	
	@Autowired
	private SalesBackService salesBackService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private GoodsService goodsService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllsalesback")
	public DataGridView loadAllSalesBack(SalesBackVo salesbackVo) {
		IPage<SalesBack> page = new Page<>(salesbackVo.getPage(), salesbackVo.getLimit());
		QueryWrapper<SalesBack> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(salesbackVo.getCustomerid()!=null&&salesbackVo.getCustomerid()!=0,"customerid",salesbackVo.getCustomerid());
		queryWrapper.eq(salesbackVo.getGoodsid()!=null&&salesbackVo.getGoodsid()!=0,"goodsid",salesbackVo.getGoodsid());
		queryWrapper.ge(salesbackVo.getStartTime()!=null, "salesbacktime", salesbackVo.getStartTime());
		queryWrapper.le(salesbackVo.getEndTime()!=null, "salesbacktime", salesbackVo.getEndTime());
		queryWrapper.like(StringUtils.isNotBlank(salesbackVo.getOperateperson()), "operateperson", salesbackVo.getOperateperson());
		queryWrapper.like(StringUtils.isNotBlank(salesbackVo.getRemark()), "remark", salesbackVo.getRemark());
		queryWrapper.orderByDesc("salesbacktime");
		this.salesBackService.page(page, queryWrapper);
		List<SalesBack> records = page.getRecords();
		for (SalesBack salesback : records) {
			Customer customer = this.customerService.getById(salesback.getCustomerid());
			if(null!=customer) {
				salesback.setCustomername(customer.getCustomername());
			}
			Goods goods = this.goodsService.getById(salesback.getGoodsid());
			if(null!=goods) {
				salesback.setGoodsname(goods.getGoodsname());
			}
		}
		return new DataGridView(page.getTotal(), records);
	}

	/**
	 * 添加退货信息
	 */
	@RequestMapping("addSalesBack")
	public ResultObj addSalesBack(Integer id,Integer number,String remark) {
		try {
			this.salesBackService.addSalesBack(id,number,remark);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}
	
}


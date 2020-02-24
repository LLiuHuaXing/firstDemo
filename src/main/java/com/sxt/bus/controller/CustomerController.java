package com.sxt.bus.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sxt.bus.domain.Customer;
import com.sxt.sys.common.Constast;
import com.sxt.sys.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Customer;
import com.sxt.bus.service.CustomerService;
import com.sxt.bus.vo.CustomerVo;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllCustomer")
	public DataGridView loadAllCustomer(CustomerVo customerVo) {
		IPage<Customer> page = new Page<>(customerVo.getPage(), customerVo.getLimit());
		QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()), "customername",
				customerVo.getCustomername());
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getPhone()), "telephone", customerVo.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()), "connectionperson",
				customerVo.getConnectionperson());
		this.customerService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}

	/**
	 * 修改客户状态
	 * @param customerVo
	 * @return
	 */
	@RequestMapping("updateCustomerAvailable")
	public ResultObj updateDeptAvailable(CustomerVo customerVo) {
		try {
			//System.out.println(deptVo.getId());
			customerService.updateDeptAvailable(customerVo);
			if(customerVo.getAvailable()==1){
				return ResultObj.AVAILABLE_SUCCESS_NO;
			}else {
				return ResultObj.AVAILABLE_SUCCESS_OFF;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}


	/**
	 * 添加
	 */
	@RequestMapping("addCustomer")
	public ResultObj addCustomer(@Valid CustomerVo customerVo, BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			//System.out.println("bank = " + errorInfo);
            return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			this.customerService.save(customerVo);
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateCustomer")
	public ResultObj updateCustomer(@Valid CustomerVo customerVo,BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			this.customerService.updateById(customerVo);
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteCustomer")
	public ResultObj deleteCustomer(Integer id) {
		try {
			this.customerService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteCustomer")
	public ResultObj batchDeleteCustomer(CustomerVo customerVo) {
		try {
			Collection<Serializable> idList = new ArrayList<Serializable>();
			for (Integer id : customerVo.getIds()) {
				idList.add(id);
			}
			this.customerService.removeByIds(idList);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 加载所有可用的客户
	 */
	@RequestMapping("loadAllCustomerForSelect")
	public DataGridView loadAllCustomerForSelect() {
		QueryWrapper<Customer> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		List<Customer> list = this.customerService.list(queryWrapper);
		return new DataGridView(list);
	}
}

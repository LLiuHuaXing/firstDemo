package com.sxt.bus.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sxt.sys.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.ProviderService;
import com.sxt.bus.vo.ProviderVo;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

	@Autowired
	private ProviderService providerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllProvider")
	public DataGridView loadAllProvider(ProviderVo providerVo) {
		IPage<Provider> page = new Page<>(providerVo.getPage(), providerVo.getLimit());
		QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getProvidername()), "providername",
				providerVo.getProvidername());
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getPhone()), "phone", providerVo.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getConnectionperson()), "connectionperson",
				providerVo.getConnectionperson());
		this.providerService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}

	/**
	 * 添加
	 */
	@RequestMapping("addProvider")
	public ResultObj addProvider(@Valid ProviderVo providerVo, BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			this.providerService.save(providerVo);
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}

	/**
	 * 修改供应商状态
	 * @param providerVo
	 * @return
	 */
	@RequestMapping("updateProviderAvailable")
	public ResultObj updateProviderAvailable(ProviderVo providerVo) {
		try {
			//System.out.println(deptVo.getId());
			providerService.updateProviderAvailable(providerVo);
			if(providerVo.getAvailable()==1){
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
	 * 修改
	 */
	@RequestMapping("updateProvider")
	public ResultObj updateProvider(ProviderVo providerVo) {
		try {
			this.providerService.updateById(providerVo);
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteProvider")
	public ResultObj deleteProvider(Integer id) {
		try {

			this.providerService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteProvider")
	public ResultObj batchDeleteProvider(ProviderVo providerVo) {
		try {
			Collection<Serializable> idList = new ArrayList<Serializable>();
			for (Integer id : providerVo.getIds()) {
				idList.add(id);
			}
			this.providerService.removeByIds(idList);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
	
	
	/**
	 * 加载所有可用的供应商
	 */
	@RequestMapping("loadAllProviderForSelect")
	public DataGridView loadAllProviderForSelect() {
		QueryWrapper<Provider> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		List<Provider> list = this.providerService.list(queryWrapper);
		return new DataGridView(list);
	}
}


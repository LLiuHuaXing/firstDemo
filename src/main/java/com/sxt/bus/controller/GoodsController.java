package com.sxt.bus.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sxt.bus.vo.CustomerVo;
import com.sxt.sys.util.AppFileUtils;
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
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.GoodsService;
import com.sxt.bus.service.ProviderService;
import com.sxt.bus.vo.GoodsVo;
import com.sxt.sys.common.Constast;
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
@RequestMapping("/goods")
public class GoodsController {


	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ProviderService providerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllGoods")
	public DataGridView loadAllGoods(GoodsVo goodsVo) {
		IPage<Goods> page = new Page<>(goodsVo.getPage(), goodsVo.getLimit());
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()), "productcode", goodsVo.getProductcode());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()), "promitcode", goodsVo.getPromitcode());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()), "description", goodsVo.getDescription());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()), "size", goodsVo.getSize());
		this.goodsService.page(page, queryWrapper);
		List<Goods> records = page.getRecords();
		for (Goods goods : records) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidername(provider.getProvidername());
			}
		}
		return new DataGridView(page.getTotal(), records);
	}

	/**
	 * 修改商品状态
	 * @param goodsVo
	 * @return
	 */
	@RequestMapping("updateGoodsAvailable")
	public ResultObj updateGoodsAvailable(GoodsVo goodsVo) {
		try {
			//System.out.println(deptVo.getId());
			goodsService.updateGoodsAvailable(goodsVo);
			if(goodsVo.getAvailable()==1){
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
	@RequestMapping("addGoods")
	public ResultObj addGoods(@Valid GoodsVo goodsVo, BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			if(goodsVo.getGoodsimg()!=null && goodsVo.getGoodsimg().endsWith("_temp")) {
				String newName= AppFileUtils.renameFile(goodsVo.getGoodsimg());
				goodsVo.setGoodsimg(newName);
			}
			this.goodsService.save(goodsVo);
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateGoods")
	public ResultObj updateGoods(@Valid GoodsVo goodsVo, BindingResult bindingResult) {
		//数据校证
		if(bindingResult.hasErrors()){
			String errorInfo="";
			for (FieldError fieldError:bindingResult.getFieldErrors()){
				errorInfo+=fieldError.getDefaultMessage()+"      ";
			}
			return new ResultObj(Constast.ERROR,errorInfo);
		}
		try {
			//说明是不默认图片
			if(!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG))) {
				//说明已经替换图片了
				if(goodsVo.getGoodsimg().endsWith("_temp")) {
					String newName=AppFileUtils.renameFile(goodsVo.getGoodsimg());
					goodsVo.setGoodsimg(newName);
					//删除原先的图片
					String oldPath=this.goodsService.getById(goodsVo.getId()).getGoodsimg();
					AppFileUtils.removeFileByPath(oldPath);
				}
			}
			this.goodsService.updateById(goodsVo);
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteGoods")
	public ResultObj deleteGoods(Integer id,String goodsimg) {
		try {
			//删除原文件
			AppFileUtils.removeFileByPath(goodsimg);
			this.goodsService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteGoods")
	public ResultObj batchDeleteGoods(GoodsVo goodsVo) {
		try {
			Collection<Serializable> idList = new ArrayList<Serializable>();
			for (Integer id : goodsVo.getIds()) {
				idList.add(id);
			}
			System.out.println("goodsVo = " + goodsVo);
			for (String imgPath : goodsVo.getGoodsImgS()) {
					//删除原文件
					AppFileUtils.removeFileByPath(imgPath);
			}
			this.goodsService.removeByIds(idList);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
	
	/**
	 * 加载所有可用的供应商
	 */
	@RequestMapping("loadAllGoodsForSelect")
	public DataGridView loadAllGoodsForSelect() {
		QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		List<Goods> list = this.goodsService.list(queryWrapper);
		//把供应商的名称赋值给providername
		for (Goods goods : list) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidername(provider.getProvidername());
			}
		}
		return new DataGridView(list);
	}
	
	/**
	 *根据供应商ID查询商品信息 
	 */
	@RequestMapping("loadGoodsByProviderId")
	public DataGridView loadGoodsByProviderId(Integer providerid) {
		QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		queryWrapper.eq(providerid!=null, "providerid", providerid);
		List<Goods> list = this.goodsService.list(queryWrapper);
		for (Goods goods : list) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidername(provider.getProvidername());
			}
		}
		return new DataGridView(list);
	}


}

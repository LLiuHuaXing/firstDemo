package com.sxt.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Sales;
import com.sxt.bus.domain.SalesBack;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.SalesBackMapper;
import com.sxt.bus.mapper.SalesMapper;
import com.sxt.bus.service.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2019-09-28
 */
@Service
@Transactional

/**
 *
 *
 */
public class SalesServiceImpl extends ServiceImpl<SalesMapper, Sales> implements SalesService {

	@Resource
	private GoodsMapper goodsMapper;

	@Resource
	private SalesBackMapper salesBackMapper;
	
	@Override
	public boolean save(Sales entity) {
		//根据商品编号查询商品
		Goods goods=goodsMapper.selectById(entity.getGoodsid());
		goods.setNumber(goods.getNumber()-entity.getNumber());
		goodsMapper.updateById(goods);
		//保存进货信息
		return super.save(entity);
	}
	
	@Override
	public boolean updateById(Sales entity) {
		//根据进货ID查询销售
		Sales sales = this.baseMapper.selectById(entity.getId());
		//根据商品ID查询商品信息
		Goods goods = this.goodsMapper.selectById(entity.getGoodsid());
		//库存的算法  当前库存+销售单修改之前的数量-修改之后的数量
		goods.setNumber(goods.getNumber()+sales.getNumber()-entity.getNumber());
		this.goodsMapper.updateById(goods);
		//更新销售单
		return super.updateById(entity);
	}
	
	
	
	@Override
	public boolean removeById(Serializable id) {
		//根据进货ID查询销售
		Sales sales = this.baseMapper.selectById(id);
		//根据商品ID查询商品信息
		Goods goods = this.goodsMapper.selectById(sales.getGoodsid());
		//根据销售商品id删除销售退货信息
		int integer=this.getBaseMapper().selectBysalesId(id);
		//库存的算法  当前库存+销售单数量
		goods.setNumber(goods.getNumber()-integer+sales.getNumber());
		this.goodsMapper.updateById(goods);

		//根据销售商品id删除销售退货信息
		this.getBaseMapper().deleteSalesBackForSalesid(id);
		return super.removeById(id);
	}
	
	
	
	
}

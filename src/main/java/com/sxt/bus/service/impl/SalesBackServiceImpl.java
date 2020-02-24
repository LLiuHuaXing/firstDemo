package com.sxt.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Sales;
import com.sxt.bus.domain.SalesBack;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.SalesMapper;
import com.sxt.bus.mapper.SalesBackMapper;
import com.sxt.bus.service.SalesBackService;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class SalesBackServiceImpl extends ServiceImpl<SalesBackMapper, SalesBack> implements SalesBackService {

	@Resource
	private SalesMapper SalesMapper;
	
	@Resource
	private GoodsMapper goodsMapper;
	
	@Override
	public void addSalesBack(Integer id, Integer number, String remark) {
		//1,根据进货单ID查询销售单信息
		Sales Sales = this.SalesMapper.selectById(id);
		//2,根据商品ID查询商品信息
		Goods goods = this.goodsMapper.selectById(Sales.getGoodsid());
		goods.setNumber(goods.getNumber()+number);
		this.goodsMapper.updateById(goods);
		//3,添加销售退货单信息
		SalesBack entity=new SalesBack();
		entity.setGoodsid(Sales.getGoodsid());
		entity.setNumber(number);
		User user=(User)WebUtils.getSession().getAttribute("user");
		entity.setOperateperson(user.getName());
		entity.setSalesbackprice(Sales.getSalesprice());
		entity.setSalesbacktime(new Date());
		entity.setPaytype(Sales.getPaytype());
		entity.setCustomerid(Sales.getCustomerid());
		entity.setRemark(remark);
		entity.setSalesid(Sales.getId());
		entity.setJobnumber(user.getJobnumber());
		this.getBaseMapper().insert(entity);
	}


}

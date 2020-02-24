package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Goods;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collection;

import com.sxt.bus.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

	
	@Override
	public boolean save(Goods entity) {
		// TODO Auto-generated method stub
		return super.save(entity);
	}
	
	@Override
	public boolean updateById(Goods entity) {
		// TODO Auto-generated method stub
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		// TODO Auto-generated method stub
		return super.removeById(id);
	}
	
	@Override
	public Goods getById(Serializable id) {
		// TODO Auto-generated method stub
		return super.getById(id);
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return super.removeByIds(idList);
	}

	/**
	 * 修改商品状态
	 * @param goodsVo
	 * @return
	 */
	@Override
	public void updateGoodsAvailable(GoodsVo goodsVo) {
		GoodsMapper goodsMapper=this.getBaseMapper();
		goodsMapper.updateGoodsAvailable(goodsVo);
	}
}

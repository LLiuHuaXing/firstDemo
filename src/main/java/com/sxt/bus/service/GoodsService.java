package com.sxt.bus.service;

import com.sxt.bus.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.bus.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface GoodsService extends IService<Goods> {

    void updateGoodsAvailable(GoodsVo goodsVo);
}

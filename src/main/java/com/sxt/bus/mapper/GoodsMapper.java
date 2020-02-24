package com.sxt.bus.mapper;

import com.sxt.bus.domain.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.bus.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 修改商品状态
     * @param goodsVo
     * @return
     */
    void updateGoodsAvailable(@Param("goodsVo") GoodsVo goodsVo);
}

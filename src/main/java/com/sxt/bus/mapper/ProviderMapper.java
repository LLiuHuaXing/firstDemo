package com.sxt.bus.mapper;

import com.sxt.bus.domain.Provider;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.bus.vo.ProviderVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface ProviderMapper extends BaseMapper<Provider> {

    void updateProviderAvailable(@Param("providerVo") ProviderVo providerVo);
}

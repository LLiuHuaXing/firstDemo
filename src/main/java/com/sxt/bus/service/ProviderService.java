package com.sxt.bus.service;

import com.sxt.bus.domain.Provider;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.bus.vo.ProviderVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface ProviderService extends IService<Provider> {

    void updateProviderAvailable(ProviderVo providerVo);
}

package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Provider;
import com.sxt.bus.mapper.ProviderMapper;
import com.sxt.bus.service.ProviderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collection;

import com.sxt.bus.vo.ProviderVo;
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
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements ProviderService {
	
	@Override
	public boolean save(Provider entity) {
		return super.save(entity);
	}
	@Override
	public boolean updateById(Provider entity) {
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	
	@Override
	public Provider getById(Serializable id) {
		return super.getById(id);
	}
	
	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return super.removeByIds(idList);
	}

	/**
	 * 修改供应商状态
	 * @param providerVo
	 * @return
	 */
	@Override
	public void updateProviderAvailable(ProviderVo providerVo) {
	   ProviderMapper providerMapper=this.getBaseMapper();
	   providerMapper.updateProviderAvailable(providerVo);
	}
}

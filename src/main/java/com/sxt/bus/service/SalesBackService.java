package com.sxt.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.bus.domain.SalesBack;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface SalesBackService extends IService<SalesBack> {

	/**
	 * 退货
	 * @param id  进货单ID
	 * @param number  退货数量
	 * @param remark  备注
	 */
	void addSalesBack(Integer id, Integer number, String remark);

}

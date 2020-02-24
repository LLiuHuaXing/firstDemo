package com.sxt.bus.service;

import com.sxt.bus.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.bus.vo.CustomerVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface CustomerService extends IService<Customer> {

    void updateDeptAvailable(CustomerVo customerVo);
}

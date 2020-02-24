package com.sxt.bus.mapper;

import com.sxt.bus.domain.Inport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface InportMapper extends BaseMapper<Inport> {

    int selectByInportid(Serializable id);

    void deleteOutputForInportid(Serializable id);
}

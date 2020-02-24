package com.sxt.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.sys.domain.Loginfo;
import com.sxt.sys.mapper.LoginfoMapper;
import com.sxt.sys.service.LoginfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class LoginfoServiceImpl extends ServiceImpl<LoginfoMapper, Loginfo> implements LoginfoService {

}

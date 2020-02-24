package com.sxt.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.mapper.DeptMapper;
import com.sxt.sys.service.DeptService;
import com.sxt.sys.vo.DeptVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
	
	@Override
	public Dept getById(Serializable id) {
		return super.getById(id);
	}
	
	@Override
	public boolean updateById(Dept entity) {
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	
	@Override
	public boolean save(Dept entity) {
		return super.save(entity);
	}

	/*
	* 加载最大的排序码
	 */
	@Override
	public Integer getDeptMax() {
		DeptMapper deptMapper=this.getBaseMapper();
		return deptMapper.getDeptMax();
	}

	@Override
	public void updateDeptAvailable(DeptVo deptVo) {
		DeptMapper deptMapper=this.getBaseMapper();
		deptMapper.updateDeptAvailable(deptVo);
	}

	/*根据用户名到用户表查部门id
      根据部门id到部门表查该部门的下属部门id
     */
	@Override
	public List<Integer> showDeptIdfFromUname(Integer deptId) {
		return this.getBaseMapper().showDeptIdfFromUname(deptId);
	}
}

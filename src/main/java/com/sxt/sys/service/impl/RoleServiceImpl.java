package com.sxt.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.sys.domain.Role;
import com.sxt.sys.mapper.RoleMapper;
import com.sxt.sys.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	
	@Override
	public boolean removeById(Serializable id) {
		//根据角色ID删除sys_role_permission
		this.getBaseMapper().deleteRolePermissionByRid(id);
		//根据角色ID删除sys_role_user
		this.getBaseMapper().deleteRoleUserByRid(id);
		return super.removeById(id);
	}

	/**
	 * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
	 */
	@Override
	public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
		return this.getBaseMapper().queryRolePermissionIdsByRid(roleId);
	}

	/**
	 * 保存角色和菜单权限之间的关系
	 */
	@Override
	public void saveRolePermission(Integer rid, Integer[] ids) {
		RoleMapper roleMapper = this.getBaseMapper();
		//根据rid删除sys_role_permission
		roleMapper.deleteRolePermissionByRid(rid);
		//可以使用mybatis的批量插入
		if(ids!=null&&ids.length>0) {
			for (Integer pid : ids) {
				roleMapper.saveRolePermission(rid,pid);
			}
		}
	}

	/**
	 * 查询当前用户拥有的角色ID集合
	 */
	@Override
	public List<Integer> queryUserRoleIdsByUid(Integer id) {
		return this.getBaseMapper().queryUserRoleIdsByUid(id);
	}

	/*
	*查询角色名称，保存该用户所有的角色名称
	*/
	@Override
	public String queryRoleNameByListId(Integer rid) {
		return this.getBaseMapper().queryRoleNameByListId(rid);
	}
}

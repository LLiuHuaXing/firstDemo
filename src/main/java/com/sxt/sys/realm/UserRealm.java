package com.sxt.sys.realm;

import com.sxt.sys.common.ActiverUser;
import com.sxt.sys.common.Constast;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.service.PermissionService;
import com.sxt.sys.service.RoleService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.UserService;
import org.springframework.context.annotation.Lazy;

import java.util.*;

public class UserRealm extends AuthorizingRealm {

	@Autowired
	@Lazy //只有使用时才会加载
	private UserService userService;

	@Autowired
	@Lazy
	private PermissionService permissionService;

	@Autowired
	@Lazy
	private RoleService roleService;

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //https://blog.csdn.net/caoyang0105/article/details/82769293
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		/*根据用户名是否为null判断是手机登录还是密码登录*/
		if(token instanceof MyToken){
			queryWrapper.eq("mobile",token.getPrincipal().toString());
			User user = userService.getOne(queryWrapper);
			if (null != user) {
				ActiverUser activerUser = returnActiverUser(user);
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getMobile(),this.getName());
				return info;
			}
		}else{
			//用户名和密码登录
			queryWrapper.eq("loginname", token.getPrincipal().toString());
			User user = userService.getOne(queryWrapper);
			if (null != user) {
				ActiverUser activerUser = returnActiverUser(user);
				ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser, user.getPassword(), credentialsSalt,this.getName());
				return info;
			}
		}

		return null;
	}

	/*
	* 返回ActiverUser
	* */
	public ActiverUser returnActiverUser(User user){
		ActiverUser activerUser = new ActiverUser();
		activerUser.setUser(user);
		//根据用户ID查询percode
		//查询所有菜单
		QueryWrapper<Permission> qw=new QueryWrapper<>();
		//设置只能查询菜单的权限
		qw.eq("type", Constast.TYPE_PERMISSION);
		qw.eq("available", Constast.AVAILABLE_TRUE);

		//根据用户ID+角色+权限去查询
		Integer userId=user.getId();
		//根据用户ID查询角色
		List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(userId);
		//查询角色名称，保存该用户所有的角色名称
		List<String> roleNameList =new ArrayList<>();


		//根据角色ID取到权限和菜单ID
		Set<Integer> pids=new HashSet<>();
		for (Integer rid : currentUserRoleIds) {
			List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
			pids.addAll(permissionIds);
			//查询角色名称
			String roleName = roleService.queryRoleNameByListId(rid);
			roleNameList.add(roleName);
		}
		//保存该用户所有的角色名称
		activerUser.setRoles(roleNameList);

		List<Permission> list=new ArrayList<>();
		//根据菜单ID查询权限
		if(pids.size()>0) {
			qw.in("id", pids);
			list=permissionService.list(qw);
		}
		List<String> percodes=new ArrayList<>();
		for (Permission permission : list) {
			percodes.add(permission.getPercode());
		}
		//查到所有的权限放到activerUser
		activerUser.setPermissions(percodes);
		return activerUser;
	}


	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
		ActiverUser activerUser=(ActiverUser) principals.getPrimaryPrincipal();
		User user=activerUser.getUser();
		List<String> permissions = activerUser.getPermissions();
		List<String> roles = activerUser.getRoles();
		if(user.getType()==Constast.USER_TYPE_SUPER) {
			authorizationInfo.addStringPermission("*:*");
		}else {
			if(null!=permissions&&permissions.size()>0) {
				authorizationInfo.addStringPermissions(permissions);
				authorizationInfo.addRoles(roles);
			}
		}
		return authorizationInfo;
	}

}

package com.sxt.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.mapper.PermissionMapper;
import com.sxt.sys.service.PermissionService;
import com.sxt.sys.vo.DeptVo;
import com.sxt.sys.vo.PermissionVo;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public void updateMenuAvailable(PermissionVo permissionVo) {
       PermissionMapper permissionMapper= this.getBaseMapper();
       permissionMapper.updateMenuAvailable(permissionVo);
    }

    @Override
    public boolean removeById(Serializable id) {
        PermissionMapper permissionMapper = this.getBaseMapper();
        //根据权限或菜单ID删除权限表各和角色的关系表里面的数据
        permissionMapper.deleteRolePermissionByPid(id);
        return super.removeById(id);//删除 权限表的数据
    }
}

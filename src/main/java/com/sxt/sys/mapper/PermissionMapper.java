package com.sxt.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.vo.DeptVo;
import com.sxt.sys.vo.PermissionVo;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 *
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    void updateMenuAvailable(@Param("permissionVo") PermissionVo permissionVo);

    void deleteRolePermissionByPid(Serializable id);
}

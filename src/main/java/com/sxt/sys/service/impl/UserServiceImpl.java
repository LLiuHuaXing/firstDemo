package com.sxt.sys.service.impl;

import com.sxt.sys.common.ActiverUser;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.Role;
import com.sxt.sys.domain.User;
import com.sxt.sys.mapper.RoleMapper;
import com.sxt.sys.mapper.UserMapper;
import com.sxt.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.sys.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    @Resource
    private RoleMapper roleMapper;

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        //根据用户ID删除用户角色中间表的数据
        roleMapper.deleteRoleUserByUid(id);
        //删除用户头[如果是默认头像不删除  否则删除]
        return super.removeById(id);
    }

    @Override
    public String saveUserRole(Integer uid, Integer[] ids,String[] names) {
        //第一种根据该用户所拥有的角色显示角色分配的页面
        //第二种方法：判断登录用户所分配角色是否是自己所拥有的角色
        Subject currentUser = SecurityUtils.getSubject();
        ActiverUser activerUser = (ActiverUser) currentUser.getPrincipal();
        List<String> roles = activerUser.getRoles();

        String tipsInfo="";

        for (String oldRoleName:names){
//            if(currentUser.hasRole(oldRoleName)){
//                System.out.println("===========第二种方法：判断登录用户所分配角色是否是自己所拥有的角色");
//            }
            boolean flag=false;
            for (String roleName:roles){
                if(roleName.equals(oldRoleName)){
                    flag=true;
                    break;
                }
            }
            if(!flag){
                tipsInfo+=oldRoleName;
            }
        }
        if(tipsInfo==""){
            //根据用户ID删除sys_role_user里面的数据
            this.roleMapper.deleteRoleUserByUid(uid);
            //保存角色和用户的关系
            if(null!=ids&&ids.length>0) {
                for (Integer rid : ids) {
                    this.roleMapper.insertUserRole(uid,rid);
                }
            }
            return null;
        }
        return tipsInfo;
    }

    /*更新数据库中用户头像的图片路径*/
    @Override
    public void updateUserPicture (String path,Integer id) {
        this.getBaseMapper().updateUserPicture(path,id);
    }

    /*查更新数据库之前用户头像的图片路径*/
    @Override
    public String showUserPicture(Integer id) {
        return this.getBaseMapper().showUserPicture(id);
    }

    /*
     * 根据用户id查用户名
     *
     */
    @Override
    public String showLeaveUsername(Integer userid) {
        return this.getBaseMapper().showLeaveUsername(userid);
    }

    /*
    * 根据部门id到用户表查用户id
    * */
    @Override
    public List<Integer> showUserIdFromdeptId(List<Integer> listDeptId) {
        return this.getBaseMapper().showUserIdFromdeptId(listDeptId);
    }

    /**查人事部的所有员工的名字*/
    @Override
    public List<String> showAllHuman(String allHuman) {
        return this.getBaseMapper().showAllHuman(allHuman);
    }

}

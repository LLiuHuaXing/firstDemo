package com.sxt.sys.service;

import com.sxt.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.sys.vo.UserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface UserService extends IService<User> {

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    String saveUserRole(Integer uid, Integer[] ids,String[] names);

    /**更新数据库中用户头像的图片路径*/
    void updateUserPicture(String path,Integer id);

    /**查更新数据库之前用户头像的图片路径*/
    String showUserPicture(Integer id);

    /**
     * 根据用户id查用户名
     * */
    String showLeaveUsername(Integer userid);

    /**
    * 根据部门id到用户表查用户id
    * */
    List<Integer> showUserIdFromdeptId(List<Integer> listDeptId);

    /**查人事部的所有员工的名字*/
    List<String> showAllHuman(String allHuman);
}

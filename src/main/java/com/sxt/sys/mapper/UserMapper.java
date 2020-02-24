package com.sxt.sys.mapper;

import com.sxt.sys.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.sys.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface UserMapper extends BaseMapper<User> {

    void updateUserPicture(@Param("path") String path,@Param("id") Integer id);

    String showUserPicture(@Param("id") Integer id);

    String showLeaveUsername(Integer userid);

    List<Integer> showUserIdFromdeptId(List<Integer> listDeptId);

    List<String> showAllHuman(String allHuman);
}

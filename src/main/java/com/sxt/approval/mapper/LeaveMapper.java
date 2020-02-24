package com.sxt.approval.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.approval.damain.Leave;
import com.sxt.approval.vo.LeaveVo;
import com.sxt.sys.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface LeaveMapper extends BaseMapper<Leave> {


    List<Leave> showLeaveFromLeave(@Param("leaveVo") LeaveVo leaveVo,@Param("listUserId") List<Integer> listUserId);

    void submissionLeave(@Param("id")Integer id,@Param("newState") int newState);
}

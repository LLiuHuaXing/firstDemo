package com.sxt.approval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.approval.damain.Leave;
import com.sxt.approval.vo.LeaveVo;
import com.sxt.sys.domain.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface LeaveService extends IService<Leave> {


    /*根据用户id和查询条件到请假表查请假信息*/
    List<Leave> showLeaveFromLeave(LeaveVo leaveVo, List<Integer> listUserId);

    /*提交请假单*/
    void submissionLeave(Integer id, int newState);
}

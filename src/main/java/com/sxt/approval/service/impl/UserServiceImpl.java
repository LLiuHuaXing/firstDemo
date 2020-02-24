package com.sxt.approval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.approval.damain.Leave;
import com.sxt.approval.mapper.LeaveMapper;
import com.sxt.approval.service.LeaveService;
import com.sxt.approval.vo.LeaveVo;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {

    /*根据用户id和查询条件到请假表查请假信息*/
    @Override
    public List<Leave> showLeaveFromLeave(LeaveVo leaveVo, List<Integer> listUserId) {
        return this.getBaseMapper().showLeaveFromLeave(leaveVo, listUserId);
    }

    /*提交请假单*/
    @Override
    public void submissionLeave(Integer id, int newState) {
        this.getBaseMapper().submissionLeave(id, newState);
    }
}


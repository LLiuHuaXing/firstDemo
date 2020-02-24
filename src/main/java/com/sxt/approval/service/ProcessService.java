package com.sxt.approval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.approval.damain.Leave;
import com.sxt.approval.vo.LeaveVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface ProcessService  {

    /**查Deployment表的息*/
    List<Map<String, Object>> loadAllProcess(String processName);

    /**请假单act_re_procdef表查询l流程信息*/
    List<Map<String, Object>> loadAllProcdef(List<Object> deployment);

    /**上传流程部署文件*/
    void saveProcessData(List<Map<String, Object>> saveMysql);

    /**查看所有做好的流程(保存已部署和为部署)*/
    List<Map<String, Object>> deployProcessTable(String deployName, String variable);

    /**修改流程的状态*/
    void updateProcessState(String id,int deploy);
}

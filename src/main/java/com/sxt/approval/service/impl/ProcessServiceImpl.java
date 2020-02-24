package com.sxt.approval.service.impl;

import com.sxt.approval.mapper.ProcessMapper;
import com.sxt.approval.service.ProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
class ProcessServiceImpl implements ProcessService {

    @Resource
    private ProcessMapper processMapper;

    /*查Deployment表的息*/
    @Override
    public List<Map<String, Object>> loadAllProcess(String processName) {
        return this.processMapper.loadAllProcess(processName);
    }

    /**请假单act_re_procdef表查询l流程信息*/
    @Override
    public List<Map<String, Object>> loadAllProcdef(List<Object> deployment) {
        return this.processMapper.loadAllProcdef(deployment);
    }

    /**上传流程部署文件*/
    @Override
    public void saveProcessData(List<Map<String, Object>> saveMysql) {
        this.processMapper.saveProcessData(saveMysql);
    }

    /**查看所有做好的流程(保存已部署和为部署)*/
    @Override
    public List<Map<String, Object>> deployProcessTable(String deployName, String variable) {
        return this.processMapper.deployProcessTable(deployName,variable);
    }

    /**修改流程的状态*/
    @Override
    public void updateProcessState(String id,int deploy) {
        this.processMapper.updateProcessState(id,deploy);
    }
}


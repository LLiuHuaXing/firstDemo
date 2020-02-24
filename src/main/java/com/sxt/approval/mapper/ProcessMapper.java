package com.sxt.approval.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
@Mapper
public interface ProcessMapper {

    /**查Deployment表的息*/
    List<Map<String, Object>> loadAllProcess(@Param("processName") String processName);

    /**请假单act_re_procdef表查询l流程信息*/
    List<Map<String, Object>> loadAllProcdef(List<Object> deployment);

    /**上传流程部署文件*/
    void saveProcessData(List<Map<String, Object>> saveMysql);

    /**查看所有做好的流程(保存已部署和为部署)*/
    List<Map<String, Object>> deployProcessTable(@Param("deployName") String deployName,@Param("variable") String variable);

    /**修改流程的状态*/
    void updateProcessState(@Param("id") String id,@Param("deploy") int deploy);
}

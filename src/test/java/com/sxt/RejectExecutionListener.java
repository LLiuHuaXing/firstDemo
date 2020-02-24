package com.sxt;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.List;

//驳回功能
public class RejectExecutionListener implements ExecutionListener {
    private static final long serialVersionUID = 1L;
    @Override
    public void notify(DelegateExecution execution) {
//        EngineServices services = execution.getEngineServices();
//        ExecutionEntity entity = (ExecutionEntity) execution;
//        String  processInstId=entity.getProcessInstanceId();          //流程实例Id
//        TransitionImpl transitionImpl = (TransitionImpl) entity.getEventSource();
//        ActivityImpl actimpl = transitionImpl.getDestination();
//        String   taskDefKey= actimpl.getId();                          //task1
//        List<HistoricTaskInstance> list=services.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().desc()
//                .processInstanceId(processInstId)
//                .taskDefinitionKey(taskDefKey)
//                .list();
//        if(list!=null){
//            String user=list.get(0).getAssignee(); //获取最新的一个责任人信息回退给他
//            execution.setVariable(IBaseFlow.SEND_DOC_INPUTUSER, user);
//        }
    }
}


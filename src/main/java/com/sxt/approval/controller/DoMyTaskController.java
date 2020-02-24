package com.sxt.approval.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.approval.service.DoMyTaskService;
import com.sxt.approval.service.ProcessService;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.User;
import com.sxt.sys.util.AppFileUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/process")
public class DoMyTaskController {

     @Resource
     private ProcessService processService;
     @Resource
     private RepositoryService repositoryService;
     @Resource
     private DoMyTaskService doMyTaskService;
     @Resource
     private TaskService taskService;

    /**
     *查询我的所有任务
     */
    @RequestMapping("loadAllMyTask")
    public DataGridView loadAllMyTask() {
        System.out.println("开始查询我的所有任务");
        //封装返回前端的数据
        List<Map<String,Object>> taskList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        //存储流程实例的key
        List<String> listProcessKey=new ArrayList<>();
        User currentUser = (User) WebUtils.getSession().getAttribute(Constast.currentUser);
        //1.获取流程实例的key：查询act_re_procdef表
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processDefinitionQuery.orderByDeploymentId().desc().list();
        //List<ProcessDefinition> list =processDefinitionQuery.deploymentId("b0c7b22d-52ef-11ea-8514-f83441db6753").desc().list();
        //输出流程定义信息
        System.out.println("输出流程定义信息：输出流程定义信息====== 和 开始拾取任务了");
        for(ProcessDefinition processDefinition :list){
            System.out.println("流程定义的Key："+processDefinition.getKey());
            listProcessKey.add(processDefinition.getKey());
           //2拾取任务
            //执行查询
            List<Task> listTask = taskService.createTaskQuery()
                    .processDefinitionKey(processDefinition.getKey())
				//.taskCandidateUser(candidate_users)//设置候选用户
                    .list();
            for(Task task :listTask){
                taskService.claim(task.getId(),currentUser.getName());//第一个参数任务ID,第二个参数为具体的候选用户名
                System.out.println("任务拾取完毕!");
            }
        }
        //3.查询我的任务
        System.out.println("开始查询我的任务了");
        for(String str:listProcessKey){
            List<Task> newList = taskService.createTaskQuery ()
                    //流程实例key
                    .processDefinitionKey (str)
                    //查询谁的任务
                    .taskAssignee(currentUser.getName())
                    .list ();
            //存储任务的id
            List<String> idList = new ArrayList<String>();

            System.out.println ("任务查询:任务查询");
            for (Task task : newList) {
                idList.add (task.getId ());
                //taskService.complete(task.getId());
                map.put("ID_",task.getId());
                map.put("NAME_",task.getName());
                map.put("CREATE_TIME_",task.getCreateTime());
                map.put("ASSIGNEE_",task.getAssignee());
                taskList.add(map);
            }
        }
        //this.doMyTaskService.loadAllMyTask();
        return new DataGridView(taskList);
    }


}


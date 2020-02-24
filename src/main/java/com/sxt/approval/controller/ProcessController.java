package com.sxt.approval.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.approval.service.ProcessService;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.util.AppFileUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
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
public class ProcessController {

     //保存deployment表的ID_
     List<Object> deployment=new ArrayList<>();

     //部署名称的标志
     int processId=0;

     @Resource
     private ProcessService processService;
     @Resource
     private RepositoryService repositoryService;

    /**
     *请假单deployment表查询
     */
    @RequestMapping("loadAllProcess")
    public DataGridView loadAllDeployment(String NAME_) {
        System.out.println("NAME_ == " + NAME_);
        /*每一次清空List<Object> deployment集合的数据*/
        for(int i=0;i<deployment.size();i++){
            Object deleteObj=deployment.get(i);
            deployment.remove(deleteObj);
        }
        /*查Deployment表的息*/
        IPage<Object> page=new Page<>(1,10);
        List<Map<String, Object>> listDeployment=this.processService.loadAllProcess(NAME_);

        for(int i=0;i<listDeployment.size();i++){
            Object obj=listDeployment.get(i).get("ID_");
            deployment.add(obj);
            //System.out.println("processName = " +obj);
        }
        page.setTotal(listDeployment.size());  //表单显示分页栏目
        return new DataGridView(page.getTotal(),listDeployment);
    }

    /**
     *请假单act_re_procdef表查询l流程信息
     */
    @RequestMapping("loadAllProcdef")
    public DataGridView loadAllProcdef() {
        System.out.println("请假单act_re_procdef表查询++++++========");
        IPage<Object> page=new Page<>(1,10);
        List<Map<String, Object>> listProcdef=this.processService.loadAllProcdef(deployment);
        page.setTotal(listProcdef.size());  //表单显示分页栏目
        return new DataGridView(page.getTotal(),listProcdef);

        //3.得到ProcessDefinitionQuery对象,可以认为它就是一个查询器
       /* ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //4.设置条件，并查询出当前的所有流程定义   查询条件：流程定义的key=holiday
        //orderByProcessDefinitionVersion() 设置排序方式,根据流程定义的版本号进行排序
        for(Object obj:deployment){
            System.out.println("procdef = " +obj);
            List<ProcessDefinition> list = processDefinitionQuery.orderByDeploymentId().desc().list();
            //5.输出流程定义信息
            for(ProcessDefinition processDefinition :list){
                System.out.println("流程定义ID："+processDefinition.getId());
                System.out.println("流程定义名称："+processDefinition.getName());
                System.out.println("流程定义的Key："+processDefinition.getKey());
                System.out.println("流程定义的版本号："+processDefinition.getVersion());
                System.out.println("流程部署的ID:"+processDefinition.getDeploymentId());
                System.out.println("流程部署的时间D:"+processDefinition.getTenantId());

            }
        }*/
    }

    /**
     *请假单deployment表查询
     */
//    @RequestMapping("addProcess")
//    public ResultObj addProcess(String processName,MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("   NAME_ == " + processName);
//        System.out.println(ProcessesFilePath +" ProcessesFilePath文件名称"+file.getOriginalFilename());
//        return ResultObj.OPERATE_SUCCESS;
//    }

    /**
     *上传流程部署文件
    * */
    @RequestMapping(value="userProfileUpdate",method = RequestMethod.POST)
    public ResultObj uploadProcessFile(String processName,@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        /**思路：原因每上传一个文件都会发一次请求
         * 一:用list存储保存数据，在文件上传成功或者关闭窗口时，在一次请求保存数据到数据库。
         * 二：每上传一个文件直接保存到数据库，如果后缀为bpmn才保存，增加一个后缀为png(到时部署时根据文件名去查询png和bpmn)*/

        //保存文件基本路径
        String path = AppFileUtils.PROCESSES_PATH;
        //保存文件文件夹
        String Folder=AppFileUtils.PROCESSES_FOLDER;
        //上传的文件名
        String fileName=file.getOriginalFilename();
        //保存存储到数据库的数据
        List<Map<String,Object>> saveMysql=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();

        //判断路径是否存在，没有就创建一个
        File filepath = new File(path,Folder);
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        //保存文件
        String newPath=path+Folder+"/"+fileName;
        File tempFile = new File(newPath);
        try{
            file.transferTo(tempFile);
        }catch (IOException e){
            return ResultObj.OPERATE_ERROR;
        }

        //文件后缀为bpmn才保存
        if(fileName.endsWith("bpmn")){
            String[] newProcessName = processName.split(",");
            //设置数据,bpmn、png、部署名称
            map.put("filenamebpmn",fileName);
            String fileNamePng=fileName.substring(0,fileName.indexOf("."))+".png";
            map.put("filenamepng",fileNamePng);
            map.put("definitionname",newProcessName[processId]);
            map.put("variable",0);
            saveMysql.add(map);
            processId++;

            //保存数据到数据库库
            this.processService.saveProcessData(saveMysql);
        }
        System.out.println(processName+" "+" 文件名称==="+file.getOriginalFilename()+"  "+file.getSize());
        return ResultObj.OPERATE_SUCCESS;

    }

    /**
     *查看所有做好的流程(保存已部署和为部署)
     */
    @RequestMapping("deployProcessTable")
    public DataGridView deployProcessTable(String deployName,String variable) {
        IPage<Object> page=new Page<>(1,10);
        List<Map<String, Object>> listDeployProcess=this.processService.deployProcessTable(deployName,variable);
        page.setTotal(listDeployProcess.size());  //表单显示分页栏目
        return new DataGridView(page.getTotal(),listDeployProcess);
    }

    /**
     *部署流程
     */
    @RequestMapping("startDeployProcess")
    public ResultObj startDeployProcess(String id,String definitionname,String filenamebpmn,String filenamepng) {
        System.out.println("definitionname = " + definitionname + ", filenamebpmn = " + filenamebpmn + ", filenamepng = " + filenamepng);
        String basePath= Constast.PROCESS_FILEPATH;
        //流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(basePath+filenamebpmn)  //添加bpmn资源
                .addClasspathResource(basePath+filenamepng)
                .name(definitionname)
                .deploy();
        //修改流程的状态
        this.processService.updateProcessState(id,Constast.deploy);
        //4.输出部署的一些信息
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());
        return ResultObj.DEPLOYMENT_SUCCESS;
    }
}


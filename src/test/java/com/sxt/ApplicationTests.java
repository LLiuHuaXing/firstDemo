package com.sxt;

import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessRuntime processRuntime;
	@Autowired
	private HistoryService historyService;


	/**
	 * 部署流程
	 */
	@Test
	public void prepare() {
		// 创建一个部署对象
		Deployment deployment = repositoryService.createDeployment ()
				.name ("请假流程_6")
				.addClasspathResource ("processes/leaveProcesses6.bpmn")
				.addClasspathResource ("processes/leaveProcesses4.png")
				.deploy ();
		System.out.println ("部署ID：" + deployment.getId ());
		System.out.println ("部署名称：" + deployment.getName ());
	}

	/**
	 * 注意事项：
	 *     1.当我们正在执行的这一套流程没有完全审批结束的时候，此时如果要删除流程定义信息就会失败
	 *     2.如果公司层面要强制删除,可以使用repositoryService.deleteDeployment("1",true);
	 *     //参数true代表级联删除，此时就会先删除没有完成的流程结点，最后就可以删除流程定义信息  false的值代表不级联
	 *
	 *
	 * 删除流程实例
	 */
	@Test
	public void deleteProcessesDinition(){
		//1.得到ProcessEngine对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		//2.创建RepositoryService对象
		RepositoryService repositoryService = processEngine.getRepositoryService();

		//3.执行删除流程定义  参数代表流程部署的id
		repositoryService.deleteDeployment("f5d68a15-525f-11ea-8429-f83441db6753");
		System.out.println("删除流程实例成功======================：");
	}

	/**
	 * 查看流程定义
	 */
	@Test
	public void showProcess() {
		//3.得到ProcessDefinitionQuery对象,可以认为它就是一个查询器
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

		//4.设置条件，并查询出当前的所有流程定义   查询条件：流程定义的key=holiday
		//orderByProcessDefinitionVersion() 设置排序方式,根据流程定义的版本号进行排序
//		List<ProcessDefinition> list =processDefinitionQuery.processDefinitionKey("myLeaveProcess_1").orderByProcessDefinitionVersion()
//				.desc()
//				.list();
		List<ProcessDefinition> list = processDefinitionQuery.orderByDeploymentId().desc().list();

		//List<ProcessDefinition> list =processDefinitionQuery.deploymentId("b0c7b22d-52ef-11ea-8514-f83441db6753").desc().list();

		//5.输出流程定义信息
		System.out.println("输出流程定义信息：输出流程定义信息======");
		for(ProcessDefinition processDefinition :list){
			System.out.println("流程定义ID："+processDefinition.getId());
			System.out.println("流程定义名称："+processDefinition.getName());
			System.out.println("流程定义的Key："+processDefinition.getKey());
			System.out.println("流程定义的版本号："+processDefinition.getVersion());
			System.out.println("流程部署的========ID:"+processDefinition.getDeploymentId());
			System.out.println("流程部署的时间==========D:"+processDefinition.getTenantId());


		}
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void testStartProcess() {
		/*第一种*/
//		ProcessInstance instance = processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey("myProcess_1")
//				.build());
		/*第二种*/
		Map<String,Object> map=new HashMap<>();
		map.put("leaveuser","zhangsan6");
		map.put("deptManagerUser_1","liu1,liu2,liu3");
		map.put("gernealmanager","wangwu");
		map.put("pensonachiva","zhaoliu");

		//ProcessInstance instance = runtimeService.startProcessInstanceByKey("myLeaveProcess_1", String.valueOf(map));
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("myLeaveProcess_6", map);
		System.out.println ("启动流程实例:============");
		System.out.println ("流程实例ID:" + instance.getId ());
		System.out.println ("流程定义ID:" + instance.getProcessDefinitionId ());
	}


	/**
	 * 任务查询
	 */
	@Test
	public void searchTask() {
		//流程启动后，各各任务的负责人就可以查询自己当前需要处理的任务，查询出来的任务都是该用户的待办任务。
		List<Task> list = taskService.createTaskQuery ()
				//流程实例key
				.processDefinitionKey ("myLeaveProcess_5")
				//查询谁的任务
				.taskAssignee("li2")
				.list ();
		List<String> idList = new ArrayList<String>();

		System.out.println ("任务查询:任务查询");
		for (Task task : list) {
			idList.add (task.getId ());
            taskService.complete(task.getId());
			System.out.println ("任务ID:" + task.getId ());
			System.out.println ("任务名称:" + task.getName ());
			System.out.println ("任务的创建时间:" + task.getCreateTime ());
			System.out.println ("任务的办理人:" + task.getAssignee ());
			System.out.println ("流程实例ID：" + task.getProcessInstanceId ());
			System.out.println ("执行对象ID:" + task.getExecutionId ());
			System.out.println ("流程定义ID:" + task.getProcessDefinitionId ());
		}

//		return idList;

	}

	//4.查询候选用户的组任务
	@Test
	public void showGroupTask(){

		//3.设置一些参数，流程定义的key,候选用户
		String key = "myLeaveProcess_5";
		String candidate_users="li2";

		//4.执行查询
		List<Task> list = taskService.createTaskQuery()
				.processDefinitionKey(key)
				.taskCandidateUser(candidate_users)//设置候选用户
				.list();
		//5.输出
		for(Task task :list){
			System.out.println(task.getProcessInstanceId());
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getAssignee());//为null，说明当前的zhangsan只是一个候选人，并不是任务的执行人
		}
	}

	//5.测试zhangsan用户，来拾取组任务
	//抽取任务的过程就是将候选用户转化为真正任务的负责人（让任务的assignee有值）
	@Test
	public void selsctTask(){
		System.out.println("开始拾取任务了");
		//3.设置一些参数，流程定义的key,候选用户
		String key = "myLeaveProcess_5";
		String candidate_users="li2";

		//4.执行查询
		List<Task> listTask = taskService.createTaskQuery()
				.processDefinitionKey(key)
//				.taskCandidateUser(candidate_users)//设置候选用户
		        .list();
		for(Task task :listTask){
			taskService.claim(task.getId(),candidate_users);//第一个参数任务ID,第二个参数为具体的候选用户名
			System.out.println("任务拾取完毕!");
		}


	}

	/*任务交接*/
	@Test
	public void currentUserFromTaskAssignee(){
		System.out.println("开始拾取任务了");
		//3.设置一些参数，流程定义的key,候选用户
		String key = "myLeaveProcess_5";
		String candidate_users="li2";

		//4.执行查询
		List<Task> listTask = taskService.createTaskQuery()
				.processDefinitionKey(key)
//				.taskCandidateUser(candidate_users)//设置候选用户
				.list();
		for(Task task :listTask){
//			taskService.setAssignee(task.getId(),"lisi");//交接任务为lisi  ,交接任务就是一个候选人拾取用户的过程
            taskService.claim(task.getId(),null); //个人任务归还组任务
			System.out.println("任务拾取完毕!");
		}
	}


	@Test
	public void contextLoads() {
	}

}

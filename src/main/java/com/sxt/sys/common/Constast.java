package com.sxt.sys.common;

/**
 * 常量接口
 * @author LJH
 *
 */
public interface Constast {

	/**
	 * 状态码
	 *
	 */
	public static final Integer OK=200;
	public static final Integer ERROR=-1;

	/**
	 * 数据统计
	 */
	public static final String DATA_ERROR="查询数据不存在";
	public static final String PASSWORD_ERROR="密码不正确";

	/**
	 * 用户默认密码
	 */
	public static final String USER_DEFAULT_PWD="123456";

	/**
	 * 菜单权限类型
	 */
	public static final String TYPE_MNEU = "menu";
	public static final String TYPE_PERMISSION = "permission";
	/**
	 * 可以状态
	 */
	public static final Object AVAILABLE_TRUE = 1;
	public static final Object AVAILABLE_FALSE = 0;

	/**
	 * 用户类型
	 */
	public static final Integer USER_TYPE_SUPER = 0;
	public static final Integer USER_TYPE_NORMAL = 1;

	/**
	 * 用户头像
	 */
	public static final String USER_PHONE = "图片上传成功";

	/**
	 * 展开类型
	 */
	public static final Integer OPEN_TRUE = 1;
	public static final Integer OPEN_FALSE = 0;

	/**
	 * 商品默认图片
	 */
	public static final String IMAGES_DEFAULTGOODSIMG_PNG = "images/defaultgoodsimg.jpg";

	//部署文件的相对路径
	public static final String  PROCESS_FILEPATH="processes/";

	/*请假单的状态码:未提交、提交、审批、放弃*/
	public static final int  UnSubmitState=0;
	public static final int  SubmitState=1;
	public static final int  SubmitStateState=2;
	public static final int  giveUpState=3;

	//存储当前用户信息的key
	public static final String  currentUser="user";

	//查人事部的所有员工的名字的变量
	public static final String  allHuman="人事部";
	//请假流程的名称
	public static final String  ProcessesName="myCompleteProcess_1";

	/*流程部署的状态：部署、未部署*/
	public static final int  unDeploy=0;
	public static final int  deploy=1;

}

package com.sxt.approval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 审批管理的路由器
 * @author LJH
 *
 */
@Controller
@RequestMapping("/approval")
public class ApprovalController {

	/**
	 * 跳转到请假单管理
	 *
	 */
	@RequestMapping("toLeaveManager")
	public String toLeaveManager() {
		return "approval/leaveManager";
	}

	/**
	 * 跳转到我待审批内容
	 *
	 */
	@RequestMapping("toMyprocessManager")
	public String toMyprocessManager() {
		return "approval/doMyTask";
	}

	/**
	 * 跳转到流程管理
	 *
	 */
	@RequestMapping("toProcessManager")
	public String toProcessManager() {
		return "approval/processManager";
	}
}

package com.sxt.approval.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.approval.damain.Leave;
import com.sxt.approval.service.LeaveService;
import com.sxt.approval.vo.LeaveVo;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.DeptService;
import com.sxt.sys.service.UserService;
import com.sxt.sys.vo.UserVo;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/leave")
public class LeaveController {

    @Resource
    private LeaveService leaveService;
    @Resource
    private UserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private RuntimeService runtimeService;


    /**
     *请假单查询
     */
    @RequestMapping("loadAllLeave")
    public DataGridView loadAllLeave(LeaveVo leaveVo) {

        /*
         *判断是普通员工还是管理层员工
         * 根据到部门表查到部门id的个数：个数1为普通用户，否则为管理员
         */
        boolean flag=false;

        /*
        * 思路：
        * 1.根据用户名到用户表查部门id
        * 2.根据部门id到部门表查该部门的下属部门id
        * 3.根据部门id到用户表查用户id(+当前用户id)
        * 4.根据用户id到请假表查请假信息
        * */
        //根据用户名到用户表查部门id
        //根据部门id到部门表查该部门的下属部门id
        User currentUser= (User) WebUtils.getSession().getAttribute("user");
        //System.out.println("leaveVo = ========" + currentUser.getId());
        List<Integer> listDeptId= this.deptService.showDeptIdfFromUname(currentUser.getDeptid());

        //根据部门id到用户表查用户id(+当前用户id)
        //System.out.println("listDeptId = ========" + listDeptId.size());
        List<Integer> listUserId=this.userService.showUserIdFromdeptId(listDeptId);
        listUserId.add(currentUser.getId());
        // 根据用户id和查询条件到请假表查请假信息
        // System.out.println("listUserId = ========" + listUserId.size());
        // List<Leave> listLeave=this.leaveService.showLeaveFromLeave(leaveVo,listUserId);

        IPage<Leave> page=new Page<>(leaveVo.getPage(), leaveVo.getLimit());
        QueryWrapper<Leave> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(leaveVo.getTitle()), "title", leaveVo.getTitle());
        queryWrapper.eq(StringUtils.isNotBlank(leaveVo.getContent()), "content", leaveVo.getContent());
        queryWrapper.eq(StringUtils.isNotBlank(leaveVo.getLeavetype()), "leavetype", leaveVo.getLeavetype());
        //根据时间段查询
        queryWrapper.between(StringUtils.isNotBlank(leaveVo.getStartTime()) && StringUtils.isNotBlank(leaveVo.getEndtime()),"createtime",leaveVo.getStartTime(),leaveVo.getEndtime());
        //根据用户id查询
        queryWrapper.in("userid",listUserId);
        this.leaveService.page(page, queryWrapper);
        List<Leave> list = page.getRecords();
       /*
        根据用户id查用户名
        * */
        for(Leave li:list){
           String uname= this.userService.showLeaveUsername(li.getUserid());
           li.setLeaveName(uname);
        }
        return new DataGridView(page.getTotal(), list);


    }


    /**
     * 填写请假单
     */
    @RequestMapping("addLeave")
    public ResultObj addLeave(@Valid LeaveVo leaveVo, BindingResult bindingResult)  {


        //数据校证，找出错误信息
        if(bindingResult.hasErrors()){
            String errorInfo="";
            for (FieldError fieldError:bindingResult.getFieldErrors()){
                errorInfo+=fieldError.getDefaultMessage()+"      ";
            }
            return new ResultObj(Constast.ERROR,errorInfo);
        }
        try {
            //根据开始时间和请假天数计算结束时间
            /*
             * 先把指定的日期转换成毫秒，再加天数的毫秒，再转成日期
             * */
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 日期格式
            Date date = dateFormat.parse(leaveVo.getLeavetime()); // 指定日期
            Date newDate = addDate(date, leaveVo.getDays()); // 指定日期加上20天
            leaveVo.setEndtime(dateFormat.format(newDate));
            System.out.println(dateFormat.format(date));// 输出格式化后的日期
            System.out.println(dateFormat.format(newDate));
            //设置创建时间,默认状态,用户id
            leaveVo.setCreatetime(dateFormat.format(new Date()));
            leaveVo.setState(0);
            User currentUser= (User) WebUtils.getSession().getAttribute("user");
            leaveVo.setUserid(currentUser.getId());

            //保存请假单信息
            this.leaveService.save(leaveVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    //注意day 必须是long类型 否者会超精度影响结果
    public static Date addDate(Date date,long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     * 修改请假单
     */
    @RequestMapping("updateLeave")
    public ResultObj updateLeave(@Valid LeaveVo leaveVo,BindingResult bindingResult) {
        //数据校证
        if(bindingResult.hasErrors()){
            String errorInfo="";
            for (FieldError fieldError:bindingResult.getFieldErrors()){
                errorInfo+=fieldError.getDefaultMessage()+"      ";
            }
            return new ResultObj(Constast.ERROR,errorInfo);
        }
        try {
            //根据开始时间和请假天数计算结束时间
            /*
             * 先把指定的日期转换成毫秒，再加天数的毫秒，再转成日期
             * */
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 日期格式
            Date date = dateFormat.parse(leaveVo.getLeavetime()); // 指定日期
            Date newDate = addDate(date, leaveVo.getDays()); // 指定日期加上20天
            leaveVo.setEndtime(dateFormat.format(newDate));
            System.out.println(dateFormat.format(date));// 输出格式化后的日期
            System.out.println(dateFormat.format(newDate)+"id=  "+leaveVo.getId());
            //设置创建时间,默认状态,用户id
            leaveVo.setCreatetime(dateFormat.format(new Date()));
            leaveVo.setState(0);
            User currentUser= (User) WebUtils.getSession().getAttribute("user");
            leaveVo.setUserid(currentUser.getId());
            this.leaveService.updateById(leaveVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 提交请假单同时启动请假流程
     */
    @RequestMapping("submissionLeave")
    public ResultObj submissionLeave(Leave leave) {
        try {
            /*查所有当前用户、当前用户的所在部门负责人、总经理的名字*/
            User currentUser = (User) WebUtils.getSession().getAttribute(Constast.currentUser);
            //查当前用户的直属领导
            if(currentUser.getMgr()==null){
                System.out.println("要抛出异常");
            }
            User currentUserLeader= this.userService.getById(currentUser.getMgr());
            //查当前用户的直属领导的直属领导
            User currentUserLeaderLeader = this.userService.getById(currentUserLeader.getMgr());
            //查人事部的所有员工的名字
            List<String> humanList=this.userService.showAllHuman(Constast.allHuman);

            Map<String,Object> map=new HashMap<>();
            map.put("LeaveUserName",currentUser.getName());
            map.put("DeptUserName",currentUserLeader.getName());
            map.put("TotalUserName",currentUserLeaderLeader.getName());
            map.put("HumanUserName",humanList);

            ProcessInstance instance = runtimeService.startProcessInstanceByKey(Constast.ProcessesName, currentUser.getId().toString(),map);
            System.out.println ("启动流程实例成功:============");

            this.leaveService.submissionLeave(leave.getId(),Constast.SubmitState);
            return ResultObj.SUBMISSION_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.SUBMISSION_ERROR;
        }
    }

    /**
     * 放弃请假单
     */
    @RequestMapping("giveUpLeave")
    public ResultObj giveUpLeave(Integer id) {
        try {
            int newState=3;
            this.leaveService.submissionLeave(id,newState);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 删除请假单
     */
    @RequestMapping("deleteLeave")
    public ResultObj deleteLeave(Integer id) {
        try {
            this.leaveService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}


package com.sxt.sys.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "用户名不能为空")
    private String name;

    @NotEmpty(message = "登录名不能为空")
    private String loginname;

    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @NotEmpty(message = "手机号码不能为空")
    @Pattern(regexp = "^(13|14|15|18|17)[0-9]{9}",message = "手机号码格式错误")
    private String mobile;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotNull(message = "性别不能为空")
    private Integer sex;

    private String remark;

    private String password;

    @NotNull(message = "所属部门id不能为空")
    private Integer deptid;

    private Date hiredate;

    @NotNull(message = "所属领导用户id不能为空")
    private Integer mgr;

    @NotNull(message = "是否可用不能为空")
    private Integer available;

    @NotNull(message = "排序码不能为空")
    private Integer ordernum;

    @NotEmpty(message = "工号不能为空")
    private String jobnumber;

    /**
     * 用户类型[0超级管理员1，管理员，2普通用户]
     */
    private Integer type;

    /**
     * 头像地址
     */
    private String imgpath;

    private String salt;

    /**
     * 领导名称
     */
    @TableField(exist=false)
    private String leadername;
    /**
     * 部门名称
     */
    @TableField(exist=false)
    private String deptname;
    //时间字符串
    @TableField(exist = false)
    private String oldPassword;


}

package com.sxt.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxt.sys.common.ActiverUser;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.Loginfo;
import com.sxt.sys.domain.User;
import com.sxt.sys.realm.MyToken;
import com.sxt.sys.service.DeptService;
import com.sxt.sys.service.LoginfoService;
import com.sxt.sys.util.OperationCodeUtil;
import com.sxt.sys.util.SmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  登陆前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private SmsUtil smsUtil;
	@Value("${aliyun.sms.template_code}")
	private String template_code;//模板编号
	//    @Value("${aliyun.sms.sign_name}")
	private String sign_name="自动注册小程序";//签名
    @Autowired
	private LoginfoService loginfoService;

    @Autowired
	private DeptService deptService;

    /**
    *@Description 手机短信验证码
    *@Param     手机号码
    *@Return
    *@Author Mr.Ren
    *@Date 2019/12/16
    *@Time 20:14
    */
	@RequestMapping("mobileCode")
	public ResultObj showMessage(@RequestParam("mobile") String mobile){
		//生成验证码
		String smsCode= RandomStringUtils.randomNumeric(6);
		//控制台打印一份
		System.out.println(mobile+"  验证码："+smsCode+" "+sign_name+template_code);
		//向session存一份
		HttpSession session= WebUtils.getSession();
		session.setAttribute("code",smsCode);
		//向客户发送一份
//        try {
//            //code是模板内容的占位符
//            smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+ smsCode +"\"}");
//        } catch (ClientException e) {
//            throw  new RuntimeException("短信发送失败");
//        }
		return ResultObj.LOGIN_SUCCESS;
	}

	//获取验证码
	@RequestMapping("getCheckCode")
	public void getCode(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		System.out.println("aaaa");
		//生成字符串验证码
//        BufferedImage image =VerificationCode.getCheckCode(request,response);
//        ImageIO.write(image,"JPG",response.getOutputStream());
		//生成运算符验证码
		BufferedImage image = OperationCodeUtil.getCheckCode(request,response);
		ImageIO.write(image,"JPG",response.getOutputStream());
	}

	/*
	* 用户登录
	*/
	@RequestMapping("login")
	public ResultObj login(@RequestBody User user, @RequestParam("uCode") String uCode) {
		System.out.println(user+uCode);
		//用户名和密码登录
		if(uCode.equals("0000")){
			System.out.println("用户名和密码登录");
			return loginMethod(user);
		}
		HttpSession session =WebUtils.getSession();
		//获取session里面的验证码
		String sessionCode= (String) session.getAttribute("code");
		//System.out.println("session里面 "+sessionCode);
		if (uCode.equals(sessionCode)){
			return loginMethod(user);
		}
		return ResultObj.LOGIN_ERROR_CODE;
	}
	
	/**
	*@Description 
	*@Param 
	*@Return 
	*@Author Mr.Ren
	*@Date 2019/12/16
	*@Time 17:01
	*/
	public ResultObj loginMethod(User user){
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token;
		/*
		* 根据用户名是否为null判断是手机登录还是密码登录
		* */
		if(user.getPassword()==null || user.getPassword()=="" ){
			token =new MyToken(user.getMobile());
		}else{
			token=new UsernamePasswordToken(user.getLoginname(),user.getPassword());
		}

		try {
			subject.login(token);
			ActiverUser activerUser=(ActiverUser) subject.getPrincipal();
			//查询部门名称
			QueryWrapper<Dept> Wrapper = new QueryWrapper<>();
			Wrapper.eq("id",activerUser.getUser().getDeptid());
			Dept dept = this.deptService.getOne(Wrapper);
			activerUser.getUser().setDeptname(dept.getTitle());

			WebUtils.getSession().setAttribute(Constast.currentUser, activerUser.getUser());
			//WebUtils.getSession().setAttribute("role",activerUser.getRoles());

			//记录登陆日志
//			Loginfo entity=new Loginfo();
//			entity.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
//			//获取ip地址WebUtils.getRequest().getRemoteAddr()
//			entity.setLoginip(WebUtils.getRequest().getRemoteAddr());
//			entity.setLogintime(new Date());
//			loginfoService.save(entity);
			return ResultObj.LOGIN_SUCCESS;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return ResultObj.LOGIN_ERROR_PASS;
		}
	}

}


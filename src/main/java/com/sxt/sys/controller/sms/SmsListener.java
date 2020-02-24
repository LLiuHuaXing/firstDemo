package com.sxt.sys.controller.sms;

import com.sxt.sys.common.ResultObj;
import com.sxt.sys.util.SmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*第三步 阿里云短信服务 接口代码
* 第四步 建立该类
* */
@Controller
@RequestMapping("/sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;//模板编号

//    @Value("${aliyun.sms.sign_name}")
    private String sign_name="自动注册小程序";//签名


    @RequestMapping("/mobileCode")
    @ResponseBody
    public ResultObj showMessage(@RequestParam("mobile") String mobile, HttpServletRequest request){
        //生成验证码
        String smsCode= RandomStringUtils.randomNumeric(6);
        //控制台打印一份
        System.out.println(mobile+"  验证码："+smsCode+" "+sign_name+template_code);
        //向session存一份
        HttpSession session=request.getSession();
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
}

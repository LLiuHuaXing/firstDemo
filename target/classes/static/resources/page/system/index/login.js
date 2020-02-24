layui.use(['form'], function () {
    var $ = layui.jquery,
        form = layui.form,
        layer = layui.layer;
    /*
    * 判断是否用验证码登录，不用：0000，用：1111*/
    var loginCode="0000"
    // 登录过期的时候，跳出ifram框架
    // if (top.location != self.location) top.location = self.location;

    // 粒子线条背景
    // $(document).ready(function(){
    //     $('.layui-container').particleground({
    //         dotColor:'#5cbdaa',
    //         lineColor:'#5cbdaa'
    //     });
    // });

    /*动态生成验证码
    * 参考文档 https://blog.csdn.net/u013360850/article/details/52577422
    * https://blog.csdn.net/qq_39992641/article/details/97116408
    * 参数：https://blog.csdn.net/qq_24140237/article/details/79484117
    * */
    $('#captchaPic').click(function () {
        console.log("=====++++");
        createCaptchaCode();
    });
    function createCaptchaCode(){
        console.log("====");
        var src = "/login/getCheckCode?"+new Date().getTime(); //加时间戳，防止浏览器利用缓存
        $('.verifyCode').attr('src', src);
    }

    //发送获取手机短信验证码请求
    $(".VerificationCode").click(function () {
        var phone=$("#userPhone").val();
        if(phone==null || phone==""){
            layer.msg("手机号码不能为空")
        }else if(!/^(13|14|15|18|17)[0-9]{9}/.test(phone)){
            layer.msg("请填写正确手机号码格式");
        }
        else {
            $.ajax({
                url: '/login/mobileCode?mobile='+phone,
                type: 'get',
                success: function (data) {
                    console.log("获取手机短信验证码成功");
                },
            });
        }
    });


    //密码登录数据校验
    form.verify({
        username:function (value, item) {
            if(value===""){
                return "用户名不能为空";
            }
            if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
                return '用户名不能有特殊字符';
            }
            if(/(^\_)|(\__)|(\_+$)/.test(value)){
                return '用户名首尾不能出现下划线\'_\'';
            }
            if(/^\d+\d+\d$/.test(value)){
                return '用户名不能全为数字';
            }
        },
        password:function (value, item) {
            if(value===""){
                return "密码不能为空";
            }
            // if (!/^[\S]{6,15}$/.test(value)){
            //     return "密码必须6到15位，且不能出现空格";
            // }
            // if(!/[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*/.test(value)){
            //     return "密码必须由字母和数字组成";
            // }
        },
        captchaCode:function(value,item){
            if(value===""){
                return "验证码不能为空"
            }
            if(!/[0-9a-zA-Z]{4}/.test(value) && !/[0-9]/.test(value)){
                return "验证码只是4位数"
            }
        },
        //手机短信登录数据校验
        userPhone:function (value, item) {
            if(value===""){
                return "手机号码不能为空";
            }
            if(!/^(13|14|15|18|17)[0-9]{9}/.test(value)){
                return "请填写正确手机号码格式";
            }
        },
        userCode:function (value, item) {
            if(value===""){
                return "验证码不能为空";
            }
            if(!/[0-9]{6}/.test(value)){
                return "输入正确的验证码"
            }
        }
    });

    //发送登录请求
    function loginRequestInfo(data,btn){

        var jsonObject={};
        var uCode=null;

        /*根据data.JudgementBasis的不同值发送不同参数*/
        if(data.JudgementBasis==="notPassword"){
            jsonObject.mobile=data.userPhone;
            uCode=data.userCode
        }else{
            jsonObject.loginname=data.username;
            console.log("data.username "+data.username);
            jsonObject.password=data.password
            /*
            页面元素的验证码默认为value="0000"，
            等于0000用用户名和密码登录，不等于用用户名、密码、验证码登录
            */
            uCode=loginCode;
            if(data.captcha!=="0000"){
                uCode=data.captcha
            }
        }
        var jsonStr=JSON.stringify(jsonObject);
        $.ajax({
            type: 'post',
            contentType: "application/json",
            url: '/login/login?uCode='+uCode,
            data: jsonStr,
            success: function (result_data) {
                //设置登录按钮 恢复可点击   在前端防止 重复点击
                btn.text("登录").attr("disabled",false).removeClass("layui-disabled");
                if (result_data.code === 200) {
                    //再由controller跳转页面
                    // alert("跳转页面");
                    window.location="/sys/index";
                } else {
                    $(".divCodeId").removeAttr("style","");
                    loginCode="1111";
                    createCaptchaCode();
                    layer.msg(result_data.msg);
                }
            },
            error:function() {
                layer.msg("登录验证失败");
            }
        });
    }

    // 进行登录操作
    form.on('submit(login)', function (data) {
        data = data.field;
        console.log(data.password+" 登录操作 "+data.JudgementBasis);
        var btn =  $(this);
        //设置登录按钮  为不可点击
        btn.text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
        loginRequestInfo(data,btn);
        return false;
    });

    /*登录页面切换*/
    /*切换短信登录*/
    $(".loginPhone").on("click",function () {
        console.log("loginPhone");
        $("#div1").css("display","none");
        $("#div2").removeAttr("style","");
    });
    /*切换密码登录*/
    $(".loginPassword").on("click",function () {
        console.log("loginPassword");
        $("#div1").removeAttr("style","");
        $("#div2").css("display","none");
    });


    function JA() {
        console.log("JA()")
        var errorMsg = "";
        //根本没有进去这个方法  https://blog.csdn.net/chidi3832/article/details/100598999
        var validator =$("#loginFormId").validate({
            invalidHandler:function(form,validator){
                //只显示第一个错误信息  https://blog.csdn.net/qq_29347295/article/details/55802891
                $.each(validator.invalid,function(key,value){
                    var tmpkey = key;
                    var tmpval = value;
                    validator.invalid = {};
                    validator.invalid[tmpkey] = value;
                    errorMsg = value;
                    return false;
                });
            },
            submitHandler:function(){
                //表单验证成功
                console.log("表单验证成功()")
                // localStorage.setItem("username",loginForm.username.value);
                // localStorage.setItem("password",loginForm.password.value);
            },
            errorPlacement: function(error, element) {
                console.log("errorMsg() "+errorMsg);
            },
            rules:{
                username:{
                    required: true,
                    minlength:4
                },
                password:{
                    required: true,
                    minlength:6
                    // checkPassword:true
                }
            },
            messages:{
                username:{
                    required: "用户名不能为空",
                    minlength:layer.msg("用户名长度至少4位以上")
                },
                password:{
                    required:"密码不能为空",
                    minlength:"密码长度在6以上之间"
                    // checkPassword:layer.msg("密码必须由字母和数字组成"),
                }
            }
        });
        //自定义正则表达示验证方法
        $.validator.addMethod("checkPassword",function(value,element,params){
            console.log("aaa达示验证方法");
            var checkEmail = /^[a-z0-9]+@([a-z0-9]+\.)+[a-z]{2,4}$/i;
            return this.optional(element)||(checkEmail.test(value));
        },"*请输入正确的邮箱！");
    }


});
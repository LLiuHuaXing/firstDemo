
var tableIns;
layui.extend({
    dtree: '/resources/layui_ext/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['jquery','form','table','layer','dtree'],function(){
    var $=layui.jquery;
    var form=layui.form;
    var table=layui.table;
    var layer=layui.layer;
    var dtree=layui.dtree;
    //加载 数据
    tableIns=table.render({
        elem: '#userTable'
        ,url:'/user/loadAllUser'
        ,toolbar: '#userToolBar' //开启头部工具栏，并为其绑定左侧模板
        ,title: '部门数据表'
        //,height:'full-250'
        ,page: true
        ,cols: [ [
            {field:'id', title:'ID',align:'center',width:'80'}
            ,{field:'name', title:'用户姓名',align:'center',width:'100'}
            ,{field:'loginname', title:'登陆名称',align:'center',width:'100'}
            ,{field:'sex', title:'性别',align:'center',width:'80',templet:function(d){
                    return d.sex==1?'<font color=blue>男</font>':'<font color=red>女</font>';
                }}
            ,{field:'deptname', title:'部门名称',align:'center',width:'120'}
            ,{field:'leadername', title:'直属领导',align:'center',width:'100'}
            ,{field:'address', title:'用户地址',align:'center',width:'150'}
            ,{field:'remark', title:'用户备注',align:'center',width:'150'}
            ,{field:'hiredate', title:'入职时间',align:'center',width:'180'}
            ,{field:'available', title:'是否可用',align:'center',width:'100',templet:function(d){
                    return d.available==1?'<input type="checkbox" checked=" " name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">':'<input type="checkbox"  name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">';
                }}
            ,{field:'ordernum', title:'排序码',align:'center',width:'100'}
            ,{field:'imgpath', title:'用户头像',align:'center',width:'200'}
            ,{fixed: 'right', title:'操作', toolbar: '#userRowBar',align:'center',width:'400'}
        ] ]
        ,done: function(res, curr, count){ //处理删除某一页最后一条数据的BUG
            if(res.data.length==0&&curr!=1){
                tableIns.reload({
                    page:{
                        curr:(curr-1)
                    }
                });
            }
        }
    });


    //模糊查询
    form.on("submit(doSearch)",function(data){
        tableIns.reload({
            where:data.field,
            page:{
                curr:1
            }
        });
        return false;
    });

    //监听工具条的事件
    table.on("toolbar(userTable)",function(obj){
        switch(obj.event){
            case 'add':
                openAddLayer();
                break;
        };
    });

    //监听行工具条的事件
    table.on("tool(userTable)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'update':
                openUpdateUserLayer(data);
                break;
            case 'delete':
                deleteUser(data);
                break;
            case 'resetPwd':
                resetPwd(data);
                break;
            case 'selectRole':
                selectRole(data);
                break;
        };
    });

    var mainIndex;
    var url;
    //打开添加的弹出层
    function openAddLayer(){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['800px','600px'],
            title:'添加用户',
            success:function(){
                $("#dataFrm")[0].reset();
                $("#deptid").val("");
                url="/user/addUser";
                //初始化排序码
                $.get("/user/loadUserMaxOrderNum",function(res){
                    $("#ordernum").val(res.value);
                });
                var html="<option value='0'>请选择直属领导</option>";
                $("#mgr").html(html);
                form.render("select");
            }
        });
    }

    //打开修改的弹出层
    function openUpdateUserLayer(data){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['800px','600px'],
            title:'修改用户',
            success:function(){
                $("#dataFrm")[0].reset();
                $("#deptid").val("");
                //装载新的数据
                form.val("dataFrm",data);
                //选中之前的所属部门  nodeId=data.deptid;
                dtree.dataInit("deptTree", data.deptid);
                dtree.setSelectValue("deptTree");

                //选中领导部门
                var leaderid=data.mgr;
                $.get("/user/loadUserById",{id:leaderid},function(res){
                    var d=res.data;
                    dtree.dataInit("leaderdeptTree", d.deptid);
                    dtree.setSelectValue("leaderdeptTree");
                    $.get("/user/loadUsersByDeptId",{deptid:d.deptid},function(res){
                        var users=res.data;
                        var dom_mgr=$("#mgr");
                        var html="<option value='0'>请选择直属领导</option>";
                        $.each(users,function(index,item){
                            html+="<option value='"+item.id+"'>"+item.name+"</option>";
                        });
                        dom_mgr.html(html);
                        //选中一个
                        dom_mgr.val(leaderid);
                        //重新渲染
                        form.render("select");
                    });
                })
                url="/user/updateUser";
            }
        });
    }

    //添加和修改的数据校验
    form.verify({
        mobile: function (value, item) {
            //手机号码和座机 https://blog.csdn.net/lhc_makefunny/article/details/78032602
            if(!/^(13|14|15|18|17)[0-9]{9}/.test(value)){
                return "请填写正确手机号码格式";
            }
        }
    });

    //监听添加和修改的事件
    form.on("submit(doSubmit)",function(data){
        $.post(url,data.field,function(res){
            if(res.code==200){
                tableIns.reload();
                layer.msg(res.msg);
                layer.close(mainIndex);
            }else{
                layer.msg(res.msg);
            }
        })
        return false;
    })
    //删除
    function deleteUser(data){
        layer.confirm('你确定要删除【'+data.name+'】这条用户吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/user/deleteUser",{id:data.id},function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }


    //重置密码
    function resetPwd(data){
        layer.confirm('你确定要重置【'+data.name+'】这个用户密码吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/user/resetPwd",{id:data.id},function(res){
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }

    //初始化查询条件下拉树
    var search_deptTree=dtree.renderSelect({
        elem: "#search_deptTree",
        width: "100%", // 可以在这里指定树的宽度来填满div
        dataStyle: "layuiStyle",  //使用layui风格的数据格式
        dataFormat: "list",  //配置data的风格为list
        response:{message:"msg",statusCode:0},  //修改response中返回数据的定义
        url: "/dept/loadDeptManagerLeftTreeJson" // 使用url加载（可与data加载同时存在）
    });
    //监听点击的方法
    dtree.on("node(search_deptTree)" ,function(obj){
        $("#search_deptid").val(obj.param.nodeId);
        console.log(obj.param.nodeId); // 点击当前节点传递的参数
    });

    //初始化添加弹出层所属部门的下拉列表
    var deptTree=dtree.renderSelect({
        elem: "#deptTree",
        width: "100%", // 可以在这里指定树的宽度来填满div
        dataStyle: "layuiStyle",  //使用layui风格的数据格式
        dataFormat: "list",  //配置data的风格为list
        response:{message:"msg",statusCode:0},  //修改response中返回数据的定义
        url: "/dept/loadDeptManagerLeftTreeJson" // 使用url加载（可与data加载同时存在）
    });
    //监听点击的方法
    dtree.on("node(deptTree)" ,function(obj){
        $("#deptid").val(obj.param.nodeId);
        console.log(obj.param.nodeId); // 点击当前节点传递的参数
    });

    //初始化添加弹出层领导部门的下拉列表
    var leaderdeptTree=dtree.renderSelect({
        elem: "#leaderdeptTree",
        width: "100%", // 可以在这里指定树的宽度来填满div
        dataStyle: "layuiStyle",  //使用layui风格的数据格式
        dataFormat: "list",  //配置data的风格为list
        response:{message:"msg",statusCode:0},  //修改response中返回数据的定义
        url: "/dept/loadDeptManagerLeftTreeJson" // 使用url加载（可与data加载同时存在）
    });
    //监听点击的方法
    dtree.on("node(leaderdeptTree)" ,function(obj){
        var deptid=obj.param.nodeId;
        //根据部门ID查询当前部门下面的领导列表
        $.get("/user/loadUsersByDeptId",{deptid:deptid},function(res){
            var users=res.data;
            var dom_mgr=$("#mgr");
            var html="<option value='0'>请选择直属领导</option>";
            $.each(users,function(index,item){
                html+="<option value='"+item.id+"'>"+item.name+"</option>";
            });
            dom_mgr.html(html);
            //重新渲染
            form.render("select");
        });
    });


    //监听用户名的失去焦点事件
    $("#username").on("blur",function(){
        var username=$(this).val();
        $.ajax({
            type: "post",
            url: "/user/changeChineseToPinyin",
            async:false,
            data:{
                username:username
            },
            success: function(res){
                $("#loginname").val(res.value);
            }
        });
        var loginname=$("#loginname").val();
        var deptId=$("#mgr").val();
        var oldData=loginname+"-"+deptId;
        autoJobNumber(oldData);
    });
    //监听直属领导的失去焦点事件
    form.on('select(mgr)', function(data){
        console.log(data.value); //得到被选中的值
        var loginname=$("#loginname").val();
        var deptId=$("#mgr").val();
        var oldData=loginname+"-"+deptId;
        autoJobNumber(oldData);
    });
    //自动填充用户工号
    function autoJobNumber(data) {
        //JS几种数据类型转换 https://blog.csdn.net/qq_37777208/article/details/83586470
        var monthData=Number(new Date().getMonth())+1;
        var newData=new Date().getFullYear()+"-"+monthData+"-"+new Date().getDate()+"-"+data;
        console.log(monthData)
        $("#addjobnumber").val(newData);
    }


    //打开分配角色的弹出层
    function selectRole(data){
        mainIndex=layer.open({
            type:1,
            content:$("#selectUserRoleDiv"),
            area:['800px','600px'],
            title:'分配【'+data.name+'】的角色',
            btn: ['<span class=layui-icon>&#xe605;确认分配</span>', '<span class=layui-icon>&#x1006;关闭窗口</span>'],
            yes: function(index, layero){
                var checkStatus = table.checkStatus('roleTable');
                var params="uid="+data.id;
                $.each(checkStatus.data,function(index,item){
                    params+="&ids="+item.id;
                    params+="&names="+item.name;
                });
                $.post("/user/saveUserRole",params,function(res){
                    layer.close(index);
                    layer.msg(res.msg);
                })
            },
            btn2: function(index, layero){
                //return false 开启该代码可禁止点击该按钮关闭
            },
            btnAlign: 'c',
            success:function(){
                initRoleTable(data);
            }
        });
    }
    var roleTableIns;
    //初始化角色列表
    function initRoleTable(data){
        roleTableIns=table.render({
            elem: '#roleTable'
            ,url:'/user/initRoleByUserId'
            ,where:{
                id:data.id
            }
            ,cols: [ [
                {type:'checkbox',align:'center'}
                ,{field:'id', title:'ID',align:'center'}
                ,{field:'name', title:'角色名称',align:'center'}
                ,{field:'remark', title:'角色备注',align:'center'}
            ] ]
        });
    }
});

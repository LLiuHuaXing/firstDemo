
    layui.extend({
        dtree: '/resources/layui_ext/dtree/dtree'
    }).use(['jquery','form','table','layer','dtree'],function(){
        var $=layui.jquery;
        var form=layui.form;
        var table=layui.table;
        var layer=layui.layer;
        var dtree=layui.dtree;
        //加载 数据
        var tableIns=table.render({
            elem: '#roleTable'
            ,url:'/role/loadAllRole'
            ,toolbar: '#roleToolBar' //开启头部工具栏，并为其绑定左侧模板
            ,title: '角色数据表'
            ,height:'full-220'
            ,page: true
            ,cols: [ [
                {field:'id', title:'ID',align:'center'}
                ,{field:'name', title:'角色名称',align:'center'}
                ,{field:'remark', title:'角色备注',align:'center'}
                ,{field:'available', title:'是否可用',align:'center',templet:function(d){
                        return d.available==1?'<font color=blue>可用</font>':'<font color=red>不可用</font>';
                    }}
                ,{field:'createtime', title:'创建时间',align:'center'}
                ,{fixed: 'right', title:'操作', toolbar: '#roleRowBar',align:'center',width:'300'}
            ] ]
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
        table.on("toolbar(roleTable)",function(obj){
            switch(obj.event){
                case 'add':
                    openAddLayer();
                    break;
            };
        });

        //监听行工具条的事件
        table.on("tool(roleTable)",function(obj){
            var data = obj.data; //获得当前行数据
            switch(obj.event){
                case 'update':
                    openUpdateRoleLayer(data);
                    break;
                case 'delete':
                    deleteRole(data);
                    break;
                case 'selectPermission':
                    selectPermission(data);
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
                title:'添加角色',
                success:function(){
                    $("#dataFrm")[0].reset();
                    url="/role/addRole";
                }
            });
        }

        //打开修改的弹出层
        function openUpdateRoleLayer(data){
            mainIndex=layer.open({
                type:1,
                content:$("#addOrUpdateDiv"),
                area:['800px','600px'],
                title:'修改角色',
                success:function(){
                    $("#dataFrm")[0].reset();
                    //装载新的数据
                    form.val("dataFrm",data);
                    url="/role/updateRole";
                }
            });
        }

        form.on("submit(doSubmit)",function(data){
            $.post(url,data.field,function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
                layer.close(mainIndex);
            })
            return false;
        });
        //删除
        function deleteRole(data){
            layer.confirm('你确定要删除【'+data.name+'】这条角色吗?', {icon: 3, title:'提示'}, function(index){
                $.post("/role/deleteRole",{id:data.id},function(res){
                    if(res.code==200){
                        tableIns.reload();
                    }
                    layer.msg(res.msg);
                })
                layer.close(index);
            });
        }

        //打开分配角色的页面
        function selectPermission(data){
            mainIndex=layer.open({
                type:1,
                content:$("#selectRolePermissionDiv"),
                area:['400px','550px'],
                title:'分配【'+data.name+'】的权限',
                btn: ['<span class=layui-icon>&#xe605;确认分配</span>', '<span class=layui-icon>&#x1006;关闭窗口</span>'],
                yes: function(index, layero){
                    //得到树选中的所有节点
                    var permissionData = dtree.getCheckbarNodesParam("permissionTree");
                    console.log(params);
                    var params="rid="+data.id;
                    $.each(permissionData,function(index,item){
                        params+="&ids="+item.nodeId;
                    });
                    $.post("/role/saveRolePermission",params,function(res){
                        layer.close(index);
                        layer.msg(res.msg);
                    })
                },
                btn2: function(index, layero){
                    //return false 开启该代码可禁止点击该按钮关闭
                },
                btnAlign: 'c',
                success:function(){
                    //根据角色ID请求权限和菜单tree的json数据
                    dtree.render({
                        elem: "#permissionTree",
                        url: "/role/initPermissionByRoleId?roleId="+data.id,
                        dataStyle: "layuiStyle",  //使用layui风格的数据格式
                        dataFormat: "list",  //配置data的风格为list
                        response:{message:"msg",statusCode:0},  //修改response中返回数据的定义
                        checkbar: true,
                        checkbarType: "all" // 默认就是all，其他的值为： no-all  p-casc   self  only
                    });
                }
            });
        }
    });
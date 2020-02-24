
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
        elem: '#deptTable'
        ,url:'/dept/loadAllDept'
        ,toolbar: '#deptToolBar' //开启头部工具栏，并为其绑定左侧模板
        ,title: '部门数据表'
        // ,height:'full-180'
        ,page: true
        ,cols: [ [
            {field:'id', title:'ID',align:'center',width:'80'}
            ,{field:'pid', title:'父级部门ID',align:'center',width:'100'}
            ,{field:'title', title:'部门名称',align:'center',width:'150'}
            ,{field:'address', title:'部门地址',align:'center',width:'150'}
            ,{field:'remark', title:'部门备注',align:'center',width:'180'}
            ,{field:'open', title:'是否展开',align:'center',width:'100',templet:function(d){
                     return d.open==1?'<font color=blue>展开</font>':'<font color=red>不展开</font>';
                   // return '<input type="checkbox" name="close" lay-skin="switch" lay-text="ON|OFF">';
                }}
            ,{field:'available', title:'是否可用',align:'center',width:'100',templet:function(d){
                    return d.available==1?'<input type="checkbox" checked=" " name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">':'<input type="checkbox"  name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">';
                }}
            ,{field:'ordernum', title:'排序码',align:'center',width:'100'}
            ,{field:'createtime', title:'创建时间',align:'center',width:'200'}
            ,{fixed: 'right', title:'操作', toolbar: '#deptRowBar',align:'center',width:'200'}
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

    //监听指定开关
    form.on('switch(switchTest)', function(data){
        var switchData={}
        if(this.checked){
            switchData.available=1
        }else{
            switchData.available=0
        }
        switchData.id=data.elem.name
        //console.log(data.elem.name+" "+JSON.stringify(switchData));
        $.post("/dept/updateDeptAvailable",switchData,function(res){
            if(res.code==200){
                layer.msg(res.msg);
            }else{
                layer.msg(res.msg);
            }
        })
    });

    //模糊查询
    form.on("submit(doSearch)",function(data){
        data.field.id="";
        tableIns.reload({
            where:data.field,
            page:{
                curr:1
            }
        });
        return false;
    });

    //监听工具条的事件
    table.on("toolbar(deptTable)",function(obj){
        switch(obj.event){
            case 'add':
                openAddLayer();
                break;
        };
    });

    //监听行工具条的事件
    table.on("tool(deptTable)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'update':
                openUpdateDeptLayer(data);
                break;
            case 'delete':
                deleteDept(data);
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
            title:'添加部门',
            success:function(){
                $("#dataFrm")[0].reset();
                $("#pid").val(" ");
                url="/dept/addDept";
                //初始化排序码
                $.get("/dept/loadDeptMaxOrderNum",function(res){
                    $("#ordernum").val(res.value);
                })
            }
        });
    }

    //打开修改的弹出层
    function openUpdateDeptLayer(data){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['800px','600px'],
            title:'修改部门',
            success:function(){
                $("#dataFrm")[0].reset();
                $("#pid").val(" ");
                //装载新的数据
                form.val("dataFrm",data);
                if(data.pid!==0){
                    //选中之前父级的部门  nodeId=data.pid;
                    dtree.dataInit("deptTree", data.pid);
                    dtree.setSelectValue("deptTree");
                }
                url="/dept/updateDept";
            }
        });
    }

    //监听添加和修改按钮事件
    form.on("submit(doSubmit)",function(data){
        $.post(url,data.field,function(res){
            if(res.code==200){
                tableIns.reload();
                //刷新下拉树
                selectTree.reload();
                //刷新左边的部门树
                window.parent.left.deptTree.reload();
            }
            layer.msg(res.msg);
            layer.close(mainIndex);
        })
        return false;
    })

    //删除
    function deleteDept(data){

        $.post("/dept/checkDeptHasChildrenNode",{id:data.id},function(result){
            if(result.value){
                layer.msg("当前部门节点有子部门，请选择删除子部门");
            }else{
                layer.confirm('你确定要删除【'+data.title+'】这条部门吗?', {icon: 3, title:'提示'}, function(index){
                    $.post("/dept/deleteDept",{id:data.id},function(res){
                        if(res.code==200){
                            tableIns.reload();
                            //刷新下拉树
                            selectTree.reload();
                            //刷新左边的部门树
                            window.parent.left.deptTree.reload();
                        }
                        layer.msg(res.msg);
                    })
                    layer.close(index);
                });
            }
        })
    }

    //初始化下拉树
    var selectTree=dtree.renderSelect({
        elem: "#deptTree",
        width: "100%", // 可以在这里指定树的宽度来填满div
        dataStyle: "layuiStyle",  //使用layui风格的数据格式
        dataFormat: "list",  //配置data的风格为list
        response:{message:"msg",statusCode:0},  //修改response中返回数据的定义
        url: "/dept/loadDeptManagerLeftTreeJson"// 使用url加载（可与data加载同时存在）

    });
    //监听点击的方法
    dtree.on("node(deptTree)" ,function(obj){
        $("#pid").val(obj.param.nodeId);
        console.log(obj.param.nodeId); // 点击当前节点传递的参数
    });

});

//给其它页面刷新当前页面数据表格的方法
function reloadTable(id){
    // alert("aaaff");
    // $("#title").val("");
    tableIns.reload({
        where:{
            id:id,
            title:"",
            address:"",
            remark:""
        },
        page:{
            curr:1
        }
    });
}
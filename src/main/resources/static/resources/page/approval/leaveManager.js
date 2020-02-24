
var tableIns;
layui.extend({
    dtree: '/resources/layui_ext/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['jquery','form','table','layer','dtree','laydate'],function(){
    var $=layui.jquery;
    var form=layui.form;
    var table=layui.table;
    var layer=layui.layer;
    var dtree=layui.dtree;
    var laydate = layui.laydate;

    //日期选择器:开始时间
    laydate.render({
        elem: '#startTime'
    });
    laydate.render({
        elem: '#leavetime'
    });

    //日期选择器:结束时间
    laydate.render({
        elem: '#endtime'
    });

    //加载 数据
    tableIns=table.render({
        elem: '#leaveTable'
        ,url:'/leave/loadAllLeave'
        ,toolbar: '#leaveToolBar' //开启头部工具栏，并为其绑定左侧模板
        ,title: '部门数据表'
        //,height:'full-250'
        ,page: true
        ,cols: [ [
            {type:'checkbox',align:'center',width:'40'}
            ,{field:'id', title:'ID',align:'center',width:'80'}
            ,{field:'title', title:'请假标题',align:'center',width:'100'}
            ,{field:'content', title:'请假原因',align:'center',width:'100'}
            ,{field:'days', title:'请假天数',align:'center',width:'120'}
            ,{field:'leavetime', title:'开始日期',align:'center',width:'100'}
            ,{field:'endtime', title:'结束日期',align:'center',width:'100'}
            ,{field:'leavetype', title:'请假类型',align:'center',width:'150'}
            ,{field:'createtime', title:'创建时间',align:'center',width:'150'}
            ,{field:'leaveName', title:'请假人',align:'center',width:'180'}
            ,{field:'state', title:'状态',align:'center',width:'100',templet:function(d){
                    var stateType="";
                    if(d.state==0){
                       stateType="未提交"
                    }else if(d.state==1){
                        stateType="审批中"
                    }else if(d.state==2){
                        stateType="审批完"
                    }else{
                        stateType="已放弃"
                    }
                    return stateType;
            }}
            //,{fixed: 'right', title:'操作', toolbar: '#leaveRowBar',align:'center',width:'400'}
            ,{fixed: 'right', title:'操作',align:'center',width:'250',templet:function (d) {
                    var buttonType="";
                    if(d.state==0){
                        buttonType='<button type="button" lay-event="update"  class="layui-btn layui-btn-sm"><span class="layui-icon layui-icon-edit"></span>编辑</button>' +
                            '<button type="button" lay-event="delete" class="layui-btn layui-btn-sm layui-btn-danger"><span class="layui-icon layui-icon-delete"></span>删除</button>' +
                            '<button type="button" lay-event="submission"  class="layui-btn layui-btn-sm"><span class="layui-icon layui-icon-edit"></span>提交申请</button>'
                    }else if(d.state==1){
                        buttonType='<button type="button" lay-event="showLeave"  class="layui-btn layui-btn-sm"><span class="layui-icon layui-icon-edit"></span>查看审批</button>' +
                            '<button type="button" lay-event="giveUpLeave"  class="layui-btn layui-btn-sm layui-btn-danger"><span class="layui-icon layui-icon-delete"></span>放弃申请</button>'
                    }else if(d.state==2){
                        buttonType='<button type="button" lay-event="showLeave"  class="layui-btn layui-btn-sm"><span class="layui-icon layui-icon-edit"></span>查看审批</button>'
                    }else{
                        buttonType='<button type="button" lay-event="delete" class="layui-btn layui-btn-sm layui-btn-danger"><span class="layui-icon layui-icon-delete"></span>删除</button>'
                    }
                    return buttonType;
            }}
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
    table.on("toolbar(leaveTable)",function(obj){
        switch(obj.event){
            case 'add':
                openAddLayer();
                break;
            case 'batchDelete':
                openAddLayer();
                break;

        };
    });

    //监听行工具条的事件
    table.on("tool(leaveTable)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'update':
                openUpdateLeaveLayer(data);
                break;
            case 'delete':
                deleteLeave(data);
                break;
            case 'submission':
                submissionLeave(data);
                break;
            case 'showLeave':
                deleteLeave(data);
                break;
            case 'giveUpLeave':
                giveUpLeave(data);
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
            area:['750px','400px'],
            title:'填写请假单',
            success:function(){
                $("#dataFrm")[0].reset();
                url="/leave/addLeave";
                //form.render("select");
            }
        });
    }

    //打开修改的弹出层
    function openUpdateLeaveLayer(data){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['750px','400px'],
            title:'修改用户',
            success:function(){
                $("#dataFrm")[0].reset();
                //装载新的数据
                form.val("dataFrm",data);
                url="/leave/updateLeave";
            }
        });
    }

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

    //提交请假单
    function submissionLeave(data){
        layer.confirm('你确定要提交【'+data.title+'】这条请假单吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/leave/submissionLeave",data,function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }

    //放弃请假单
    function giveUpLeave(data){
        layer.confirm('你确定要放弃【'+data.title+'】这条请假单吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/leave/giveUpLeave",{id:data.id},function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }

    //删除
    function deleteLeave(data){
        layer.confirm('你确定要删除【'+data.title+'】这条请假单吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/leave/deleteLeave",{id:data.id},function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }

});

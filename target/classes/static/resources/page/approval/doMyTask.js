
var tableIns;
layui.extend({
    dtree: '/resources/layui_ext/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['jquery','form','table','layer','upload'],function(){
    var $=layui.jquery;
    var form=layui.form;
    var table=layui.table;
    var layer=layui.layer;
    upload = layui.upload;

    //加载 数据
    tableIns=table.render({
        elem: '#MyTaskTable'
        ,url:'/process/loadAllMyTask'
        ,title: '我待审批内容'
        ,page: true
        ,cols: [ [
            ,{field:'ID_', title:'任务ID',align:'center',width:300}
            ,{field:'NAME_', title:'任务名称',align:'center',width:100}
            ,{field:'CREATE_TIME_', title:'创建时间',align:'center',width:200}
            ,{field:'ASSIGNEE_', title:'办理人',align:'center',width:100}
            ,{fixed: 'right', title:'操作', toolbar: '#MyTaskRowBar',align:'center'}
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

    /**
     *请假单act_re_procdef:流程定义
     */
    tableInProcdef=table.render({
        elem: '#procdefTable'
        ,url:'/process/loadAllProcdef'
        ,title: '流程部署信息'
        //,height:'full-250'
        ,page: true
        ,cols: [ [
            ,{field:'ID_', title:'流程定义ID',align:'center',width:'430'}
            ,{field:'NAME_', title:'定义名称',align:'center',width:'100'}
            ,{field:'KEY_', title:'定义key',align:'center',width:'100'}
            ,{field:'VERSION_', title:'定义版本',align:'center',width:'100'}
            ,{field:'DEPLOYMENT_ID_', title:'部署ID',align:'center',width:'100'}
            ,{field:'RESOURCE_NAME_', title:'资源名称[bpmn]',align:'center',width:'100'}
            ,{field:'DGRM_RESOURCE_NAME_', title:'资源名称[png]',align:'center',width:'200'}
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

    var mainIndex;
    var url;
    //部署流程
    form.on("submit(deployProcess)",function(){
        mainIndex=layer.open({
            type:1,
            content:$("#deployProcessWindow"),
            area:['700px','600px'],
            title:'部署流程',
            success:function(){
                deploymentProcesses=table.render({
                    elem: '#deployProcessTable'
                    , url: '/process/deployProcessTable'
                    , title: '部署流程'
                    , page: true
                    , cols: [[
                        , {field: 'definitionname', title: '流程名称', align: 'center'}
                        , {field: 'filenamebpmn', title: 'bpmn文件', align: 'center'}
                        , {field: 'filenamepng', title: 'png文件', align: 'center'}
                        , {field: 'variable', title: '是否部署', align: 'center',width:100,templet:function(d){
                            return d.variable===0?'未部署':'<font color=red>已部署</font>';
                        }}
                        , {fixed: 'right', title: '操作', align: 'center',templet:function(d){
                            if(d.variable==0){
                                return '<button type="button" lay-event="startDeployProcess" class="layui-btn layui-btn-normal">启动流程部署</button>'
                            }else{
                                return '<button type="" style="pointer-events: none;" class="layui-btn layui-btn-normal" >已禁用</button>'
                            }
                        }}
                    ]]
                })
            }
        });
        return false;
    });

    //启动流程部署搜索
    form.on("submit(startProcessDoSubmit)",function(data){
        deploymentProcesses.reload({
            where:data.field,
            page:{
                curr:1
            }
        });
        return false;
    });

    //监听行工具条的事件
    table.on("tool(MyTaskRowBar)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'completeTask':
                completeTask(data);
                break;
            case 'showProcessChart':
                showProcessChart(data);
                break;
        };
    });

    //办理任务
    function completeTask(data){
        mainIndex=layer.open({
            type: 1,
            content: $("#completeLeaveData"),
            area: ['700px', '600px'],
            title: '部署流程',
            success: function () {

                $("#dataFrm")[0].reset();
                //装载新的数据
                //form.val("dataFrm", data);
                // deploymentProcesses=table.render({
                //     elem: '#deployProcessTable'
                //     , url: '/process/deployProcessTable'
                //     , title: '部署流程'
                //     , page: true
                //     , cols: [[
                //         , {field: 'definitionname', title: '流程名称', align: 'center'}
                //         , {field: 'filenamebpmn', title: 'bpmn文件', align: 'center'}
                //         , {field: 'filenamepng', title: 'png文件', align: 'center'}
                //         , {field: 'variable', title: '是否部署', align: 'center',width:100,templet:function(d){
                //                 return d.variable===0?'未部署':'<font color=red>已部署</font>';
                //             }}
                //         , {fixed: 'right', title: '操作', align: 'center',templet:function(d){
                //                 if(d.variable==0){
                //                     return '<button type="button" lay-event="startDeployProcess" class="layui-btn layui-btn-normal">启动流程部署</button>'
                //                 }else{
                //                     return '<button type="" style="pointer-events: none;" class="layui-btn layui-btn-normal" >已禁用</button>'
                //                 }
                //             }}
                //     ]]
                // })
            }
        })
        return false;
    }

    //查看流程图
    function showProcessChart(data){

    }

    //打开修改的弹出层
    function openUpdateProcessLayer(data){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['750px','400px'],
            title:'修改用户',
            success:function(){
                $("#dataFrm")[0].reset();
                //装载新的数据
                form.val("dataFrm",data);
                url="/process/updateProcess";
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

    //删除
    function deleteProcess(data){
        layer.confirm('你确定要删除【'+data.title+'】这条请假单吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/process/deleteProcess",{id:data.id},function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }

});

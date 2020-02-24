
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
        elem: '#processTable'
        ,url:'/process/loadAllProcess'
        ,toolbar: '#processToolBar' //开启头部工具栏，并为其绑定左侧模板
        ,title: '流程部署信息'
        //,height:'full-250'
        ,page: true
        ,cols: [ [
            ,{type:'checkbox',align:'center'}
            ,{field:'ID_', title:'部署ID',align:'center',width:'300'}
            ,{field:'NAME_', title:'部署名称',align:'center',width:'300'}
            ,{field:'DEPLOY_TIME_', title:'部署时间',align:'center',width:'300'}
            ,{fixed: 'right', title:'操作', toolbar: '#processRowBar',align:'center',width:'200'}
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
        tableInProcdef.reload({
            page:{
                curr:1
            }
        })
        return false;
    });

    var mainIndex;
    var url;
    //添加流程管理
    form.on("submit(addProcesses)",function(){
        mainIndex=layer.open({
            type:1,
            content:$("#addProcesses"),
            area:['600px','400px'],
            title:'添加流程管理',
            success:function(){
                $("#dataFrm")[0].reset();
                url="/process/addProcess";
                //form.render("select");
            }
        });
        return false;
    });

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
                        , {field: 'id', title: 'ID', align: 'center'}
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


    //上传文件
    //多文件列表示例
    var demoListView = $('#demoList')
        ,uploadListIns = upload.render({
        elem: '#addProcessesFile'
        ,url: '/process/userProfileUpdate' //改成您自己的上传接口
        ,accept: 'file'
        ,exts: 'bpmn|png'  //允许上传的文件后缀。一般结合 accept 参数类设定
        ,multiple: true
        ,auto: false
        ,bindAction: '#doSubmit'
        ,before: function () {
            this.data = {
                processName: $('input[name="processName"]').val()
            }
        }
        ,choose: function(obj){
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function(index, file, result){
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td>'+ file.name +'</td>'
                    ,'<td>'+ (file.size/1024).toFixed(1) +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function(){
                    delete files[index]; //删除对应的文件
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                });

                demoListView.append(tr);
            });
        }
        ,done: function(res, index, upload){
           // alert("============"+res.code);
            if(res.code===200){ //上传成功
                //layer.alert("上传成功");
                var tr = demoListView.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(3).html(''); //清空操作
                delete this.files[index]; //删除文件队列已经上传成功的文件
            }else{
                this.error(index, upload);
            }
            //关闭窗口,无效
            mainIndex.close();
        }
        ,error: function(index, upload){
            var tr = demoListView.find('tr#upload-'+ index)
                ,tds = tr.children();
            tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
            tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
    });
    //指定允许上传的文件类型
    // upload.render({
    //     elem: '#addProcessesFile'
    //     ,url: '/process/userProfileUpdate' //改成您自己的上传接口
    //     ,accept: 'file' //普通文件
    //     ,multiple: true
    //     ,auto: false    //选择文件后不自动上传
    //     ,bindAction: '#doSubmit',
    //     //上传前的回调
    //     before: function () {
    //         this.data = {
    //             processName: $('input[name="processName"]').val()
    //         }
    //     },
    //     //选择文件后的回调
    //     // choose: function (obj) {
    //     //     obj.preview(function (index, file, result) {
    //     //         $('#preview').attr('src', result);
    //     //     })
    //     // },
    //     //操作成功的回调
    //     done: function(res){
    //     layer.msg('上传成功');
    //     console.log(res);
    //     },
    //     //上传错误回调
    //     error: function (index, upload) {
    //         layer.alert('上传失败！' + index);
    //     }
    // });


    //监听工具条的事件
    table.on("toolbar(processTable)",function(obj){
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
    table.on("tool(processTable)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'update':
                openUpdateProcessLayer(data);
                break;
            case 'delete':
                deleteProcess(data);
                break;
            case 'startDeployProcess':
                startDeployProcess(data);
                break;
        };
    });

    //启动流程部署
    function startDeployProcess(data){
        layer.confirm('你确定要部署【'+data.definitionname+'】这个流程吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/process/startDeployProcess",data,function(res){
                if(res.code==200){
                    deploymentProcesses.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
        return false;
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

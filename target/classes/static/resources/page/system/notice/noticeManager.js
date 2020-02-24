layui.use(['jquery','form','table','layer','laydate','layedit'],function(){
    var $=layui.jquery;
    var form=layui.form;
    var table=layui.table;
    var layer=layui.layer;
    var laydate=layui.laydate;
    var layedit=layui.layedit;
    //初始化时间选择器
    laydate.render({
        elem:'#startTime',
        type:'datetime'
    });
    laydate.render({
        elem:'#endTime',
        type:'datetime'
    });

    //初始化富文本
    var editIndex = layedit.build('content');
    //加载 数据
    var tableIns=table.render({
        elem: '#NoticeTable'
        ,url:'/Notice/loadAllNotice'
        ,toolbar: '#NoticeToolBar' //开启头部工具栏，并为其绑定左侧模板
        ,title: '公告数据表'
        ,height:'full-220'
        ,page: true
        ,cols: [ [
            {type: 'checkbox', fixed: 'left'}
            ,{field:'id', title:'ID',align:'center'}
            ,{field:'title', title:'标题',align:'center'}
            ,{field:'opername', title:'发布人',align:'center'}
            ,{field:'createtime', title:'发布时间',align:'center'}
            ,{fixed: 'right', title:'操作', toolbar: '#NoticeRowBar',align:'center'}
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
    table.on("toolbar(NoticeTable)",function(obj){
        switch(obj.event){
            case 'batchDelete':
                batchDelete();
                break;
            case 'add':
                openAddLayer();
                break;
        };
    });

    //监听行工具条的事件
    table.on("tool(NoticeTable)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'update':
                openUpdateNoticeLayer(data);
                break;
            case 'delete':
                deleteNotice(data);
                break;
            case 'show':
                showNotice(data);
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
            area:['700px','550px'],
            title:'发布公告',
            success:function(){
                $("#dataFrm")[0].reset();
                layedit.setContent(editIndex,"");
                url="/Notice/addNotice";
            }
        });
    }

    //打开修改的弹出层
    function openUpdateNoticeLayer(data){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['600px','400px'],
            title:'修改公告',
            success:function(){
                $("#dataFrm")[0].reset();
                //装载新的数据
                form.val("dataFrm",data);
                layedit.setContent(editIndex,data.content);
                url="/Notice/updateNotice";
            }
        });
    }

    //监听提交按钮事件
    form.on("submit(doSubmit)",function(data){
        //同步富文本和textarea里面的内容
        layedit.sync(editIndex);
        //通过表单系列化获取表单信息
        //var data=$("#dataFrm").serialize();
        $.post(url,data.field,function(res){
            if(res.code==200){
                tableIns.reload();
            }
            layer.msg(res.msg);
            layer.close(mainIndex);
        })
        return false;
    })
    //监听重置按钮事件
    $("#doReset").click(function(){
        $("#dataFrm")[0].reset();
        layedit.setContent(editIndex,"");
    })

    //删除
    function deleteNotice(data){
        layer.confirm('你确定要删除【'+data.title+'】这条公告吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/Notice/deleteNotice",{id:data.id},function(res){
                if(res.code==200){
                    tableIns.reload();
                }
                layer.msg(res.msg);
            })
            layer.close(index);
        });
    }

    //批量删除
    function  batchDelete(){
        //得到选中行
        var checkStatus = table.checkStatus('NoticeTable');
        var dataLength=checkStatus.data.length;
        if(dataLength>0){
            layer.confirm('你确定要删除这些公告数据吗?', {icon: 3, title:'提示'}, function(index){
                var data=checkStatus.data; //获取选中行的数据
                var ids="";
                $.each(data,function(index,item){
                    if(index==0){
                        ids+="ids="+item.id;
                    }else{
                        ids+="&ids="+item.id;
                    }
                })
                $.post("/Notice/batchDeleteNotice",ids,function(res){
                    if(res.code==200){
                        tableIns.reload();
                    }
                    layer.msg(res.msg);
                })
                layer.close(index);
            });

        }else{
            layer.msg("请选中操作行")
        }
    }

    //弹出查看层
    function showNotice(data){
        mainIndex=layer.open({
            type:1,
            content:$("#showNoticeDiv"),
            area:['600px','400px'],
            title:'查看公告',
            success:function(){
                $("#show_title").html(data.title);
                $("#show_opername").html(data.opername);
                $("#show_createtime").html(data.createtime);
                $("#show_content").html(data.content);
            }
        });
    }
});

var tableIns;
layui.use(['jquery','form','table','layer'],function(){
    var $=layui.jquery;
    var form=layui.form;
    var table=layui.table;
    var layer=layui.layer;
    //加载 数据
    tableIns=table.render({
        elem: '#customerTable'
        ,url:'/customer/loadAllCustomer'
        ,toolbar: '#customerToolBar' //开启头部工具栏，并为其绑定左侧模板
        ,title: '客户数据表'
        // ,height:'full-220'
        ,page: true
        ,cols: [ [
            {type:'checkbox',align:'center'}
            ,{field:'id', title:'ID',align:'center' ,width:'80'}
            ,{field:'customername', title:'客户名称',align:'center',width:'120'}
            ,{field:'zip', title:'邮编',align:'center',width:'120'}
            ,{field:'address', title:'客户地址',align:'center',width:'150'}
            ,{field:'telephone', title:'客户电话',align:'center',width:'150'}
            ,{field:'connectionperson', title:'联系人',align:'center',width:'120'}
            ,{field:'phone', title:'联系人电话',align:'center',width:'150'}
            ,{field:'bank', title:'开户行',align:'center',width:'120'}
            ,{field:'account', title:'账号',align:'center',width:'180'}
            ,{field:'email', title:'邮箱',align:'center',width:'150'}
            ,{field:'fax', title:'传真',align:'center',width:'120'}
            ,{field:'available', title:'是否可用',align:'center',width:'100',templet:function(d){
                return d.available==1?'<input type="checkbox" checked=" " name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">':'<input type="checkbox"  name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">';
             }}
            ,{fixed: 'right', title:'操作', toolbar: '#customerRowBar',align:'center',width:'200'}
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

    //监听客户状态的指定开关
    form.on('switch(switchTest)', function(data){
        var switchData={}
        if(this.checked){
            switchData.available=1
        }else{
            switchData.available=0
        }
        switchData.id=data.elem.name
        $.post("/customer/updateCustomerAvailable",switchData,function(res){
            if(res.code==200){
                layer.msg(res.msg);
            }else{
                layer.msg(res.msg);
            }
        })
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
    table.on("toolbar(customerTable)",function(obj){
        switch(obj.event){
            case 'add':
                openAddLayer();
                break;
            case 'batchDelete':
                batchDelete();
                break;
        };
    });

    //监听行工具条的事件
    table.on("tool(customerTable)",function(obj){
        var data = obj.data; //获得当前行数据
        switch(obj.event){
            case 'update':
                openUpdateCustomerLayer(data);
                break;
            case 'delete':
                deleteCustomer(data);
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
            title:'添加客户',
            success:function(){
                $("#dataFrm")[0].reset();
                url="/customer/addCustomer";
            }
        });
    }

    //打开修改的弹出层
    function openUpdateCustomerLayer(data){
        mainIndex=layer.open({
            type:1,
            content:$("#addOrUpdateDiv"),
            area:['800px','600px'],
            title:'修改客户',
            success:function(){
                $("#dataFrm")[0].reset();
                //装载新的数据
                form.val("dataFrm",data);
                url="/customer/updateCustomer";
            }
        });
    }

    //添加和修改的数据校验
    form.verify({
        telephone: function (value, item) {
            //手机号码和座机 https://blog.csdn.net/lhc_makefunny/article/details/78032602
            if(!/^(13|14|15|18|17)[0-9]{9}/.test(value) && !/0\d{2,3}-\d{7,8}/.test(value)){
                return "请填写正确手机号码或者座机号格式";
            }
        },
        zip: function (value, item) {
            if (!/^[0-9]{6}$/.test(value)) {
                return "邮编位数为6位";
            }
        },
        phone: function (value,item) {
            if(!/^(13|14|15|18|17)[0-9]{9}/.test(value) && !/0\d{2,3}-\d{7,8}/.test(value)){
                return "请填写正确手机号码或者座机号码格式";
            }
        },
        //http://tool.chinaz.com/regex/
        email: function (value,item) {
            if(!/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/.test(value)){
                return "请填写正确邮箱格式";
            }
        },
        account: function (value,item) {
            if(!/^(\d{15}|\d{16}|\d{18})$/.test(value)){
                return "请填写正确银行卡号位数";
            }
        },
        connectionperson: function (value,item) {
            if(!/^[\u4e00-\u9fa5.·\u36c3\u4DAE]{2,5}$/.test(value) && !/^[a-zA-Z]+[/s.]?([a-zA-Z]+[/s.]?){0,4}[a-zA-Z]$/.test(value)){
                return "请填写纯中文或纯英文的姓名";
            }
        }
    });

    //监听添加和修改的事件
    form.on("submit(doSubmit)",function(data){
        $.post(url,data.field,function(res){
            if(res.code==200){
                tableIns.reload();
                layer.close(mainIndex);
                layer.msg(res.msg);
            }else {
                layer.msg(res.msg);
            }
        })
        return false;
    });
    //删除
    function deleteCustomer(data){
        layer.confirm('你确定要删除【'+data.customername+'】这个客户吗?', {icon: 3, title:'提示'}, function(index){
            $.post("/customer/deleteCustomer",{id:data.id},function(res){
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
        var checkStatus = table.checkStatus('customerTable');
        var dataLength=checkStatus.data.length;
        if(dataLength>0){
            layer.confirm('你确定要删除这些客户数据吗?', {icon: 3, title:'提示'}, function(index){
                var data=checkStatus.data; //获取选中行的数据
                var ids="";
                $.each(data,function(index,item){
                    if(index==0){
                        ids+="ids="+item.id;
                    }else{
                        ids+="&ids="+item.id;
                    }
                })
                $.post("/customer/batchDeleteCustomer",ids,function(res){
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

});
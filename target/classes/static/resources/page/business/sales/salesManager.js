
var tableIns;
layui.use(['jquery','form','table','layer','laydate'],function(){
	var $=layui.jquery;
	var form=layui.form;
	var table=layui.table;
	var layer=layui.layer;
	var laydate=layui.laydate;
	//渲染时间选择器
	laydate.render({
		elem:'#startTime',
		type:'datetime'
	});
	laydate.render({
		elem:'#endTime',
		type:'datetime'
	});

	//加载 数据
	tableIns=table.render({
		elem: '#salesTable'
		,url:'/sales/loadAllSales'
		,toolbar: '#salesToolBar' //开启头部工具栏，并为其绑定左侧模板
		,title: '商品销售数据表'
		// ,height:'full-280'
		,page: true
		,cols: [ [
			{field:'id', title:'销售ID',align:'center' ,width:'80',sort: true}
			,{field:'customername', title:'客户名称',align:'center',width:'120'}
			,{field:'goodsname', title:'商品名称',align:'center',width:'120'}
			,{field:'salestime', title:'销售时间',align:'center',width:'120', sort: true}
			,{field:'paytype', title:'支付类型',align:'center',width:'120'}
			,{field:'number', title:'销售数量',align:'center',width:'100', sort: true}
			,{field:'salesprice', title:'销售价格',align:'center',width:'100', sort: true}
			,{field:'remark', title:'备注',align:'center',width:'120'}
			,{field:'operateperson', title:'操作员',align:'center',width:'100'}
			,{fixed: 'right', title:'操作', toolbar: '#salesRowBar',align:'center',width:'250'}
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

	//加载查询条件客户的下拉列表
	$.get("/customer/loadAllCustomerForSelect",function(res){
		var data=res.data;
		var dom=$("#search_customerid");
		var html='<option value="">请选择客户</option>'
		$.each(data,function(index,item){
			html+='<option value="'+item.id+'">'+item.customername+'</option>'
		});
		dom.html(html);
		form.render("select");
	});
	$.get("/goods/loadAllGoodsForSelect",function(res){
		var data=res.data;
		var dom=$("#search_goodsid");
		var html='<option value="">请选择商品</option>'
		$.each(data,function(index,item){
			html+='<option value="'+item.id+'">'+item.goodsname+'-'+item.size+'['+item.providername+']</option>'
		});
		dom.html(html);
		form.render("select");
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
	table.on("toolbar(salesTable)",function(obj){
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
	table.on("tool(salesTable)",function(obj){
		var data = obj.data; //获得当前行数据
		switch(obj.event){
			case 'update':
				openUpdateSalesLayer(data);
				break;
			case 'delete':
				deleteSales(data);
				break;
			case 'back':
				back(data);
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
			title:'添加商品销售',
			success:function(){
				$("#dataFrm")[0].reset();
				url="/sales/addSales";
				initCustomerSelect();
				$(".mydiv").hide();
			}
		});
	}

	//初始化添加和修改页面的下拉列表
	function initCustomerSelect(customerid){
		var dom=$("#goodsid");
		dom.html("");
		$.get("/customer/loadAllCustomerForSelect",function(res){
			var data=res.data;
			var dom=$("#customerid");
			var html='<option value="">请选择客户</option>'
			$.each(data,function(index,item){
				html+='<option value="'+item.id+'">'+item.customername+'</option>'
			});
			dom.html(html);
			//如果customerid有值就反选
			if(customerid!=undefined){
				dom.val(customerid);
			}
			form.render("select");
		});
	}
	//监听添加页面的客户下拉框的改变事件
	form.on('select(customerid)', function(data){
		initGoodsSelect();
	});

	//根据客户加载商品下拉列表
	function initGoodsSelect(goodsid){
		$.get("/goods/loadGoodsByProviderId",function(res){
			var data=res.data;
			var dom=$("#goodsid");
			var html='<option value="">请选择商品</option>'
			$.each(data,function(index,item){
				html+='<option value="'+item.id+'">'+item.goodsname+'-'+item.size+'['+item.providername+']</option>'
			});
			dom.html(html);
			//如果goodsid有值就反选
			if(goodsid!=undefined){
				dom.val(goodsid);
			}
			form.render("select");
		});
	}



	//打开修改的弹出层
	function openUpdateSalesLayer(data){
		mainIndex=layer.open({
			type:1,
			content:$("#addOrUpdateDiv"),
			area:['800px','600px'],
			title:'修改商品销售',
			success:function(){
				$("#dataFrm")[0].reset();
				//装载新的数据
				form.val("dataFrm",data);
				initCustomerSelect(data.customerid);
				initGoodsSelect(data.customerid,data.goodsid);
				url="/sales/updateSales";
				//禁用客户和商品的下拉框
				$(".mydiv").show();

			}
		});
	}

	form.on("submit(doSubmit)",function(data){
		$.post(url,data.field,function(res){
			if(res.code==200){
				tableIns.reload();
				layer.msg(res.msg);
				layer.close(mainIndex);
			}else {
				layer.msg(res.msg);
			}

		})
		return false;
	});
	//删除
	function deleteSales(data){
		layer.confirm('你确定要删除这个销售商品吗?', {icon: 3, title:'提示'}, function(index){
			$.post("/sales/deleteSales",{id:data.id},function(res){
				if(res.code==200){
					tableIns.reload();
				}
				layer.msg(res.msg);
			})
			layer.close(index);
		});
	}

	//打开销售退货的弹出层
	function back(data){
		mainIndex=layer.open({
			type:1,
			content:$("#backGoodsDiv"),
			area:['800px','500px'],
			title:'商品退货',
			success:function(){
				$("#dataBackFrm")[0].reset();
				//装载新的数据
				form.val("dataBackFrm",{id:data.id,currentnumber:data.number});
			}
		});
	}

	//自定义验证规则
	form.verify({
		checknumber: function(value){
			var currentnumber=parseInt($("#currentnumber").val());
			if(parseInt(value)>currentnumber||parseInt(value)<1){
				return '退货数量只能在【0】-【'+currentnumber+"】数量之间";
			}
		},
		addCustomerid:function(value){
			if(value===null||value===""){
				return "请选择客户"
			}
		},
		//定位到下拉框 https://fly.layui.com/jie/22496/
		addGoodsid:function(value){
			if(value===null||value===""){
				return "请选择商品名称"
			}
		},
	});

	//退货提交
	form.on("submit(doBackSubmit)",function(data){
		$.post("/salesback/addSalesBack",data.field,function(res){
			if(res.code==200){
				tableIns.reload();
			}
			layer.msg(res.msg);
			layer.close(mainIndex);
		})
		return false;
	});
});
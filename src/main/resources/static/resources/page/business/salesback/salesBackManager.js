
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
		elem: '#salesbackTable'
		,url:'/salesback/loadAllsalesback'
		,toolbar: true //开启头部工具栏，并为其绑定左侧模板
		,title: '商品退货数据表'
		// ,height:'full-280'
		,page: true
		,cols: [ [
			{field:'id', title:'退货ID',align:'center',width:'100' }
			,{field:'customername', title:'客户',align:'center',width:'120'}
			,{field:'goodsname', title:'商品名称',align:'center',width:'120'}
			,{field:'salesbacktime', title:'退货时间',align:'center',width:'180'}
			,{field:'paytype', title:'支付类型',align:'center',width:'120'}
			,{field:'number', title:'退货数量',align:'center',width:'120'}
			,{field:'salesbackprice', title:'退货价格',align:'center',width:'120'}
			,{field:'remark', title:'备注',align:'center',width:'100'}
			,{field:'operateperson', title:'操作员',align:'center',width:'120'}
		] ]
	});

	//加载查询条件客户的下拉列表
	$.get("/customer/loadAllCustomerForSelect",function(res){
		var data=res.data;
		var dom=$("#search_customerid");
		var html='<option value="0">请选择客户</option>'
		$.each(data,function(index,item){
			html+='<option value="'+item.id+'">'+item.customername+'</option>'
		});
		dom.html(html);
		form.render("select");
	});
	$.get("/goods/loadAllGoodsForSelect",function(res){
		var data=res.data;
		var dom=$("#search_goodsid");
		var html='<option value="0">请选择商品</option>'
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
});
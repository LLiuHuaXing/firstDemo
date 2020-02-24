
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
		elem: '#outportTable'
		,url:'/outport/loadAllOutport'
		,toolbar: true //开启头部工具栏，并为其绑定左侧模板
		,title: '商品退货数据表'
		// ,height:'full-280'
		,page: true
		,cols: [ [
			{field:'id', title:'退货ID',align:'center',width:'100' }
			,{field:'providername', title:'供应商',align:'center',width:'120'}
			,{field:'goodsname', title:'商品名称',align:'center',width:'120'}
			,{field:'size', title:'商品规格',align:'center',width:'120'}
			,{field:'outputtime', title:'退货时间',align:'center',width:'180'}
			,{field:'number', title:'退货数量',align:'center',width:'120'}
			,{field:'outportprice', title:'退货价格',align:'center',width:'120'}
			,{field:'remark', title:'备注',align:'center',width:'100'}
			,{field:'operateperson', title:'操作员',align:'center',width:'120'}
		] ]
	});

	//加载查询条件供应商的下拉列表
	$.get("/provider/loadAllProviderForSelect",function(res){
		var data=res.data;
		var dom=$("#search_providerid");
		var html='<option value="0">请选择供应商</option>'
		$.each(data,function(index,item){
			html+='<option value="'+item.id+'">'+item.providername+'</option>'
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
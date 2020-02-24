
var tableIns;
layui.use(['jquery','form','table','layer','upload'],function(){
	var $=layui.jquery;
	var form=layui.form;
	var table=layui.table;
	var layer=layui.layer;
	var upload=layui.upload;
	//加载 数据
	tableIns=table.render({
		elem: '#goodsTable'
		,url:'/goods/loadAllGoods'
		,toolbar: '#goodsToolBar' //开启头部工具栏，并为其绑定左侧模板
		,title: '商品数据表'
		// ,height:'full-280'
		,page: true
		,cols: [ [
			{type:'checkbox',align:'center'}
			,{field:'id', title:'ID',align:'center' ,width:'80'}
			,{field:'goodsname', title:'商品名称',align:'center',width:'150'}
			,{field:'providername', title:'供应商',align:'center',width:'120'}
			,{field:'produceplace', title:'产地',align:'center',width:'120'}
			,{field:'size', title:'商品规格',align:'center',width:'120'}
			,{field:'goodspackage', title:'商品包装',align:'center',width:'120'}
			,{field:'productcode', title:'生产批号',align:'center',width:'100'}
			,{field:'promitcode', title:'批准文号',align:'center',width:'100'}
			,{field:'description', title:'商品描述',align:'center',width:'120'}
			,{field:'price', title:'商品价格',align:'center',width:'100'}
			,{field:'number', title:'库存量',align:'center',width:'100'}
			,{field:'dangernum', title:'预警库存',align:'center',width:'100'}
			,{field:'goodsimg', title:'商品图片',align:'center',width:'100', templet:function(d){
				return '<img width=40 height=40 src=/file/showImageByPath?path='+d.goodsimg.substring(d.goodsimg.lastIndexOf(",")+1,d.goodsimg.length)+ ' />';
			}}
			,{field:'available', title:'是否可用',align:'center',width:'100',templet:function(d){
				return d.available==1?'<input type="checkbox" checked=" " name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">':'<input type="checkbox"  name="'+d.id+'" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">';
			}}
			,{fixed: 'right', title:'操作', toolbar: '#goodsRowBar',align:'center',width:'280'}
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

	//监听指定开关
	form.on('switch(switchTest)', function(data){
		var switchData={}
		if(this.checked){
			switchData.available=1
		}else{
			switchData.available=0
		}
		switchData.id=data.elem.name
		$.post("/goods/updateGoodsAvailable",switchData,function(res){
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
	table.on("toolbar(goodsTable)",function(obj){
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
	table.on("tool(goodsTable)",function(obj){
		var data = obj.data; //获得当前行数据
		switch(obj.event){
			case 'update':
				openUpdateGoodsLayer(data);
				break;
			case 'delete':
				deleteGoods(data);
				break;
			case 'showImg':
				showImg(data);
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
			title:'添加商品',
			success:function(){
				$("#dataFrm")[0].reset();
				url="/goods/addGoods";
				$.get("/provider/loadAllProviderForSelect",function(res){
					var data=res.data;
					var dom=$("#providerid");
					var html='<option value="0">请选择供应商</option>'
					$.each(data,function(index,item){
						html+='<option value="'+item.id+'">'+item.providername+'</option>'
					});
					dom.html(html);
					form.render("select");
				});
				//设置默认图片
				$(".thumbImg").attr("src",'/file/showImageByPath?path=images/defaultgoodsimg.jpg');
				$("#goodsimg").val('images/defaultgoodsimg.jpg');
			}
		});
	}

	//打开修改的弹出层
	function openUpdateGoodsLayer(data){
		mainIndex=layer.open({
			type:1,
			content:$("#addOrUpdateDiv"),
			area:['800px','600px'],
			title:'修改商品',
			success:function(){
				$("#dataFrm")[0].reset();
				//装载新的数据
				form.val("dataFrm",data);
				url="/goods/updateGoods";
				$.get("/provider/loadAllProviderForSelect",function(res){
					var redata=res.data;
					var dom=$("#providerid");
					var html='<option value="0">请选择供应商</option>'
					$.each(redata,function(index,item){
						if(data.providerid===item.id){
							html+='<option value="'+item.id+'" selected>'+item.providername+'</option>'
						}else{
							html+='<option value="'+item.id+'">'+item.providername+'</option>'
						}
					});
					dom.html(html);
					form.render("select");
				});
				$(".thumbImg").attr("src",'/file/showImageByPath?path='+data.goodsimg);
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

	//文件上传
	upload.render({
		elem: '.thumbBox',
		url: '/file/uploadFile',
		acceptMime:'image/*',
		field:'mf',
		method : "post",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
		done: function(res, index, upload){
			var path=res.path;
			$('.thumbImg').attr('src','/file/showImageByPath?path='+path);
			$('.thumbBox').css("background","#fff");
			$("#goodsimg").val(path);//给隐藏域赋值
		}
	});

	var imgPath
	//查看图片
	function showImg(data) {
		imgPath=data.goodsimg.split(",");
		mainIndex=layer.open({
			type:1,
			content:$("#showGoodsImg"),
			area:['650px','550px'],
			title:'查看商品图片',
			success:function(){
				//设置默认图片
				$(".showThumbImg").attr("src",'/file/showImageByPath?path='+imgPath[0]);
				$("#findGoodsImg").val(0);
			}
		});
	}

	//监听查看图片事件
	$("#upShowDoSubmit").click(function () {
		var ImgArrayIndexStr=$("#findGoodsImg").attr("value");
		var ImgArrayIndex= parseInt(ImgArrayIndexStr)-1;
		if(0===ImgArrayIndex){
			$("#upShowDoSubmit").attr('disabled',true);
            $("#upShowDoSubmit").css("background","#c2c2c2");
			$("#downShowDoSubmit").attr('disabled',false);
            $("#downShowDoSubmit").css("background","#009688");
		}
		$(".showThumbImg").attr("src",'/file/showImageByPath?path='+imgPath[ImgArrayIndex]);
		$("#findGoodsImg").val(ImgArrayIndex);
	})

	$("#downShowDoSubmit").click(function () {
		var ImgArrayIndexStr=$("#findGoodsImg").attr("value");
		// js中字符串转数字的方法 https://www.cnblogs.com/yunshengz/p/7084110.html
		var ImgArrayIndex = parseInt(ImgArrayIndexStr)+1;
		if(ImgArrayIndex === (imgPath.length-1)){
			//https://blog.csdn.net/xinghuo0007/article/details/53428333
			$("#downShowDoSubmit").attr('disabled',true);
            $("#downShowDoSubmit").css("background","#c2c2c2");
			$("#upShowDoSubmit").attr('disabled',false);
            $("#upShowDoSubmit").css("background","#009688");
		}
		$(".showThumbImg").attr("src",'/file/showImageByPath?path='+imgPath[ImgArrayIndex]);
		$("#findGoodsImg").val(ImgArrayIndex);

	})
	//删除
	function deleteGoods(data){
		layer.confirm('你确定要删除【'+data.goodsname+'】这个商品吗?', {icon: 3, title:'提示'}, function(index){
			$.post("/goods/deleteGoods",{id:data.id,goodsimg:data.goodsimg},function(res){
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
		var checkStatus = table.checkStatus('goodsTable');
		var dataLength=checkStatus.data.length;
		if(dataLength>0){
			layer.confirm('你确定要删除这些商品数据吗?', {icon: 3, title:'提示'}, function(index){
				var data=checkStatus.data; //获取选中行的数据
				var ids="";
				$.each(data,function(index,item) {
					if(index==0){
						ids+="ids="+item.id+"&goodsImgS="+item.goodsimg;
					}else{
						ids+="&ids="+item.id+"&goodsImgS="+item.goodsimg;
					}
				})

				$.post("/goods/batchDeleteGoods",ids,function(res){
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

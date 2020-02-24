
	layui.use(['jquery','form','table','layer'],function(){
		var $=layui.jquery;
		var form=layui.form;
		var table=layui.table;
		var layer=layui.layer;
		//加载 数据
		var tableIns=table.render({
			elem: '#cacheTable'
			,url:'/cache/loadAllCache'
			,toolbar: '#cacheToolBar' //开启头部工具栏，并为其绑定左侧模板
			,title: '缓存数据表'
			,page: true
			,cols: [ [
				{field:'key', title:'ID',align:'center',width:'100'}
				,{field:'value', title:'标题',align:'center',width:'1000'}
				,{fixed: 'right', title:'操作', toolbar: '#cacheRowBar',align:'center',width:'100'}
			] ]
		});

		//监听工具条的事件
		table.on("toolbar(cacheTable)",function(obj){
			switch(obj.event){
				case 'removeAll':
					removeAll();
					break;
				case 'syncCache':
					syncCache();
					break;
			};
		});

		//监听行工具条的事件
		table.on("tool(cacheTable)",function(obj){
			var data = obj.data; //获得当前行数据
			switch(obj.event){
				case 'delete':
					deleteCache(data);
					break;
			};
		});

		//删除
		function deleteCache(data){
			layer.confirm('你确定要删除【'+data.key+'】这条缓存吗?', {icon: 3, title:'提示'}, function(index){
				$.post("/cache/deleteCache",{key:data.key},function(res){
					if(res.code==200){
						tableIns.reload();
					}
					layer.msg(res.msg);
				})
				layer.close(index);
			});
		}

		//清空缓存
		function removeAll(){
			layer.confirm('你确定要清空所有缓存数据吗?', {icon: 3, title:'提示'}, function(index){
				$.post("/cache/removeAllCache",function(res){
					if(res.code==200){
						tableIns.reload();
					}
					layer.msg(res.msg);
				})
			});
		}
		//同步缓存
		function syncCache(){
			layer.confirm('你确定要同步所有缓存数据吗?', {icon: 3, title:'提示'}, function(index){
				$.post("/cache/syncCache",function(res){
					if(res.code==200){
						tableIns.reload();
					}
					layer.msg(res.msg);
				})
				layer.close(index);
			});
		}
	});
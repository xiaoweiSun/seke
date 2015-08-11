	var arr;
	var now;
	function test() {
			var search = window.location.search;
			var tdbname = getParameter(search, "tdbname");
			if (tdbname != null) {
				$('#mytdb').html("<h2>your knowledge store in  "+tdbname+"</h2>");
			}
	}
	function getTdb() {
		$.ajax({
			url : "/KWL/MainServlet",
			data : {
				type : 19
			},
			type : "post",
			dataType : "json",
			async : false,
			error : function(json) {
				alert("error");
			},
			success : function (json) {
				if (json.res == "0") {
					listTdbs(json.data);
				} else {
					alert(json.data);
				}
			}
		});
	}
	function listTdbs(data) {
		arr = data;
		$.each(data, function(i, tdb) {
			//tdb已经是一个数组
			if (tdb[3] == "complete") {
				$('#tdbFinished').append("<tr><td>"+tdb[0]+"</td><td><p>100%</p></td><td><a href='javascript:void(0)'><img onclick=\"showTDB('"+i+"')\" src=\"images\/showtops.png\" \/><\/a></td><td><a href='javascript:void(0)'><img onclick=\"modifyTDB('"+i+"')\" src=\"images\/showmodify.png\" \/><\/a></td></tr>");
			} else {
				var info;
				if (tdb[3] == "building") {
					info = "已创建";
				} else {
					info = "正在执行中";
				}
				$('#tdbProcessing').append("<tr><td>"+tdb[0]+"</td><td><p>"+info+"</p></td><td><a href='javascript:void(0)'><img onclick=\"showTDB('"+i+"')\" src=\"images\/showtops.png\" \/><\/a></td><td><a href='javascript:void(0)'><img onclick=\"modifyTDB('"+i+"')\" src=\"images\/showmodify.png\" \/><\/a></td></tr>");
			}
		});
	};
	
	function showTDB(i) {
		$('#table1').hide();
		$('#table2').hide();
		$('#table3').show();
		$('#fieldName').html(arr[i][0]);
		$('#tag').html(arr[i][1]);
		$('#description').html(arr[i][2]);
		now = arr[i];
		defaultDo();
	}
	function defaultDo() {
		$('#sparqlIn').val("select * {?a ?b ?c} limit 100");
	};
	function modifyTDB(i) {
		document.cookie = "fieldName="+arr[i][0];
		document.cookie = "tag=" + arr[i][1];
		document.cookie = "description=" + arr[i][2];
		window.location.href = "/KWL/field.jsp?modify=yes";
	};
	//查sparql
	function query(){
		
		var sparqlIn = $('#sparqlIn').val();
		var tdbname = now[0];
		queryDo(sparqlIn, tdbname);
	};
	//实际动作
	function queryDo(sparqlIn, tdbname) {
		$.ajax({
			//请求接收方相对根路径
			url: "/KWL/MainServlet",
			data: {
				type : 20,
				sparqlIn : sparqlIn,
				tdbname : tdbname
			},
//			processData : false,
			//POST方法提交
			type : "POST",
			//同步
			async : false,
			//回调数据格式
			dataType : "json",
			//调用成功函数
			success : function( json) {
				showResult(json);
			},
			//调用失败函数
			error : function( xhr, status ) {
				alert("sorry, there was a problem");
			},
			//调用完成函数
			complete : function(xhr, status) {
//				alert("The Request is complete!");
			}
		});
	};
	
	function showResult(json) {
			//创建分割线
		$('#table').html("");
		$('#table').append("<tr><th>来源文件</th><th>属性</th><th>属性值</th></tr>");
			//创建表格
			$.each(json,function(i, record) {
				//读取每个属性
				//创建表行
				$('#table').append('<tr id = \"'+i+'\" ></tr>');
				//创建标签单元格
				$.each(record, function(j, value) {
						$('#'+i).append('<td><p><b>'+value+'</b></p></td>');
				});
			});
	}
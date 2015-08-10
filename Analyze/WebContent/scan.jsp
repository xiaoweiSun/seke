<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/ext-theme-neptune-all.css">
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script type="text/javascript" src="script/ext-all.js"></script>
<script type="text/javascript" src="script/ext-theme-neptune.js"></script>
<script type ="text/javascript" src="script/app.js"></script>
<script type="text/javascript">
function listTask() {
	$.ajax({
		url: "/Analyze/AnalyzeHtml",
		data: {
			method: "listTask",
			type:6
		},
		type : "POST",
		async : false,
		dataType : "json",
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success : function( json) {
			console.log(json);
			showTaskName(json);
		},
		error : function( xhr, status ) {
			return false;
		},
		complete : function(xhr, status) {
			console.log("complete");
		}
	});

}

function scan() {
	var index = document.getElementById("task").value;
	var time = array[index][1];
	$.ajax({
		url: "/Analyze/AnalyzeHtml",
		data: {
			time:time,
			method: "scan",
			type:7
		},
		type : "POST",
		async : false,
		dataType : "json",
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success : function( json) {
			console.log(json);
			load(json);
		},
		error : function( xhr, status ) {
			return false;
		},
		complete : function(xhr, status) {
			console.log("complete");
		}
	});

}
var array;
function showTaskName(json) {
	$('#task').html("");
	array = json;
	console.log(json + "adf");
	$.each(json, function(j, file) {
		console.log(file);
		if (j==0) {
			$('#task').append('<option selected ="selected" value = "'+j+'">'+file[0]+'</option>');
		} else {
			$('#task').append('<option value = "'+j+'">'+file[0]+'</option>');
		}
	});
	
};
</script>
</head>
<body>
<a href="create.jsp">创建模板</a>
<a href="extract.jsp">知识抽取</a>
<a href="scan.jsp">结果查询</a>
<br>
<button onclick="listTask()">列出已有任务名称</button>
任务名称<select id="task"></select>
<button onclick="scan()">浏览内容</button>
<div id="content"></div>
</body>
</html>
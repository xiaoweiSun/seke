<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script type="text/javascript">
function scanTemplates() {
	$.ajax({
		url: "/Analyze/AnalyzeHtml",
		data: {
			method: "scanTemplates",
			type: 4
		},
		type : "POST",
		async : false,
		dataType : "json",
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success : function( json) {
			console.log(json);
			showTemplates(json);
		},
		error : function( xhr, status ) {
			return false;
		},
		complete : function(xhr, status) {
			console.log("complete");
		}
	});
}

function parse() {
	//重新清洗下结果数据项
	var site = document.getElementById("site").value;
	var index = document.getElementById("templates").value;
	var ruleStr = array[index][2];
	var name = document.getElementById("taskName").value;
	var count = document.getElementById("count").value;
	$.ajax({
		url: "/Analyze/AnalyzeHtml",
		data: {
			ruleStr: ruleStr,
			taskName:name,
			site:site,
			count:count,
			method: "parse",
			type:5
		},
		type : "POST",
		async : false,
		dataType : "json",
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success : function( json) {
			console.log(json);
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
function showTemplates(json) {
	$('#templates').html("");
	array = json;
	console.log(json);
	$.each(json, function(j, file) {
		console.log(file);
		if (j==0) {
			$('#templates').append('<option selected ="selected" value = "'+j+'">'+file[0]+'</option>');
		} else {
			$('#templates').append('<option value = "'+j+'">'+file[0]+'</option>');
		}
	});
	
};
</script>
</head>
<body>
本次任务名称<input id="taskName"></input>
<br/>
<button onclick="scanTemplates()">显示所有模板</button>
<br/>
选择一个模板<select id="templates">
</select>
<p>目标站点<input id = "site"></input> </p>
<input id="count">最大抽取网页数</input>
<br/>

<button onclick="parse()">开始抽取</button>
</body>
</html>
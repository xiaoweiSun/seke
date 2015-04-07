function trim(str){
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
function getParameter(request, key) {
	if (request == null) return null;
	var sessionS = request.indexOf(key+"=", 0);
	if (sessionS == -1) {
		console.log("get Parameter "+key+" error: doesn't exists!");
		return null;
	}
	sessionS += key.length + 1;
	var sessionE = request.indexOf("&", sessionS);
	if (sessionE == -1) {
		return request.substring(sessionS);
	} else {
		return request.substring(sessionS, sessionE);
	}
}
function checkLoginState(fun) {
	var username = getParameterFromCookie("username");
	var session = getParameterFromCookie("session");
	if(username==null || session==null){
		window.confirm("请您先登录!");
		return;
	}
	
	$.ajax({
			
			url: "/KWL/MainServlet",
			data: {
				type : 23,
				username : username,
				session :	session
			},
//			processData : false,
			type : "POST",
			async : false,
			dataType : "json",
			success : function( json) {
				if(json.data==false) {
					window.confirm("登录已超时，请重新登录！");
				}else {
					fun();
				}
			},
			error : function( xhr, status ) {
				return false;
			},
			complete : function(xhr, status) {
//				alert("The Request is complete!");
			}
	});
}
function getParameterFromCookie(key) {
	var strCookie = document.cookie;
	var arrCookie = strCookie.split(";");
	for (var i=0; i<arrCookie.length; i++) {
		var arr= arrCookie[i].split("=");
		if (trim(arr[0]) == key) {
			if (trim(arr[1]) == "" || trim(arr[1]) == "null") return null;
			return trim(arr[1]);
		}
	}
	console.log("can not find "+key+" from cookie");
	return null;
}

function delCookie(name){//为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
	var date = new Date();
	date.setTime(date.getTime() - 10000);
	document.cookie = name + "=a; expires=" + date.toGMTString();
}
closelogin = function(str){
	document.getElementById("login").style.display="none";
	var username = getParameter(str,"username");
	var session = getParameter(str,"session");
	document.cookie = "username="+username;
	document.cookie = "session="+session;
	console.log("login");
	checklogin();
};
openlogin = function(){
	console.log("openlogin");
	document.getElementById("login").style.display="";
	document.getElementById("register").style.display="none";
};
openregister = function(){
	document.getElementById("login").style.display="none";
	document.getElementById("register").style.display="";
};
closeregister = function(){
	document.getElementById("register").style.display="none";
};
openlogout = function() {
	delCookie("username");
	console.log("logout");
	checklogin();
};
login = function(){
		var oXMLHttp = new XMLHttpRequest();
		oXMLHttp.open("post","MainServlet",true);
		// ע����������XMLHttp���������ͷContent-Type������    
		oXMLHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");    
		oXMLHttp.onreadystatechange = function() {    
			if (oXMLHttp.readyState == 4) {    
				if (oXMLHttp.status == 200) {
					
					if (oXMLHttp.responseText.length>10){
						alert("登录成功!");
						//document.getElementById("wrong_password").style.display = "none";
						closelogin("session="+oXMLHttp.responseText); 	
					}else{
						//document.getElementById("wrong_password").style.display = "";
					}
				}else {    
					alert("An error occurred: " + oXMLHttp.statusText);    
				}    
			}    
		};
		var username = document.getElementById("login_username").value;
		var password = document.getElementById("login_password").value;
		if(username.length*password.length==0){
			alert("password or user is null��");
			return;
		}
		var sendContent='type=6';
		sendContent+='&username='+username;
		sendContent+='&password='+password;
		console.log("Send:"+sendContent);
		oXMLHttp.send(sendContent);
};
register = function(){
	var username = document.getElementById("register_username").value;
	var password = document.getElementById("register_password").value;
	var retypepassword = document.getElementById("register_retype_password").value;
	if(username==null ){
		alert("enter username please");
		return;
	}else if(password == null){
		alert("enter password");
		return;
	}else if(password != retypepassword){
		alert("enter password again");
		return;
	}else{
		var oXMLHttp = new XMLHttpRequest();
		oXMLHttp.open("post","MainServlet",true);
		// ע����������XMLHttp���������ͷContent-Type������    
		oXMLHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");    
		oXMLHttp.onreadystatechange = function() {    
		if (oXMLHttp.readyState == 4) {    
			if (oXMLHttp.status == 200) {
				
				if (oXMLHttp.responseText.length<5){
					alert("failed��");
				}else{
					closeregister();
					window.location.href='index.jsp';
				}
			}else {    
				alert("An error occurred: " + oXMLHttp.statusText);    
			}    
		}    
		};
		var sendContent='type=7';
		sendContent+='&username='+username;
		sendContent+='&password='+password;
		console.log("Send:"+sendContent);
		oXMLHttp.send(sendContent);
	}

};

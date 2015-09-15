<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		
		<title>找工作</title>
		
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
		<script src="bootstrap/js/bootstrap.min.js"></script>
		<script src="bootstrap/js/jquery-2.1.4.min.js"></script>
		<style type="text/css">
			body {
			  padding-top: 40px;
			  padding-bottom: 40px;
			  background-color: #eee;
			}
			
			.form-signin {
			  max-width: 330px;
			  padding: 15px;
			  margin: 0 auto;
			}
			.form-signin .form-signin-heading,
			.form-signin .checkbox {
			  margin-bottom: 10px;
			}
			.form-signin .checkbox {
			  font-weight: normal;
			}
			.form-signin .form-control {
			  position: relative;
			  height: auto;
			  -webkit-box-sizing: border-box;
			     -moz-box-sizing: border-box;
			          box-sizing: border-box;
			  padding: 10px;
			  font-size: 16px;
			}
			.form-signin .form-control:focus {
			  z-index: 2;
			}
			.form-signin input[type="email"] {
			  margin-bottom: -1px;
			  border-bottom-right-radius: 0;
			  border-bottom-left-radius: 0;
			}
			.form-signin input[type="password"] {
			  margin-bottom: 10px;
			  border-top-left-radius: 0;
			  border-top-right-radius: 0;
			}
		</style>
	</head>
	
	<body>
		<div class="container">
	
			<form class="form-signin" action="regist" method="post">
				<h2 class="form-signin-heading">注册-找工作</h2>
				<label for="inputEmail" class="sr-only">Username</label>
				<input type="text" name="username" class="form-control"
					placeholder="Username" required autofocus>
				<label for="inputPassword" class="sr-only">Password</label>
				<input type="password" name="password" class="form-control"
					placeholder="Password" required>
				<button class="btn btn-lg btn-primary btn-block" type="submit">注册</button>
			</form>
	
		</div><!-- /container -->
	</body>
</html>

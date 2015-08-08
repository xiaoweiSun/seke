<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<style>
		body {
			padding-top: 50px;		
		}	
		
		.starter{
			padding: 40px 15px;
			text-align: center;
		}
	</style>
  </head>
  
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">找工作</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<c:if test="${empty user }">
				<form action="login" method="post" class="navbar-form navbar-right">
					<div class="form-group">
						<input name="username" type="text" placeholder="Email"
							class="form-control">
					</div>
					<div class="form-group">
						<input name="password" type="password" placeholder="Password"
							class="form-control">
					</div>
					<button type="submit" class="btn btn-success">登录</button>
					<button type="button" class="btn btn-success">注册</button>
				</form>
			</c:if>
			<c:if test="${not empty user }">
				<li class="dropdown navbar-right">
					<a class="navbar-brand" href="#">${user.username }</a>
				</li>
			</c:if>
		</div>
		<!--/.navbar-collapse -->
	</div>
	</nav>

	<div class="container">
		<div class="starter">
			<form class="form-search">
				<div class="form-group">
					<input type="text" class="input-medium search-query" placeholder="关键词">
					<input type="text" class="input-medium search-query" placeholder="城市">
					<button type="submit" class="btn">Search</button>
				</div>
			</form>
		</div>
	</div><!-- /.container -->
</body>
</html>

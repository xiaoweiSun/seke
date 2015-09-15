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
						<button type="button" class="btn btn-success" onclick="javascript:window.location.href='regist'">注册</button>
					</form>
				</c:if>
				
				<!-- 个人下拉列表 -->
				<c:if test="${not empty user }">
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							 <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="true">
								${user.username }
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menu">
								<c:if test="${user.hasResume == 0 }">
									<li><a href="addResume">添加简历</a></li>
								</c:if>
								<c:if test="${user.hasResume == 1 }">
									<li><a href="checkResume">查看简历</a></li>
								</c:if>
								<li role="separator" class="divider"></li>
								<li><a href="logout">退出</a></li>
							</ul>
						</li>
					</ul>
				</c:if>
			</div>
			<!--/.navbar-collapse -->
		</div>
	</nav>

	<div class="container center">
		<div class="starter">
			<!-- 搜索栏 -->
			<form action="search" method="post" class="form-search">
				<div class="form-group">
					<table>
						<tr>
							<td width="250px">
								<input style="width:200px" type="text" name="key" value="${key }" class="form-control" placeholder="关键词">
							</td>
							<td width="250px">
								<input style="width:200px" type="text" name="city" value="${city }" class="form-control" placeholder="城市">
								<input type="hidden" name="offset" value="0">
							</td>
							<td>
								<button type="submit" class="btn">Search</button>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<!-- 分页 -->
	  	<ul class="pagination">
	  		<c:if test="${searchResult.offset > 0 }">
	  			<li><a href="search?key=${key }&city=${city}&offset=${searchResult.offset-searchResult.size}">&laquo;</a></li>
	  		</c:if>
	  		<c:forEach var="id" begin="0" end="9">
	  			<c:if test="${id * searchResult.size < searchResult.total }">
	  				<c:if test="${id * searchResult.size != searchResult.offset }">
	  					<li><a href="search?key=${key }&city=${city}&offset=${id*searchResult.size}">${id+1}</a></li>
	  				</c:if>
	  				<c:if test="${id * searchResult.size == searchResult.offset }">
	  					<li class="active"><a href="javascript:void(0)">${id+1}<span class="sr-only">(current)</span></a></li>
	  				</c:if>
	  			</c:if>
	  		</c:forEach>
	  		<c:if test="${searchResult.offset+searchResult.size<searchResult.total }">
	  			<li><a href="search?key=${key}&city=${city}&offset=${searchResult.offset+searchResult.size}">&raquo;</a></li>
	  		</c:if>
	  	</ul>
	  	
	  	<!-- 搜索结果列表 -->
		<c:forEach items="${searchResult.datas}" var="data" varStatus="index">
	  		<h4><a href="${data.url }">${data.jobName}</a></h4>
	  		<p style="width:600px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${data.jobDescription }</p>
	  	</c:forEach>
		  	
		<!-- 分页 -->
	  	<ul class="pagination">
	  		<c:if test="${searchResult.offset > 0 }">
	  			<li><a href="search?key=${key }&city=${city}&offset=${searchResult.offset-searchResult.size}">&laquo;</a></li>
	  		</c:if>
	  		<c:forEach var="id" begin="0" end="9">
	  			<c:if test="${id * searchResult.size < searchResult.total }">
	  				<li><a href="search?key=${key }&city=${city}&offset=${id*searchResult.size}">${id+1}</a></li>
	  			</c:if>
	  		</c:forEach>
	  		<c:if test="${searchResult.offset+searchResult.size<searchResult.total }">
	  			<li><a href="search?key=${key}&city=${city}&offset=${searchResult.offset+searchResult.size}">&raquo;</a></li>
	  		</c:if>
	  	</ul>
	</div><!-- /.container -->
  
  	<!-- js在最后加载 -->
    <script src="bootstrap/js/jquery-1.11.3.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
  </body>
</html>

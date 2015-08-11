<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<form action="MainServlet" method="post">
<input type="hidden" name="type" value="0"/>
<input type="submit" value="开始建立索引" />
</form>
<br/>
<br/>
<form action="MainServlet" method="post">
<input type="text" name="url"/>
<br/>
<input type="text" name="cookie"/>
<input type="hidden" name="type" value="4"/>
<input type="submit" value="抓取网页"/>
</form>
<br/>
<br/>
<form action="MainServlet" method="post">
<input type="hidden" name="type" value="5"/>
<input type="submit" value="停止抓取"/>
</form>
</body>
</html>
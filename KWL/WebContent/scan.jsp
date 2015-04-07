<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
  dojob = function(filename,pos){
	  document.getElementsByTagName("form")[0].action = "MainServlet?filename="+filename+"&pos="+pos;
	  document.getElementsByTagName("input")[0].click();
  }
</script>
<title>download page</title>
</head>
<body onload="dojob('<%=request.getParameter("filename")%>','<%=request.getParameter("pos")%>')">
<form action="/" method="post">
<input type="submit"/>
<input type="hidden" name="type" value="3"/> 
</form>
</body>
</html>
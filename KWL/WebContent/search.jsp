<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>pku knowware library</title>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/zojiepublish.css" rel="stylesheet" type="text/css" />

</head>

<body>
<div class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
      <div class="conbodybg">
    	<div class="conbody" style=" padding-top:78px;">
<script type="text/javascript">
function search(){
	if (document.getElementById("value").value=="") {
		window.confirm("请输入查询内容");
		return false;
	}
	var form = document.getElementById("form");
	form.action="MainServlet";
	form.submit();
};
</script>
        <form id="form" method="post" action="">
        <input type="hidden" name="username" value='<%=request.getParameter("username")%>'/>
        <input type="hidden" name="session" value='<%=request.getParameter("session")%>'/>
        <input type="hidden" name="type" value="1"/>
        <input type="hidden" id="type" name="field" value="filebody"></input>
    	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr height="70"><td colspan="2" align="center"><img src="images/seachlogo.png" style="margin:25px; width:350px;" /></td></tr>
            <tr>
              <td width="65%" align="right" valign="middle">
                <input type="text" id="value" name="value" class="inputtext" />              </td>
              <td width="35%" align="left">&nbsp;&nbsp;
              <a><input type="image" name="imageField" src="images/seabtn.gif" id="imageField" onclick="search()"/></a></td>
            </tr>
          </table>
          </form>
    	</div>
    </div>

</div>

</body>
</html>

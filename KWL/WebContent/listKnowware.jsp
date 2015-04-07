<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>pku knowware library</title>
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script src="script/listKnowware.js" type="text/javascript"></script>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/zojiepublish.css" rel="stylesheet" type="text/css" />
</head>

<body>
<script type="text/javascript">
$(function(){
		var search = window.location.search;
		var username = getParameterFromCookie("username");
		var session = getParameterFromCookie("session");
		var errno = getParameter(search,"errno");
		var filename = getParameter(search, "filename");
		loadKnowware(username, session, errno, filename);
	});
</script>
<div class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
</div>

  <table width="100%" border="0" cellspacing="0" cellpadding="0" id="body">
 
      <tr>
        <td height="80" colspan="2" align="center"><table width="705" height="40" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="235" align="right"><img src="images/pub001n.png" width="235" id="img1"/></td>
            <td width="235"><img src="images/pub002n.png" width="235" id="img2"/></td>
            <td width="235"><img src="images/pub003a.png" width="235" id="img3"/></td>
          </tr>
        </table></td>
    </tr>
    <tr><td colspan="2" align="center" style="padding-top:10px;"> <h2 id="large"></h2></td></tr>
    <!-- 下面是一个样例  因为结果由js生成 -->
      <tr>
        <td colspan="2" align="center" style="padding-top:10px;">
        <table width="700" border="0" cellspacing="0" cellpadding="0" bgcolor="#f3f3f3" style="padding:10px 10px 0px 10px; display:none; " class="table3">
              <tr>
                <td colspan="2" align="center" class="tabletitle">知件发布成功!</td>
              </tr>
              <tr>
                <td width="113" align="right" class="table3left">知件名称</td>
                <td width="587" align="left" class="table3right">知件名称</td>
              </tr>
              <tr>
                <td align="right" class="table3left">知件标签</td>
                <td align="left" class="table3right">知件标签</td>
              </tr>
              <tr>
                <td align="right" class="table3left">知件描述</td>
                <td align="left" class="table3right">知件描述</td>
              </tr>
              <tr>
                <td colspan="2" align="center"><hr width="90%" /></td>
              </tr>
              <tr>
                <td align="right" class="table3left">文件名称</td>
                <td align="left" class="table3right">文件名称</td>
              </tr>
              <tr>
                <td colspan="2" align="left" class="tabledown">下载&nbsp;&nbsp;&nbsp;&nbsp;查看&nbsp;&nbsp;&nbsp;&nbsp;SPARQL检索&nbsp;&nbsp;&nbsp;&nbsp;在线修改</td>
              </tr>
              <tr>
                <td colspan="2" align="right" style="padding:10px 50px 10px 0px;"><img src="images/meupbtn.png" width="165" height="37" style="margin-right:25px;" /><img src="images/menewbtn.png" width="165" height="37" /></td>
              </tr>
            </table>
            </td>
    </tr>
    
    </table>
</body>
</html>

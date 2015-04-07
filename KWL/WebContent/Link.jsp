<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>pku knowware library</title>
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/zojiecreate.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="bodybg">
    <jsp:include page="headerindex.jsp"></jsp:include></div>
<!--  
<script language="javascript">
	document.cookie = "filepath=E:/ProgramData/WorkSpaceFor3.7.1/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/KWL/owl/graph2owl/书本.owl";
</script>
-->
<script language="javascript">
	document.cookie = "filename="+getParameter(window.location.search,"word");
</script>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2" align="center" style="padding-top:10px;">
        <table width="1200" border="0" cellspacing="0" cellpadding="0" style="display:block;" id="table2">
           

          <tr class="tableheight">
            <td colspan="3" align="left" style="color:#717171; font-size:14px;">
            <div class="falsh">
              <iframe src="tool/OGEditor.jsp?mode=modify" width="1200px" height="600px"></iframe>
            </div>           </td>
          </tr>
        </table>
         
        </td>
    </tr>
    </table>
    

</body>
</html>

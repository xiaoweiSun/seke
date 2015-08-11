<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>pku knowware library</title>
<script src="script/publish.js" type="text/javascript"></script>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/webware.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
</div>
      <div class="conbodybg">
    	<div class="conbodys" >
    	  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding-top:40px;">
            <tr>
              <td align="center"><img src="images/webware001.png" width="622" height="41" /></td>
            </tr>
            <tr>
              <td align="center">&nbsp;</td>
            </tr>
            <tr>
              <td align="center"><form id="form1" name="form1" method="post" action="">
                <table width="2%" height="59" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="41%" align="right"><label>
                    <input type="text" name="textfield" id="textfield" class="inputone" />
                    </label></td>
                    <td width="13%"><label>
                    <input type="text" name="textfield2" id="textfield2" class="inputtwo" value="层数"/>
                    </label></td>
                    <td width="13%"><label>
                    <input type="text" name="textfield3" id="textfield3" class="inputtwo" value="页数"/>
                    </label></td>
                    <td width="33%"><a href="#">
              <input type="image" name="imageField" src="images/webware.gif" id="imageField" /></a></td>
                  </tr>
                </table>
                            </form>
              </td>
            </tr>
          </table>
    	</div>
    </div>

</body>
</html>

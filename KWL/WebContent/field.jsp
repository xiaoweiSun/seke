<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>pku knowware library</title>
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script src="script/field.js" type="text/javascript"></script>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/zojiepublish.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
</div>
<script language="javascript">
//通过元素ID获取文档中的元素 
function $1(v){return document.getElementById(v);}
function showtable(id){
	for(var i=1;i<4;i++){
		$1("table"+i).style.display="none";
		$1("img"+i).src="images/f00"+i+"n.png";
	}
	$1("table"+id).style.display="block";	
	$1("img"+id).src="images/f00"+id+"a.png";
}
</script>
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
      		<tr>
	        	<td height="80" colspan="2" align="center">
		        	<table width="705" height="40" border="0" cellpadding="0" cellspacing="0">
		          		<tr>
		            		<td width="235" align="right"><img src="images/f001a.png" width="235" id="img1"/></td>
		            		<td width="235"><img src="images/f002n.png" width="235" id="img2"/></td>
		            		<td width="235"><img src="images/f003n.png" width="235" id="img3"/></td>
		          		</tr>
		        	</table>
		        </td>
    		</tr>
      		<tr>
        		<td colspan="2" align="center" style="padding-top:10px;">
        			<table width="700" border="0" cellspacing="0" cellpadding="0" bgcolor="#f3f3f3" style="padding:10px 10px 0px 10px; display:none; " class="table3" id="table3">
		              <tr>
		                <td width="700" align="center" class="tabletitle">领域知识正在创建!</td>
		              </tr>
		              
		              <tr>
		                <td align="center"><a href="disfield.jsp">点击此处进行查看</a></td>
		              </tr>
        			</table>
          
			        <table width="1200" border="0" cellspacing="0" cellpadding="0" style="display:none;" id="table2">
			           <tr>
			            <td align="left"  style="padding:0px 0px 10px 50px; width:700px;color:#717171; font-size:14px;">领域知识库名称</td>
			            <td align="right" style="padding:0px 50px 10px 0px; width:700px;"><a href="javascript:void(0)"  onclick="showtable('1')"><img src="images/backbtn.png" width="95" height="37" style="margin-right:25px;" /></a><a href="javascript:void(0)"  onclick="showtable('3')"><img src="images/nextbtn.png" width="95" height="37"/></a></td>
			          </tr>
			
			          <tr class="tableheight">
			            <td colspan="3" align="left" style="color:#717171; font-size:14px;">
			            <div class="falsh">
			              <iframe src="tool/OGEditor.jsp" width="1200px" height="600px"></iframe>
			            </div>           </td>
			          </tr>
			        </table>
         
			        <table width="710" border="0" cellpadding="0" cellspacing="0" bgcolor="#f3f3f3" id="table1">
			          <tr  class="tableheight">
			            <td width="29%" valign="middle" class="decthree">领域知识库名称(<span style="color:#FF0000">必填</span>)</td>
			            <td width="71%" valign="middle"><input type="text" name="title" id="title" class="inputthree" /></td>
			          </tr>
			          <tr class="tableheight">
			            <td valign="middle" class="decthree">领域知识库标签</td>
			            <td valign="middle"><input type="text" name="tag" id="tag" class="inputthree" /></td>
			          </tr>
			          <tr class="tableheight">
			            <td valign="top" class="decthree">领域知识库描述</td>
			            <td valign="middle"><textarea name="description" rows="7" class="inputnumthree" id="description"></textarea></td>
			          </tr>
			          <tr class="tableheight">
			            <td colspan="2" align="right" style="padding:10px 50px 10px 0px;"><a href="javascript:void(0)"  onclick="submitField()"><img src="images/nextbtn.png" width="95" height="37" /></a></td>
			          </tr>
			        </table>
			     </td>
    		</tr>
    	</table>
</body>
</html>

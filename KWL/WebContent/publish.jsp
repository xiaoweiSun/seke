<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>pku knowware library</title>
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script src="script/publish.js" type="text/javascript"></script>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/zojiepublish.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
</div>
<script language="javascript">
$(select);
var vx=1;
//======================
//功能:在表单中input file控件
//参数:parentID---要插入input file控件的父元素ID
//    inputID----input file控件的ID
//======================
function createInput(parentID,inputFileID){  
  var parent=$1(parentID);//获取父元素
 
  var div=document.createElement("div");//创建一个div容器用于包含input file
  var x=vx;
  var divName=parentID+x.toString();//随机div容器的名称
  div.name=divName;
  div.id=divName;
 
  var  aElement=document.createElement("input"); //创建input
  aElement.name=inputFileID+x;
  aElement.id=inputFileID+x;
  aElement.type="file";//设置类型为file
 
  var delBtn=document.createElement("input");//再创建一个用于删除input file的Button
  delBtn.type="button";
  delBtn.value="删除";
  delBtn.onclick=function(){ removeInput(parentID,divName)};//为button设置onclick方法
 
  div.appendChild(aElement);//将input file加入div容器
  div.appendChild(delBtn);//将删除按钮加入div容器
  parent.appendChild(div);//将div容器加入父元素
  $1("vx").value=vx;
  vx+=1;
}
//============================
//功能:删除一个包含input file的div 容器
//参数:parentID---input file控件的父元素ID
//    DelDivID----个包含input file的div 容器ID
//============================
function removeInput(parentID,DelDivID){
 var parent=$1(parentID);
 parent.removeChild($1(DelDivID));
}
//通过元素ID获取文档中的元素 
function $1(v){return document.getElementById(v);}
function showtable(id){
	if (id == 2) {
		var name = $('#title').val();
		if (name == null || name == "") {
			alert("知件名称不能为空");
			return;
		} else {
			$('#knowwarespan').html(name);
		}
	}
	for(i=1;i<4;i++){
		$1("table"+i).style.display="none";
		$1("img"+i).src="images/pub00"+i+"n.png";
	}
		$1("table"+id).style.display="block";	
		$1("img"+id).src="images/pub00"+id+"a.png";
}
</script>
    <form id="publish" action="" method="post" enctype="multipart/form-data">   
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="80" colspan="2" align="center"><table width="705" height="40" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="235" align="right"><img src="images/pub001a.png" width="235" id="img1"/></td>
            <td width="235"><img src="images/pub002n.png" width="235" id="img2"/></td>
            <td width="235"><img src="images/pub003n.png" width="235" id="img3"/></td>
          </tr>
        </table></td>
    </tr>
      <tr>
        <td colspan="2" align="center" style="padding-top:10px;">
        <table width="700" border="0" cellspacing="0" cellpadding="0" bgcolor="#f3f3f3" style="padding:10px 10px 0px 10px; display:none; " class="table3" id="table3">
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
                <td align="right" class="table3left">文件类型</td>
                <td align="left" class="table3right">文件类型</td>
              </tr>
              <tr>
                <td align="right" class="table3left">文件描述</td>
                <td align="left" class="table3right">文件描述</td>
              </tr>
              <tr>
                <td colspan="2" align="left" class="tabledown">下载&nbsp;&nbsp;&nbsp;&nbsp;查看&nbsp;&nbsp;&nbsp;&nbsp;SPARQL检索&nbsp;&nbsp;&nbsp;&nbsp;在线修改</td>
              </tr>
              <tr>
                <td colspan="2" align="right" style="padding:10px 50px 10px 0px;"><img src="images/meupbtn.png" width="165" height="37" style="margin-right:25px;" /><img src="images/menewbtn.png" width="165" height="37" /></td>
              </tr>
            </table>
            
        <table width="700" border="0" cellspacing="0" cellpadding="0" bgcolor="#f3f3f3" style="padding:10px 10px 0px 10px; display:none; font-size:16px; font-family:'黑体';" id="table2">
          <tr class="tableheight">
            <td align="left" style="color:#717171; font-size:14px;">
            知件名称:<span id = "knowwarespan">Taw-ware</span></td>
          </tr>
          
          <tr  id="from" onclick="select()">
        			<td><p><b>请选择要上传的OWL文件的位置：</b></p>
        				<input type="radio" name="from" value="client" checked="checked" />从本地上传 
						<input type="radio" name="from" value="server" />从服务器上传
        			</td>
          </tr>
        <tr  id="fromserver">
        	<td>
        		<p><b>请选择一个服务器上的OWL文件：</b></p>
        		<select style="width:100px;" id="serverfile" name="serverfile"></select>
        	</td>
        </tr> 		
        <tr class="tableheight" id="fromclient">
        	<td align="left">
              <div align="left">
                     <p><b>请选择一个本地的OWL文件：</b></p><input name="owl" type="file" />
              </div>            </td>
          </tr>        		
          <tr class="tableheight">
            <td align="left" style="font-size:14px;"><p><b>请选择其他类型的辅助文件：</b></p>
            <input type="button" onclick="createInput('div_Pic','PicFiles')" name="button" id="button" value="+ 继续添加文件" /></td>
          </tr>
          <tr class="tableheight">
            <td align="left">
              <div align="left" id="div_Pic">
                     <input name="PicFiles0" type="file" id="PicFiles0"/>
              </div>            </td>
          </tr>
          <tr>
            <td align="right" style="padding:0px 50px 10px 0px; width:700px;"><a href="javascript:void(0)"  onclick="showtable('1')"><img src="images/backbtn.png" width="95" height="37" style="margin-right:25px;" /></a><a href="javascript:void(0)"  onclick="upload()"><img src="images/nextbtn.png" width="95" height="37"/></a></td>
          </tr>
        </table>
         
        <table width="710" border="0" cellpadding="0" cellspacing="0" bgcolor="#f3f3f3" id="table1">
          <tr  class="tableheight">
            <td width="29%" valign="middle" class="decthree">知件名称（<span style="color:#FF0000">必填</span>）</td>
            <td width="71%" valign="middle"><input type="text" name="knowwareName" id="title" class="inputthree" /></td>
          </tr>
          <tr class="tableheight">
            <td valign="middle" class="decthree">知件标签</td>
            <td valign="middle"><input type="text" name="tag" id="tag" class="inputthree" /></td>
          </tr>
          <tr class="tableheight">
            <td valign="top" class="decthree">知件描述</td>
            <td valign="middle"><textarea name="description" rows="7" class="inputnumthree" id="description"></textarea></td>
          </tr>
          <tr class="tableheight">
            <td colspan="2" align="right" style="padding:10px 50px 10px 0px;"><a href="javascript:void(0)"  onclick="showtable('2')"><img src="images/nextbtn.png" width="95" height="37" /></a></td>
          </tr>
        </table></td>
    </tr>
    </table>
  </form>

</body>
</html>

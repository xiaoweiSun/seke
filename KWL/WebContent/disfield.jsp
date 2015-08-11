<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PKU Knowware Library</title>
<link rel="icon" href="images/favicon.ico" mce_href="images/favicon.ico" type="image/x-icon" />
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/disfield.css" rel="stylesheet" type="text/css" />
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script src="script/disfield.js" type="text/javascript"></script>
<script language="javascript">
var funPlaceholder = function(element) {
    //检测是否需要模拟placeholder
    var placeholder = '';
    if (element && !("placeholder" in document.createElement("input")) && (placeholder = element.getAttribute("placeholder"))) {
        //当前文本控件是否有id, 没有则创�?
        var idLabel = element.id ;
        if (!idLabel) {
            idLabel = "placeholder_" + new Date().getTime();
            element.id = idLabel;
        }

        //创建label元素
        var eleLabel = document.createElement("label");
        eleLabel.htmlFor = idLabel;
        eleLabel.style.position = "absolute";
        //根据文本框实际尺寸修改这里的margin�?
        eleLabel.style.margin = "2px 0 0 3px";
        eleLabel.style.color = "graytext";
        eleLabel.style.cursor = "text";
        //插入创建的label元素节点
        element.parentNode.insertBefore(eleLabel, element);
        //事件
        element.onfocus = function() {
            eleLabel.innerHTML = "";
        };
        element.onblur = function() {
            if (this.value === "") {
                eleLabel.innerHTML = placeholder;  
            }
            //checkValue(this);
        };

        //样式初始�?
        if (element.value === "") {
            eleLabel.innerHTML = placeholder;   
        }
    }   
};
</script>
</head>
<body onload = "getTdb()">
<div class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
</div>
<div align="center" style="background:url(images/conbg.jpg) no-repeat;background-position: center top; height:434px;">
<h2>浏览领域知识库</h2>
		<div id="table3" style="display:none;">
			<table width="750" border="0" cellspacing="0" cellpadding="0" class="table3 clatd">
              <tr>
                <td width="113" align="right" class="table3left">领域库名称</td>
                <td id = "fieldName" width="587" align="left" class="table3right">领域库名称</td>
              </tr>
              <tr>
                <td align="right" class="table3left">领域库标签</td>
                <td id = "tag" align="left" class="table3right">领域库标签</td>
              </tr>
              <tr>
                <td align="right" class="table3left">领域库描述</td>
                <td id = "description" align="left" class="table3right">领域库描述</td>
              </tr>
         	</table>
         <br />
<table id ="sparql" width="1000" align="center" border="0" cellspacing="0" cellpadding="0" >
	          <tr>
	            <td class="tdboder" align="center">
		            <table width="750" border="0" align="center" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="646" align="right" valign="middle" class="tdboder borderleftitem">
					  <label>请输入您的SPARQL语句:</label>
                      <input name="sparqlIn" type="text" id="sparqlIn" size="65"   />
                      
					</td>
                    <td width="85" class="tdboder borderrightitem"><a href="javascript:void(0)"><img src="images/selsplbtn.png" onclick="query()" /></a></td>
		              </tr>
		            </table>
		        </td>
	          </tr>
	          <tr>
	            <td  class="tdboder" align="center" style="padding:6px 0px;">
	            <table width="500px"  style="border:1px solid #C1DAD7;" id = "table" class="clatd">
	            	<tr><th>来源文件</th><th>属性</th><th>属性值</th></tr>
	            </table></td>
	          </tr>
	        </table>
           
	        </div>
<table width="60%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td class="tdboder" align="center" valign="top">
   		<div id="table2">
		<h3>已经执行完成的领域库</h3>
			<table id = "tdbFinished" style="border-color:C1DAD7; border:1px solid #C1DAD7;" class="clatd">
			<tr><th>领域库名称</th><th>进度</th><th colspan="2">操作</th></tr>
			</table>
		</div>

    </td>
    <td class="tdboder"  align="center"  valign="top">		
		<div id = "table1">	
		<h3>正在执行的领域库</h3>
			<table id = "tdbProcessing"  style="border-color:C1DAD7; border:1px solid #C1DAD7;"  class="clatd">
			<tr><th>领域库名称</th><th>进度</th><th  colspan="2">操作</th></tr>
			</table>
		</div>
</td>
  </tr>
</table>
</div>
</body>
</html>

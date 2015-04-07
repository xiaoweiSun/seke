<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script src="script/sparql.js" type="text/javascript"></script>
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<link href="css/zojiepublish.css" rel="stylesheet" type="text/css" />
<link href="css/disfield.css" rel="stylesheet" type="text/css" />
<title>Sparql query</title>
<script language="javascript">
var funPlaceholder = function(element) {
    //检测是否需要模拟placeholder
    var placeholder = '';
    if (element && !("placeholder" in document.createElement("input")) && (placeholder = element.getAttribute("placeholder"))) {
        //当前文本控件是否有id, 没有则创建
        var idLabel = element.id ;
        if (!idLabel) {
            idLabel = "placeholder_" + new Date().getTime();
            element.id = idLabel;
        }

        //创建label元素
        var eleLabel = document.createElement("label");
        eleLabel.htmlFor = idLabel;
        eleLabel.style.position = "absolute";
        //根据文本框实际尺寸修改这里的margin值
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

        //样式初始化
        if (element.value === "") {
            eleLabel.innerHTML = placeholder;   
        }
    }   
};
</script>
</head>
<body>
	<div class="bodybg">
  		<jsp:include page="headerindex.jsp"></jsp:include>
	</div>
	<div class="body">
	<div id="filename" align="center"></div>
	<table id ="sparql" width="100%" align="center" border="0" cellspacing="0" cellpadding="0" >
	          <tr>
	            <td class="tdboder" align="center" style="padding-top:25px;">
		            <table width="800" border="0" align="center" cellspacing="0" cellpadding="0">
		              <tr>
		                <td align="center"><div >
                      <input name="sparqlIn"  id="sparqlIn" type="text" id="sparqlIn" size="80" placeholder="请输入您的SPARQL语句"/><label>*查询类型：
			  <input type="radio" class="radio1" name="querytype" value="select" checked="checked"/>SELECT
<!--			<input type="radio" class="radio1" name="querytype" value="construct"/>CONSTRUCT
			  <input type="radio" class="radio1" name="querytype" value="describe"/>DESCRIBE
-->			  <input type="radio" class="radio1" name="querytype" value="ask"/>ASK
			  </label>
					  <button id="query" onClick="submit()" >查询</button>
					</div>
                    <script>funPlaceholder(document.getElementById("sparqlIn"));</script>
                    </td>
		              </tr>
		            </table>
		        </td>
	          </tr>
	          <tr>
	            <td  class="tdboder" align="center">
	        <!--      <table width="500px"  style="border:1px solid #C1DAD7;" id = "table">
	            	<tr><th>来源文件</th><th>属性</th><th>属性值</th></tr>
	            </table>
	        -->
	        <textarea id = "result" rows="20" cols="180"></textarea>    
	            </td>
	          </tr>
	        </table>

    </div>
    
    
</body>
</html>
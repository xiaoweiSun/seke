<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<title>北京大学本体搜索</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/display.css" rel="stylesheet" type="text/css" />
<link rel="StyleSheet" href="dtree/dtree.css" type="text/css" />
<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script type="text/javascript">
$(checkresult);
	function checkresult() {
		var result = getParameter(window.location.search,"result");
		if (result=="fail") {
			window.confirm("owl文件解析失败,不是一个合法的owl文件 或者文件过大！");
		}
	}
	function refresh(focus)
	{
		if(type == 1)//如果此时是单独显示
		{
			var framesType1 = document.getElementById("f");
			framesType1.contentWindow.mainFrameReview(focus);
		}
		else if(type==2)//如果是全部显示
		{
			var framesType2 = document.getElementById("f");
			framesType2.contentWindow.mainFrameClick(focus);
		}
		else//如果是增量显示
		{
			var framesType2 = document.getElementById("f");
			framesType2.contentWindow.mainFrameAdd(focus);
		}

		var frames2 = document.getElementById("g");
		frames2.src="rightdown.jsp?focus="+focus;

		//window.parent.ifr.location.href= "flex_work/test.jsp?focus="+focus; 
	}
	function change(ltype)
	{

		type = ltype;
		if(type == 1)//如果是要转换成全部显示
		{
			var frames = document.getElementById("f");
			frames.src="content4/test1.jsp";
			type = 1;
			
		}
		else if(type==2)//如果要转换成单独显示
		{
			var frames = document.getElementById("f");
			frames.src="content4/test2.jsp";
			type = 2;
			
		}
		else//如果是增量显示
		{
			var frames = document.getElementById("f");
			frames.src="content4/test3.jsp";
			type = 3;
			
		}
	}

	function changeWin()
	{
		var win = document.getElementById("minmax");
		var frame = document.getElementById("f");
		
		if(Win == 1)//如果当前要求最大化
		{
			//frame.style.position="absolute";
			//frame.style.left="0px";
			///frame.style.right="0px";
			//frame.width=1000;
			frame.height=650;
			win.value="最小化";
		}
		else
		{
			//frame.style.position="relative";
			//frame.width = 600;
			frame.height=500;
			win.value="最大化";
		}
			
		Win = 1 - Win;
	}
		

	function listNode(num)
	{
		alert(1);
		d.openTo(num,true);
		alert(3);
	}
	function search()
	{
		var input = document.getElementById("input").value;
		var temp = arr.split("|");//divide arr by element "|"
		var flag= 0;
		

		for(var i=0;i<temp.length;i++)
		{
			var now = temp[i].split("/");
			if(now[0]==input)
			{
				refresh(input);
				flag = 1;
				break;
			}
		}
		if(flag==0)
		{
			alert("抱歉，没有找到"+input+"!");
		}
	}

		
</script>


</head>
<body>

	<%
	request.setCharacterEncoding("UTF-8");
    %>
	
<div id="container" style="width:1200px;">
	  
   
 <div id="content" style="background-color:#ffffff;width:1200px;margin-right:5px;"> 
    <div id="dtree_left_column" style="background-color:#ffffff;">
		<div class="dtree" style="background-color:#ffffff;width:100%;height:100%;float:left;">
			<script type="text/javascript" src="dtree/dtree.js"></script>
				<p><a href="javascript: d.openAll();">open all</a> | <a href="javascript: d.closeAll();">close all</a></p>

			<script type="text/javascript">
			<!--
			type = 1;//初始默认类型为1 即显示test1.jsp
			Win = 1;//初始窗口为普通
			arr = "<%=session.getAttribute("Server1ResEle")%>";
			var temp = arr.split("|");//divide arr by element "|"

		
			d = new dTree('d');

			for(var i=0;i<temp.length;i++)
			{
				var now = temp[i].split("/");
				d.add(now[2],now[1],"<a  href=\"javascript:;\" onclick=\"refresh('"+now[0]+"')\">"+now[0]+"</a>");
			}

		

			document.write(d);

			//-->
			</script>

		</div>

	</div>
	<div id="dtree_right_column" style="background-color:#ffffff;padding-top: 5px;" >
<% 
	session.setAttribute("ele",session.getAttribute("Server1ResEle"));
	session.setAttribute("elen",session.getAttribute("Server1ResElen"));
	session.setAttribute("edg",session.getAttribute("Server1ResEdg"));
	session.setAttribute("edgn",session.getAttribute("Server1ResEdgn"));
%>
	<!-- 已经隐藏本部分 -->
		<div style="display:none;" id="form_row">
		  <form id="form" method="post" action="queryDo1.jsp">
		  <table ><tr>
		  <td align="center"><input id="input" class="inputfield" type="text" value="概念名称" name="kwname" onclick="this.value=''"/></td>
		  </tr></table>
		  </form>
		  <table><tr>
		  <td align="center"><input name="submit" type="submit" class="button" value="查找" onclick="search()" /></td>
		  </tr></table>
		</div>
	<!-- 已经隐藏这部分 -->
	 	<div style="display:none;">
		  <form action="">
		  				<label>选择显示方式</label>
		  				<label><input id="radio1" class="radio1" checked="checked" type="radio" name="only" value="1" onclick="change(1)">单个概念</input></label>
		  				<label><input id="radio2" class="radio1" type="radio" name="only" value="2" onclick="change(2)">全部概念</input></label>
		  				<label><input id="radio3" class="radio1" type="radio" name="only" value="3" onclick="change(3)">增加概念</input>
		  				</label>
		  </form>
	  	</div>
	
	
	<iframe name="ifr" id="f" width=955 height=600 frameborder=1 scrolling=auto src="content4/test1.jsp"></iframe>

<!-- 根据要求 隐藏本部分 -->
  			<div style="display:none;">
	  			<br></br>
				<input id="minmax" class="btn2" type="button" value="最大化" onclick="changeWin()"></input>
				<input class="btn2" type="button" value="运动" name="btnStart" onclick="ifr.start()"/>
	    		<input class="btn2" type="button" value="静止" name="btnStop" onclick="ifr.end()"/>
	    		<input class="btn2" type="button" value="展示" name="btnInit" onclick="ifr.initGraphShow()"/>
	    		<input class="btn2" type="button" value="清空" name="btn4" onclick="ifr.Clear()"/>
			</div>

	
	</div>
	<!-- 根据要求  本部分隐藏 -->
  		<div id="dtree_right_column" style="display:none;width:250px;padding-top: 5px;padding-right:5px;">
  			<iframe name="if" id="g" width=250 height=500 frameborder=0 scrolling=auto src="rightdown.jsp"></iframe>
		</div>

</div>
  

</div>

</body>
</html>
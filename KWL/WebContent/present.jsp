<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" 
    import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/present.css" rel="stylesheet" type="text/css" />
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<script src="script/base.js" type="text/javascript"></script>
<script type="text/javascript">
	var username= getParameterFromCookie("username");
	var session= getParameterFromCookie("session");	
	function download(filename,pos){
		var ifr=document.createElement("iframe");
		ifr.src = "download.jsp?filename="+filename+"&pos="+pos;
		document.body.appendChild(ifr);
		ifr.hidden = true;
	};
	function send(filename,pos){
		var sfr=document.getElementById("dis");
		sfr.src = "generateData.jsp?filename="+filename+"&pos="+pos;
		sfr.width="1200px";
		sfr.height="750px";
		sfr.style.display="";
		document.getElementById("info").style.display="none";
		document.getElementById("file").style.display="none";
	};
	function search(){
		if (document.getElementById("value").value=="") {
			window.confirm("请输入查询内容");
			return false;
		}
		var form = document.getElementById("form");
		form.submit();
	};
	function sparql(filename,pos){
		window.location.href = "sparql.jsp?"+"session="+session+"&username="+username+"&filename="+filename+"&dirtype="+pos;
	};
</script>
<title>Response </title>
</head>
<body>
<div id="include" class="bodybg">
  <jsp:include page="headerindex.jsp"></jsp:include>
</div>

	<form id="form" style="display: block;"action="MainServlet" method="post">
	<input type="hidden" name="type" value="1"/>
	<input type="hidden" name="username" value='<%=request.getParameter("username")%>'/>
    <input type="hidden" name="session" value='<%=request.getParameter("session")%>'/>
		<div style="padding:10px 0 8px 190px;valign:center; background:#f3f3f3;">
		<span style="margin:0 0 10px 0;valign:center;width:600px;height:40px;font-size:20px;"><input type="hidden" name="field" value="filebody"/></span>
			<span style="float:left;vertical-align:baseline;valign:center;width:338px;height:40px;"><input style=" background:url(images/mseaedit.png)  no-repeat; border:0px; width:318px; height:35px; font-size:13pt; padding-left:5px; padding-right:5px; " type ="text" id="value" name="value" /></span>
			<a href="javascript:search()" ><img src="images/mseabtn.png" border="0" /></a>
		</div>
	</form>

<div class="page" style="clear:both;">
<%
	String res = (String)request.getAttribute("hits");
	String key = (String)request.getAttribute("key");
	int num = Integer.parseInt(res);
	ArrayList<String[]> al = new ArrayList<String[]>();
	for(int i=0;i<num;i++){
		String temp = (String)request.getAttribute("hit"+i);
		String metadata[] = new String[4];
		//System.out.println(temp);
		metadata[0] = temp.substring(0,temp.indexOf("|"));
		metadata[1] = temp.substring(temp.indexOf("|")+1);
		String t = (String)request.getAttribute("con"+i);
		if(t!=null){
		t = t.replace("<", "&#60;");
		t = t.replace(">", "&#62;");
		t =t.replace(key, "<span style=\"color:#ff6600;	\">"+key+"</span>");
		//System.out.println(key);
		} else {
			t = "该知件内容中不包含检索所用的关键字";
		}
		metadata[2] = t;
		String pos = (String)request.getAttribute("pos"+i);
		metadata[3] = pos;
		al.add(metadata);
	}

%>
	
	<div id="info">
		<table class="mainhdr" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tbody><tr><td width="100%"></td>
			 <td style="padding:3px 1em; font-weight:normal; white-space:nowrap;">
			 reactiontime&lt;=<%=request.getAttribute("time")%>ms&nbsp;&nbsp;
			 Results 1 - 10 of <%=request.getAttribute("hitsnum")%>
			 </td>
			</tr>
			</tbody>
		</table>
	</div>
	<br>

	<iframe id="dis" style="display:none;"></iframe>
	<div id="file" class="file">
	<% 
		for(int i=0;i<num;i++){
			%>
			<table cellpadding="0"><tbody>
			<tr><td style="width:100px;padding-right:9px;">
					<div style="width:100px;height:70px;"><img src="imgs/<%=al.get(i)[3]%>.png" width="100"></div></td>
				<td><a class="key" target="_blank" href="scan.jsp?filename=<%=al.get(i)[1] %>&pos=<%=al.get(i)[3]%>"><span style="font-size:23px;color:#ff6600;"><%=key%>&nbsp;-&nbsp;</span><span style="font-size:23px;color:blue;"><%=al.get(i)[0] %></span></a><br>
			<font  style="color:#000000;"><%=al.get(i)[2]%></font><br>
			
			<font style="font-size:16px;color:#008000;"><a href="javascript:download('<%=al.get(i)[1] %>','<%=al.get(i)[3]%>')"><span style="color:#008000;" >下载</span></a>&nbsp;&nbsp;	
			<a target="_blank" href="scan.jsp?filename=<%=al.get(i)[1] %>&pos=<%=al.get(i)[3]%>"><span style="color:#008000;" >查看</span></a>&nbsp;&nbsp;
			<a href="javascript:sparql('<%=al.get(i)[1] %>','<%=al.get(i)[3]%>')"><span style="color:#008000;" >SPARQL</span></a>&nbsp;&nbsp;
			<a href="#" onClick="send('<%=al.get(i)[1] %>','<%=al.get(i)[3]%>')"><span style="color:#008000;" >图形化展示</span></a></font></td></tr>
			</tbody></table>
			<br>
			<%
		}
	 
	
	%>
	</div>
	
	
</div>
</body>
</html>
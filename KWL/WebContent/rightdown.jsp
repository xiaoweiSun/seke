<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/table.css" type="text/css"/>
<title>Insert title here</title>
</head>
<body>
<div id="right">
<script type="text/javascript"">


	var strele = "<%=session.getAttribute("ele")%>";
	var stredg = "<%=session.getAttribute("edg")%>";
	var numele = <%=session.getAttribute("elen")%>;
	var numedg = <%=session.getAttribute("edgn")%>;
	var focus = "<%=request.getParameter("focus")%>";
	elestr = strele.split("|");
	edgstr = stredg.split("|");

	document.write("<table id=\"mytable\" cellspacing=\"0\" summary=\"The technical specifications of the Apple PowerMac G5 series\">");
	document.write("<caption>相关属性</caption> ");
	document.write("<tr> "
    			//+"<th scope=\"col\" >Relation</th>" 
    			//+"<th scope=\"col\" >Domain</th>" 
    			+"<th scope=\"col\" >property</th> "
    			+"<th scope=\"col\" >Range&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>" 
  				+"</tr> ");
	var tag = 0;
	for(var i=0;i<elestr.length;i++)
	{
		var temp = elestr[i].split("/");
		if( temp[0] == focus )
		{
			var len = temp.length;
			if(	len > 3)//如果有其他属性
			{
				tag = 1;
				for(var k=3;k<len;k++)
				{
					document.write("<tr>");
					var pro = temp[k].split("~");
					document.write("<td>"+pro[0]+"</td><td>"+pro[1]+"</td></tr>");
				}
				
			}
			break;
		}
	}
	if(tag == 0)		
	{
		document.write("<tr><th scope=\"row\" class=\"spec\">noproperty</th><td></td></tr>");
	}

	document.write("</table>");


		//////////////////
	document.write("<table id=\"mytable\" cellspacing=\"0\" summary=\"The technical specifications of the Apple PowerMac G5 series\">");
	document.write("<caption>some relation</caption> ");
	document.write("<tr> "
    			//+"<th scope=\"col\" >Relation</th>" 
    			//+"<th scope=\"col\" >Object</th>" 
    			+"<th scope=\"col\" >relation</th> "
    			+"<th scope=\"col\" >Subject&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>" 
  				+"</tr> ");


		var tot = 0;
		for(var i=0;i<edgstr.length;i++)
		{
			
			var temp = edgstr[i].split("/");
			
			if( temp[1] == focus )
			{
				tot++;
				//document.write("<tr><th scope=\"row\"  class=\"spec\">relation"+tot+"</th>");
				document.write("<tr>");
				document.write("<td>"+temp[0]+"</td><td>"+temp[2]+"</td></tr>");
			}
			if( temp[2] == focus )
			{
				tot++;
				//document.write("<tr><th scope=\"row\"  class=\"spec\">relation"+tot+"</th>");
				document.write("<tr>");
				document.write("<td class=\"alt\">"+temp[1]+"</td><td class=\"alt\">"+temp[0]+"</td></tr>");
			}
			
		}
		if(tot == 0)
		{
			document.write("<tr><th scope=\"row\" class=\"spec\">norelation</th><td></td></tr>");
		}

		document.write("</table>");

</script>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">	
    <!-- 
    Smart developers always View Source. 
    
    This application was built using Adobe Flex, an open source framework
    for building rich Internet applications that get delivered via the
    Flash Player or to desktops via Adobe AIR. 
    
    Learn more about Flex at http://flex.org 
    // -->
    <head>
        <title></title>
        <meta name="google" value="notranslate">         
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!-- Include CSS to eliminate any default margins/padding and set the height of the html element and 
		     the body element to 100%, because Firefox, or any Gecko based browser, interprets percentage as 
			 the percentage of the height of its parent container, which has to be set explicitly.  Fix for
			 Firefox 3.6 focus border issues.  Initially, don't display flashContent div so it won't show 
			 if JavaScript disabled.
		-->
        <style type="text/css" media="screen"> 
			html, body	{ height:600; }
			body { margin:0; padding:0; overflow:auto; text-align:center; 
			       background-color: #ffffff; }   
			object:focus { outline:none; }
			#flashContent { display:none; }
        </style>
		
		<!-- Enable Browser History by replacing useBrowserHistory tokens with two hyphens -->
        <!-- BEGIN Browser History required section -->
        <link rel="stylesheet" type="text/css" href="history/history.css" />
        <script type="text/javascript" src="history/history.js"></script>
        <!-- END Browser History required section -->  
		    
        <script type="text/javascript" src="swfobject.js"></script>
        <script type="text/javascript"><!--
            <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
            var swfVersionStr = "10.0.0";
            <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
            var xiSwfUrlStr = "playerProductInstall.swf";
            var flashvars = {};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#ffffff";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "GraphShow";
            attributes.name = "GraphShow";
            attributes.align = "middle";
            swfobject.embedSWF(
                "GraphShow.swf", "flashContent", 
                "955", "600", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
			<!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->
			swfobject.createCSS("#flashContent", "display:block;text-align:left;");
			

        </script>
    </head>
    <body>
        <div id="flashContent">
        	<p>
	        	To view this page ensure that Adobe Flash Player version 
				10.0.0 or greater is installed. 
			</p>
			<script type="text/javascript"> 
				var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://"); 
				document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
								+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
			</script> 
        </div>
	   	
       	<noscript>
            
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="955" height="600" id="GraphShow">
                <param name="movie" value="GraphShow.swf" />
                <param name="quality" value="high" />
                <param name="bgcolor" value="#ffffff" />
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="allowFullScreen" value="true" />
                <!--[if !IE]>-->
                <object type="application/x-shockwave-flash" data="GraphShow.swf" width="955" height="600">
                    <param name="quality" value="high" />
                    <param name="bgcolor" value="#ffffff" />
                    <param name="allowScriptAccess" value="always" />
                    <param name="allowFullScreen" value="true" />
                <!--<![endif]-->
                <!--[if gte IE 6]>-->
                	<p> 
                		Either scripts and active content are not permitted to run or Adobe Flash Player version
                		10.0.0 or greater is not installed.
                	</p>
                <!--<![endif]-->
                    <a href="http://www.adobe.com/go/getflashplayer">
                        <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash Player" />
                    </a>
                <!--[if !IE]>-->
                </object>
                <!--<![endif]-->
            </object>
	    </noscript>	
	    <div style="margin:0px 0px 0px 0px;position:absolute;left:0px;top:0px;height:600px;width:955px;z-index:-1"></div>	
	    <script type="text/javascript">
	    elestr 	= "<%=session.getAttribute("Server1ResEle")%>";
		elen	= 	<%=session.getAttribute("Server1ResElen")%>;
		edgstr	= "<%=session.getAttribute("Server1ResEdg")%>";
		edgn	=	<%=session.getAttribute("Server1ResEdgn")%>;
		
		    function getFlexApp(appName)
			{
			  if (navigator.appName.indexOf ("Microsoft") !=-1)
			  {
			    return window[appName];
			  } 
			  else 
			  {
			    return document[appName];
			  }
			}
	    
	    	function initGraphShow()
			{
				var GraphShow = getFlexApp('GraphShow');
				GraphShow.BeginUpdate();
				
				var strele = elestr.split("|");
				for(var i=0;i<elen;i++)
				{
					var t = strele[i].split("/");
					GraphShow.setUserData("name",t[0]);
					GraphShow.AddElement();
				}
				var stredg = edgstr.split("|");
				for(var j=0;j<edgn;j++)
				{
					var t = stredg[j].split("/");
					GraphShow.setCurFrom("name",t[1]);
					GraphShow.setCurTo("name",t[2]);
					GraphShow.setUserData("name",t[0]);
					GraphShow.AddEdge();
				}
				
				
				GraphShow.RegisterJSEventHandler("doubleClick","MouseEventHandler");
				GraphShow.RegisterJSEventHandler("click","clickhandler");
				
				GraphShow.EndUpdate();
			}
			
			function clickhandler()
			{
<!--				alert("clickhandler!");-->
			}
			
			var userdata={}; 
			function setUserData(p,v)
			{
				userdata[p]=v;
			}
			
			var altKey,buttonDown,ctrlKey,delta,localX,localY,shiftKey,stageX,stageY;
			function MouseEventGetter(_altKey,_buttonDown,_ctrlKey,_delta,_localX
					,_localY,_shiftKey,_stageX,_stageY)
			{
				altKey = _altKey;
				buttonDown = _buttonDown;
				ctrlKey = _ctrlKey;
				delta = _delta;
				localX = _localX;
				localY = _localY;
				shiftKey = _shiftKey;
				stageX = _stageX;
				stageY = _stageY;
			}
			
			function btn1()
			{
			var GraphShow = getFlexApp('GraphShow');
				GraphShow.setUserData("name","ele1");
				GraphShow.SetCenterElement();
				GraphShow.startTimer();
			}
			
			function btn2()
			{
			var GraphShow = getFlexApp('GraphShow');
				GraphShow.setUserData("name","ele2");
				GraphShow.SetCenterElement();
				GraphShow.startTimer();
			}
			
			function btn3()
			{
			var GraphShow = getFlexApp('GraphShow');
				GraphShow.setUserData("name","ele3");
				GraphShow.SetCenterElement();
				GraphShow.startTimer();
			}
			
			function MouseEventHandler()
			{
			var GraphShow = getFlexApp('GraphShow');
				if (userdata["mouseEventType"] == "doubleClick")
				{
					GraphShow.setUserData("name",userdata["name"]);
					GraphShow.setUserData("id",userdata["id"]);
					GraphShow.SetCenterElement();
					setTimeout("start()",1000);
					
					var frames = window.parent.document.getElementById("g");
					frames.src="./rightdown.jsp?focus="+userdata["name"];
					userdata = {};
				}
				else
				{
				}
			}
			function mainFrameClick(focus)
			{
				var GraphShow = getFlexApp('GraphShow');
				GraphShow.setUserData("name",focus);
				GraphShow.SetCenterElement();
				GraphShow.setShowDepth(1);
				setTimeout("start()",1000);
				
			}
			function start()
			{
				var GraphShow = getFlexApp('GraphShow');
				GraphShow.startTimer();
			}
			function end()
			{
				var GraphShow = getFlexApp('GraphShow');
				GraphShow.endTimer();
			}
	    </script>
   </body>
</html>

<script src="script/jquery-2.0.3.js" type="text/javascript"></script>
<script src="script/jquery.KinSlideshow-1.1.js" type="text/javascript"></script>
<script src="script/base.js" type="text/javascript"></script>
<script src="script/head.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript"> 
function checklogin() {
	var username = getParameterFromCookie("username");
	console.log("checklogin");
	console.log(username);
	if (username != null && username != "null") {
		$('#user').html("User:"+username);
		$('#logindiv').hide();
		$('#loginoutdiv').show();
		console.log("login username:"+username);
	} else {
		$('#loginoutdiv').hide();
		$('#logindiv').show();
		console.log("loginout");
	}
};
$(function(){
 $("ul#menu>li:has(ul)").hover( 
  function(){ 
   $(this).children('a').addClass('red').end().find('ul').fadeIn(400);
  },
  function(){
   $(this).children('a').removeClass('red').end().find('ul').fadeOut(400);
  }
 );
});
$(checklogin);
</script>
	<div class="head_top">
        <div style="height:67px;">
        	<div class="logo"><img src="images/logo.png" height="60" /></div>
        <div  id="loginoutdiv" class="top_loginout" ><div id="user" style="line-height:20px;">用户名:</div><a href="javascript:void(0)" onclick="javascript:openlogout()"><img src="images/loginout.png" /></a></div>

        <div id="logindiv" class="top_login" ><a href="javascript:void(0)" onclick="javascript:openlogin()"><img src="images/login.jpg" /></a></div>


        </div>
        
	     <div id="wrap" style="clear:both;">
        	<ul id="menu">
            	<li class="menu1" ><a href="javascript:void(0)"  onClick="gotomain()" <%if(request.getRequestURI().indexOf("index.jsp")>0){%>class="dh1"<%}%>><img src="images/menu.gif" border="0"/></a></li>
                <li class="menu2"><a href="javascript:void(0)" onClick="gotoseach()" <%if(request.getRequestURI().indexOf("search.jsp")>0){%>class="dh2"<%}%>><img src="images/menu.gif" border="0" /></a></li>
                <li class="menu3"><a href="javascript:void(0)" onClick="gotopublish()" <%if(request.getRequestURI().indexOf("publish.jsp")>0){%>class="dh3"<%}%>><img src="images/menu.gif" border="0" /></a></li>
                <li class="menu4"><a href="javascript:void(0)" onClick="gotocreate()" <%if(request.getRequestURI().indexOf("create.jsp")>0){%>class="dh4"<%}%>><img src="images/menu.gif" border="0" /></a></li>
                <li class="menu5"><a href="javascript:void(0)" onClick="gotofield()" <%if(request.getRequestURI().indexOf("field.jsp")>0){%>class="dh5"<%}%>><img src="images/menu.gif" border="0" /></a>
                  <ul>
             		<li class="menu51"><a href="javascript:void(0)" onClick="gotofield()"><img src="images/menu.gif" border="0" /></a></li>
                	<li class="menu52"><a href="javascript:void(0)" onClick="gotodisfield()"><img src="images/menu.gif" border="0" /></a></li>
                  </ul>


                </li>
                <li class="menu6"><a href="javascript:void(0)" onClick="gotowebware()" <%if(request.getRequestURI().indexOf("webware.jsp")>0 || request.getRequestURI().indexOf("webwareok.jsp")>0 ||request.getRequestURI().indexOf("webwarequeue.jsp")>0 ){%>class="dh6"<%}%>><img src="images/menu.gif" border="0" /></a>
                  <ul>
             		<li class="menu61"><a href="javascript:void(0)"  onclick="gotowebwareok()"><img src="images/menu.gif" border="0" /></a></li>
                	<li class="menu62"><a href="javascript:void(0)"  onclick="gotowebwarequeue()"><img src="images/menu.gif" border="0" /></a></li>
                  </ul>
                </li>
                <li class="menu7"><a href="javascript:void(0)" onClick="gotoapppro()" <%if(request.getRequestURI().indexOf("apppro.jsp")>0){%>class="dh7"<%}%>><img src="images/menu.gif" border="0" /></a></li>
                <li class="menu8"><a href="javascript:void(0)" onClick="gotoabout()" <%if(request.getRequestURI().indexOf("about.jsp")>0){%>class="dh8"<%}%>><img src="images/menu.gif" border="0" /></a></li>
            </ul>
        </div>

    </div>
<jsp:include page="login.jsp"></jsp:include>
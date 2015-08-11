<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/zojiemain.css" rel="stylesheet" type="text/css" />
<title>pku knowware library</title>
<script src="script/jquery-1.4a2.min.js" type="text/javascript"></script>

<script src="script/jquery.KinSlideshow-1.1.js" type="text/javascript"></script>
<script src="script/index.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#KinSlideshow").KinSlideshow({
			moveStyle:"down",
			intervalTime:8,
			mouseEvent:"mouseover",
			isHasTitleBar:false,
			titleFont:{TitleFont_size:14,TitleFont_color:"#FF0000"},
			isHasTitleFont:false,
			isHasBtn:true, //是否显示按钮

			btn:{btn_bgColor:"#e9e9e9",btn_bgHoverColor:"#8e8d8b",
				  btn_fontColor:"#000000",btn_fontHoverColor:"#000000",btn_fontFamily:"Verdana",
				  btn_borderColor:"#999999",btn_borderHoverColor:"#8e8d8b",
				  btn_borderWidth:1,btn_bgAlpha:0.7}
	});
});
</script> 
</head>

<body>
<div class="bodybg">
    <jsp:include page="headerindex.jsp"></jsp:include>
    <div class="banner">
    	<div class="flashs">
            <div id="KinSlideshow" style="visibility:hidden;">
                <a href="#" target="_blank"><img src="images/001.jpg" alt="这是标题一" width="1200" height="300" /></a>
                <a href="#" target="_blank"><img src="images/002.jpg" alt="这是标题二" width="1200" height="300" /></a>
                <a href="#" target="_blank"><img src="images/003.jpg" alt="这是标题三" width="1200" height="300" /></a>
            </div>
        </div>
    </div>
    <div class="conbodybg">
    	<div class="conbody" style="height:258px;">
        	<ul>
            	<li><img src="images/index001.png"  border="0"/></li>
            	<li><img src="images/index002.png"  border="0"/></li>
            	<li><img src="images/index003.png"  border="0"/></li>                                
            </ul>
    	</div>
    </div>
</div>
</body>
</html>

<link href="css/login_register.css" rel="stylesheet" type="text/css" />
<script src="script/base.js" type="text/javascript"></script>
<script src="script/login_register.js" type="text/javascript"></script>
   <!-- 隐藏在最后面的不能见人的页面 -->
    <div id="login" style="display:none;">
    	<div style="position: absolute; z-index: 10001; background-color: rgb(51, 51, 51); opacity: 0.4; top: 0px; left: 0px; width: 1350px; height: 750px; "></div>
    	<div style="position: absolute; z-index: 10005; opacity: 1; top: 200px; left: 500px; width: 415px; height: 200px; border:0px;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:url(images/loginbg.png) no-repeat;" height="200">
              <tr style="height:39px;">
                <td background="images/userlogintitlebg.png"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="99%" align="center" class="inputtitle">登录</td>
                    <td width="1%" style="padding:8px;"><a href="javascript:void(0)" onClick="closelogin()"><img src="images/close.png" width="14" height="13"></a></td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td >
                  <label>                  </label>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="16%" align="right">用户名: </td>
                      <td width="84%" align="left"><input class="inputedit" type="text" name="username" id="login_username" /></td>
                    </tr>
                    <tr >
                      <td align="right">密&nbsp;&nbsp;码:</td>
                      <td align="left"><input type="password" class="inputedit" name="login_password" id="login_password" /></td>
                    </tr>
                  </table>
                </td>
              </tr>
              
              <tr>
                <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="50%" align="center"><a href="javascript:void(0)" onClick="openregister()">立即注册</a></td>
                    <td align="center" valign="middle"><a href="javascript:void(0)" onClick="login()"><img src="images/login.png" width="95" height="37"></a></td>
                  </tr>
                </table></td>
              </tr>
            </table>
      </div>
    </div>
    <!-- 隐藏在最后面的不能见人的页面 -->
    <div id="register" style="display:none;">
    	<div style="position: absolute; z-index: 10001; background-color: rgb(51, 51, 51); opacity: 0.4; top: 0px; left: 0px; width: 1350px; height: 750px; "></div>
    	<div style="position: absolute; z-index: 10005; opacity: 1; top: 200px; left: 500px; width: 413px; height: 250px; ">
    		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:url(images/loginbg.png) no-repeat;">
  <tr height="39">
    <td  background="images/userlogintitlebg.png"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="99%" align="center"  class="inputtitle">注册</td>
                    <td width="1%" style="padding:8px;"><a href="javascript:void(0)" onClick="closeregister()"><img src="images/close.png" width="14" height="13"></a></td>
                  </tr>
                </table></td>
  </tr>
  <tr>
    <td>
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right">用户名:</td>
    <td align="left"><input  class="inputedit" id="register_username" type="text" name="username"></td>
  </tr>
  <tr>
    <td align="right">密码:</td>
    <td align="left"><input class="inputedit" id="register_password" type="password" name="password"></td>
  </tr>
  <tr>
    <td align="right">密码确认:</td>
    <td align="left"><input class="inputedit" id="register_retype_password" type="password" name="password"></input></td>
  </tr>
</table>
</td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="50%" align="center"><a href="javascript:void(0)" onClick="openlogin()">返回登录</a></td>
                    <td align="center" valign="middle"><a href="javascript:void(0)" onClick="register()"><img src="images/regin.png" width="95" height="37"></a></td>
                  </tr>
                </table></td>
  </tr>
</table>

    </div>
    </div>

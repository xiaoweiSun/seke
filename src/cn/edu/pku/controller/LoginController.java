package cn.edu.pku.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.pku.domain.User;
import cn.edu.pku.service.LoginService;

@Controller
public class LoginController {
	
	LoginService loginService;
	
	public LoginService getLoginService() {
		return loginService;
	}

	@Resource
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}


	@RequestMapping("login")
	public String login(HttpServletRequest req,HttpServletResponse res) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User user = loginService.login(username, password);
		if(user != null) {
			req.setAttribute("user", user);
			return "index.jsp";
		}
		return "";
	}
}

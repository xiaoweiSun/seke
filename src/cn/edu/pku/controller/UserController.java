package cn.edu.pku.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.pku.domain.User;
import cn.edu.pku.service.UserService;

@Controller
public class UserController {
	
	UserService userService;
	
	public UserService getLoginService() {
		return userService;
	}

	@Resource
	public void setLoginService(UserService userService) {
		this.userService = userService;
	}


	@RequestMapping("login")
	public String login(HttpServletRequest req,HttpServletResponse res) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User user = userService.login(username, password);
		HttpSession session = req.getSession();
		if(user != null) {
			session.setAttribute("user", user);
			return "index.jsp";
		}
		return "error.jsp";
	}
	
	@RequestMapping(value="regist",method=RequestMethod.GET)
	public String regist(){
		return "WEB-INF/jsp/regist.jsp";
	}
	
	@RequestMapping(value="regist",method=RequestMethod.POST)
	public String regist(HttpServletRequest req,HttpServletResponse res) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User user = userService.regist(username, password);
		HttpSession session = req.getSession();
		if(user != null) {
			session.setAttribute("user", user);
			return "index.jsp";
		}
		return "error.jsp";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest req,HttpServletResponse res) {
		HttpSession session = req.getSession();
		session.removeAttribute("user");
		return "index.jsp";
	}
}

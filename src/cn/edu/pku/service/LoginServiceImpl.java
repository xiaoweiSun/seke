package cn.edu.pku.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.pku.dao.UserDAO;
import cn.edu.pku.domain.User;

@Service
public class LoginServiceImpl implements LoginService{

	private UserDAO userDao;
	
	
	public UserDAO getUserDao() {
		return userDao;
	}

	@Resource
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}


	@Override
	public User login(String username, String password) {
		User user = userDao.loadByUsername(username);
		if(user != null && password.equals(user.getPassword()))
			return user;
		return null;
	}

}

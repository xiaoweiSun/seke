package cn.edu.pku.service;

import cn.edu.pku.domain.User;

public interface LoginService {

	public User login(String username,String password);
}

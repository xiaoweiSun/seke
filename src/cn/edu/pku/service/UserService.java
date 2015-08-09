package cn.edu.pku.service;

import cn.edu.pku.domain.User;

public interface UserService {

	public User login(String username,String password);
	public User regist(String username,String password);
}

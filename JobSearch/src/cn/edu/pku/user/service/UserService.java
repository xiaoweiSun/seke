package cn.edu.pku.user.service;

import cn.edu.pku.user.domain.User;

public interface UserService {

	public User login(String username,String password);
	public User regist(String username,String password);
}

package cn.edu.pku.dao;

import cn.edu.pku.domain.User;

public interface UserDAO {

	public void add(User user);
	public void update(User user);
	public void delete(long id);
	public User load(long id);
	public User loadByUsername(String username);
}

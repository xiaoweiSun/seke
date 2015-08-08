package cn.edu.pku.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.edu.pku.domain.User;

@Repository
public class UserDAOImpl extends HibernateDaoSupport implements UserDAO{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void add(User user) {
		this.getHibernateTemplate().save(user);
	}

	@Override
	public void update(User user) {
		this.getHibernateTemplate().update(user);
		
	}

	@Override
	public void delete(long id) {
		User user = this.load(id);
		this.getHibernateTemplate().delete(user);
	}

	@Override
	public User load(long id) {
		return this.getHibernateTemplate().load(User.class, id);
	}

	@Override
	public User loadByUsername(String username) {
		return (User) this.getSession()
				.createQuery("from User where username=?")
				.setParameter(0, username).uniqueResult();
	}

}

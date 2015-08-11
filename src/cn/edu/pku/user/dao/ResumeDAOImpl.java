package cn.edu.pku.user.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.edu.pku.user.domain.Education;
import cn.edu.pku.user.domain.UserInfo;
import cn.edu.pku.user.domain.WorkExperience;

@Repository
public class ResumeDAOImpl extends HibernateDaoSupport implements ResumeDAO {

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void addUserInfo(UserInfo userInfo) {
		this.getHibernateTemplate().save(userInfo);
	}

	@Override
	public void addEducation(Education education) {
		this.getHibernateTemplate().save(education);

	}

	@Override
	public void addWorkExperience(WorkExperience workExperience) {
		this.getHibernateTemplate().save(workExperience);
	}

	@Override
	public void deleteUserInfo(UserInfo userInfo) {
		this.getHibernateTemplate().delete(userInfo);
	}

	@Override
	public void deleteEducation(Education education) {
		this.getHibernateTemplate().delete(education);
	}

	@Override
	public void deleteWorkExperience(WorkExperience workExperience) {
		this.getHibernateTemplate().delete(workExperience);
	}

	@Override
	public void updateUserInfo(UserInfo userInfo) {
		this.getHibernateTemplate().update(userInfo);
	}

	@Override
	public void updateEducation(Education education) {
		this.getHibernateTemplate().update(education);
	}

	@Override
	public void updateWorkExperience(WorkExperience workExperience) {
		this.getHibernateTemplate().update(workExperience);
	}

	@Override
	public UserInfo getUserInfo(long userId) {
		return this.getHibernateTemplate().load(UserInfo.class, userId);
	}

	@Override
	public List<Education> listEducation(long userId) {
		return this.getSession().createQuery("from Education where userId=?")
				.setParameter(0, userId).list();
	}

	@Override
	public List<WorkExperience> listWorkExperience(long userId) {
		return this.getSession()
				.createQuery("from WorkExperience where userId=?")
				.setParameter(0, userId).list();
	}

}

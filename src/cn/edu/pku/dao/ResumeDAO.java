package cn.edu.pku.dao;

import java.util.List;

import cn.edu.pku.domain.Education;
import cn.edu.pku.domain.UserInfo;
import cn.edu.pku.domain.WorkExperience;

public interface ResumeDAO {

	public void addUserInfo(UserInfo userInfo);
	public void addEducation(Education education);
	public void addWorkExperience(WorkExperience workExperience);
	public void deleteUserInfo(UserInfo userInfo);
	public void deleteEducation(Education education);
	public void deleteWorkExperience(WorkExperience workExperience);
	public void updateUserInfo(UserInfo userInfo);
	public void updateEducation(Education education);
	public void updateWorkExperience(WorkExperience workExperience);
	public UserInfo getUserInfo(long userId);
	public List<Education> listEducation(long userId);
	public List<WorkExperience> listWorkExperience(long userId);
}

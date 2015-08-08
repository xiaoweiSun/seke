package cn.edu.pku.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="workexperience")
public class WorkExperience {

	long id;
	long userId;
	String jobTitle;
	String company;
	int city;
	String salary;
	Date dateBegin;
	Date dateEnd;
	String responsibilities;
	String accomplishments;
	String skillsUsed;
	
	@GeneratedValue
	@Id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="user_id")
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Column(name="job_title")
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	@Column(name="date_begin")
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	@Column(name="date_end")
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
	public String getAccomplishments() {
		return accomplishments;
	}
	public void setAccomplishments(String accomplishments) {
		this.accomplishments = accomplishments;
	}
	@Column(name="skills_used")
	public String getSkillsUsed() {
		return skillsUsed;
	}
	public void setSkillsUsed(String skillsUsed) {
		this.skillsUsed = skillsUsed;
	}
	
	
}

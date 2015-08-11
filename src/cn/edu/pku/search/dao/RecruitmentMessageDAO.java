package cn.edu.pku.search.dao;

import cn.edu.pku.search.domain.Pager;
import cn.edu.pku.search.domain.RecruitmentMessage;

public interface RecruitmentMessageDAO {

	public Pager<RecruitmentMessage> search(String field, String queryString,int offset);
	public Pager<RecruitmentMessage> search(String[] field, String[] queryString,int offset);
	
}

package cn.edu.pku.search.service;

import cn.edu.pku.search.domain.Pager;
import cn.edu.pku.search.domain.RecruitmentMessage;

public interface SearchService {

	public Pager<RecruitmentMessage> search(String field, String queryString,int offset);
	public Pager<RecruitmentMessage> search(String[] field, String[] queryString,int offset);
}

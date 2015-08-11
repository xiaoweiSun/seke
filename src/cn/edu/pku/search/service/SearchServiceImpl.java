package cn.edu.pku.search.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import cn.edu.pku.search.dao.RecruitmentMessageDAO;
import cn.edu.pku.search.domain.Pager;
import cn.edu.pku.search.domain.RecruitmentMessage;
import cn.edu.pku.util.KeySet;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

@Service
public class SearchServiceImpl implements SearchService{
	
	RecruitmentMessageDAO recruitmentMessageDao;
	
	public RecruitmentMessageDAO getRecruitmentMessageDao() {
		return recruitmentMessageDao;
	}

	@Resource
	public void setRecruitmentMessageDao(RecruitmentMessageDAO recruitmentMessageDao) {
		this.recruitmentMessageDao = recruitmentMessageDao;
	}

	public Pager<RecruitmentMessage> search(String field, String queryString,int offset){
		return recruitmentMessageDao.search(field, queryString, offset);
	}
	
	public Pager<RecruitmentMessage> search(String[] field, String[] queryString,int offset){
		return recruitmentMessageDao.search(field, queryString, offset);
	}

	
	
}

package cn.edu.pku.search.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.pku.search.domain.Pager;
import cn.edu.pku.search.domain.RecruitmentMessage;
import cn.edu.pku.search.service.SearchService;
import cn.edu.pku.util.KeySet;

@Controller
public class SearchController {
	
	SearchService searchService;

	public SearchService getSearchService() {
		return searchService;
	}

	@Resource
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	@RequestMapping("search")
	public String search(HttpServletRequest req,HttpServletResponse res) {
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String key = req.getParameter("key");
		String city = req.getParameter("city");
		int offset = Integer.parseInt(req.getParameter("offset"));
		Pager<RecruitmentMessage> searchResult;
		if((key == null || key.equals("")) && (city == null || city.equals(""))) {
			return "index.jsp";
		} 
		if(key == null || key.equals("")) {
			key = "";
			searchResult = searchService.search(KeySet.workPlace, city,offset);
		} else if(city == null || city.equals("")) {
			city = "";
			searchResult = searchService.search(KeySet.jobDescription, key,offset);
		} else {
			String[] field = new String[]{KeySet.jobDescription,KeySet.workPlace};
			String[] queryString = new String[]{key,city};
			searchResult = searchService.search(field,queryString,offset);
		}
		//int numTotalHits = search.getNumTotalHits();
		req.setAttribute("searchResult", searchResult);
		req.setAttribute("key", key);
		req.setAttribute("city", city);
		return "WEB-INF/jsp/searchResult.jsp?key=" + key + "&city=" + city;
						
	}
}

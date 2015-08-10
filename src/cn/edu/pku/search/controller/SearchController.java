package cn.edu.pku.search.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.pku.search.service.SearchFiles;
import cn.edu.pku.util.KeySet;

@Controller
public class SearchController {

	@RequestMapping("search")
	public String search(HttpServletRequest req,HttpServletResponse res) {
		String key = req.getParameter("key");
		String city = req.getParameter("city");
		List<Map<String,String>> searchResult;
		SearchFiles search = new SearchFiles();
		if((key == null || key.equals("")) && (city == null || city.equals(""))) {
			return "index.jsp";
		} 
		if(key == null || key.equals("")) {
			key = "";
			searchResult = search.doSearch(KeySet.workPlace, city);
		} else if(city == null || city.equals("")) {
			city = "";
			searchResult = search.doSearch(KeySet.jobDescription, key);
		} else {
			String[] field = new String[]{KeySet.jobDescription,KeySet.workPlace};
			String[] queryString = new String[]{key,city};
			searchResult = search.doSearch(field,queryString);
		}
		//int numTotalHits = search.getNumTotalHits();
		req.setAttribute("searchResult", searchResult);
		return "WEB-INF/jsp/searchResult.jsp?key=" + key + "&city=" + city;
						
	}
}

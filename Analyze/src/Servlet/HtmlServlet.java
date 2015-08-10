package Servlet;

import java.io.File;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.DB.MyDB;
import com.google.gson.Gson;
import com.parse.WebCrawler;
import com.parse.WebPatternParse;
import com.structure.rule.Record;
import com.structure.rule.RuleClass;

/**
 * Servlet implementation class HtmlServlet
 */
public class HtmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HtmlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	private void info(String str) {
		System.out.println("INFO: "+str);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		if (type == null) {
			System.err.println("method is null");
		}
		int ty = Integer.parseInt(type);
		switch(ty) {
			case 1:
				//载入页面
				System.err.println("case 1");
				loadHTML(request, response);
				break;
			case 2:
				parse(request, response);
				System.err.println("case 2");
				break;
			case 3:
				//保存模板
				save(request, response);
				System.err.println("case 3");
				break;
			case 4:
				//显示所有模板
				scanTemplates(request, response);
				System.err.println("case 4");
				break;
			case 5:
				//爬取
				parse(request, response);
				System.err.println("case 5");
				break;
			case 6:
				//列出所有任务
				listTask(request, response);
				System.err.println("case 6");
				break;
			case 7:
				//浏览内容
				scan(request, response);
				System.err.println("case 7");
				break;
			default:
				System.err.println("干什么来了");
				break;
		}
		
	}
	private void scan(HttpServletRequest request, HttpServletResponse response) {
		String time = request.getParameter("time");
		List<List<String[]>> resList = MyDB.getInstance().getContents(request.getRealPath(request.getRequestURI()), time);
		System.out.println(resList.size());
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(new Gson().toJson((resList)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void listTask(HttpServletRequest request, HttpServletResponse response) {
		List<String[]> resList = MyDB.getInstance().getTaskNames();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(new Gson().toJson((resList)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void scanTemplates(HttpServletRequest request, HttpServletResponse response) {
		List<String[]> resList = MyDB.getInstance().ReadTemplates();
		response.setCharacterEncoding("UTF-8");

		try {
			response.getWriter().write(new Gson().toJson((resList)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void save(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("保存");
		String ruleName = request.getParameter("ruleName");
		String ruleStr = request.getParameter("ruleStr");
		String url = request.getParameter("urlsStr");
		ruleStr.replace(" ", "");
		MyDB.getInstance().SaveTemplate(ruleName, url, ruleStr);
	}
	/**
	 * 载入要求的页面
	 * @param request
	 * @param response
	 */
	private void loadHTML(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getParameter("url");
		try {
			url  = URLDecoder.decode(url ,"utf-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		//验证路径信息提取结果
		info(url);
		int timeout = 1000;
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), timeout);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(doc!=null) info(doc.body().text());
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(new Gson().toJson((doc.html())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parse(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("解析");
		
		String ruleStr = request.getParameter("ruleStr");
		String site = request.getParameter("site");
		String countStr = request.getParameter("count");
		String name = request.getParameter("taskName");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String time = sdf.format(new Date());
		
		MyDB.getInstance().AddTaskName(name, time);
		System.out.println(ruleStr);
		Gson g = new Gson();
		RuleClass rc = g.fromJson(ruleStr, RuleClass.class);
	/*	for (int i=0; i<rc.content.size(); i++) {
			RuleUnit ru = rc.content.elementAt(i);
			//System.out.println(ru.value);
			for (int j=0; j<ru.path.size(); j++) {
				StructUnitC suc = ru.path.get(j);
				//System.out.println(suc.tagName+" "+suc.index);
			}
		}
		*/
		ArrayList<String> urls = new ArrayList<String>();
		System.out.println(site);
		urls.add(site);
		String dest = request.getRealPath(request.getRequestURI())+"/htmlDocument/"+time+"/";
		int maxCount = Integer.parseInt(countStr);
		WebCrawler.crawling(urls, dest, maxCount);
		WebPatternParse wpp = new WebPatternParse();
		Vector<Record> result = new Vector<Record>();
		for (String url: urls) {
			File file = new File(dest+WebCrawler.getFileNameByUrl(url));
			if (file.isDirectory()) {
				wpp.pickRecord(file.listFiles(), rc, result);
				System.out.println("当前网页群提取结果 "+url);
				MyDB.getInstance().SaveContents(request.getRealPath(request.getRequestURI()), time, result);
				result.clear();
			}
		}
		System.out.println("over");
	}
	
	private void outputParameter(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getParameterNames(); 
		while (names.hasMoreElements()) { 
			String string = (String) names.nextElement(); 
			System.out.println(string); 
		}
	}
	
}

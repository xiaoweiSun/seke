package com.data.get;

import java.util.HashMap;

public class PageParser {
	/**
	 * 
	 * @param page   将页面字符串传入
	 * @return   返回一个哈希表
	 */
	public static HashMap<String,String> getlink(String website,String page){
		HashMap<String,String> hm = new HashMap<String,String>();
		String head = "href=\"";
		String end = "\"";
		int findhead = page.indexOf(head);
		int findend;
		String left = page;
		while(findhead!=-1){
			left = left.substring(findhead+head.length());
			findend = left.indexOf(end);
			if(findend==-1)break;
			String url = left.substring(0, findend);
			if(!url.startsWith("http://")){
				
					url = website+"/"+url;
			}
			url = url.replace("%2F", "/");
			url = url.replace("%3A", ":");
			if(hm.get(url)==null){
				hm.put(url, url);
			}
			left = left.substring(findend+end.length());
			findhead = left.indexOf(head);
		}
		
		return hm;
	}
}

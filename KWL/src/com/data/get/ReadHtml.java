package com.data.get;

public class ReadHtml {
/**
 * 
 * @param url  访问的链接地址
 * @param wait 设置的最长等待时间 1个单位为100ms
 * @return
 */
	public static String read(String url,int wait,String cookie){
		return new doReadData(url,wait,cookie).doRead();
	}
}

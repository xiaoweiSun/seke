package com.data.get;

public class ReadHtml {
/**
 * 
 * @param url  ���ʵ����ӵ�ַ
 * @param wait ���õ���ȴ�ʱ�� 1����λΪ100ms
 * @return
 */
	public static String read(String url,int wait,String cookie){
		return new doReadData(url,wait,cookie).doRead();
	}
}

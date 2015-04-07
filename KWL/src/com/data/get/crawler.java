package com.data.get;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class crawler {
	private String url;
	private String path;
	private String debug = "crawler.java: ";
	private boolean stopflag ;
	private HashMap<String,String> Searched;
	private HashMap<String,String> Searching;
	private HashMap<String,String> toSearch;
	public crawler(String url,String path){
		this.url = url;
		this.path= path;
		this.stopflag = false;
		Searched = new HashMap();
		Searching = new HashMap();
		toSearch = new HashMap();
	}
	public void stop(){
		this.stopflag = true;
	}
	public ArrayList<String[]> start(String cookie){
		ArrayList<String[]> newfiles = new ArrayList<String[]>();
		System.out.println(cookie);
		toSearch.put(url, url);
		ArrayList<Thread> al = new ArrayList();
//		int totalThread = 32;//总线程数控制在32
		int num=0;
		while(!stopflag && (!toSearch.isEmpty()||!Searching.isEmpty())){
			 for(Iterator it=toSearch.keySet().iterator();it.hasNext();){
				 String u = (String)it.next();
				 Thread t = new searchThread(u,100,cookie);
				 al.add(t);
				 it.remove();
				 Searching.put(u, u);
			 }//将所有没有查访过的都放入待查找路径中
			 for(int i=0;i<al.size();i++){
				 if(!al.get(i).isAlive())
				 al.get(i).run();
			 }//启动所有在数组中的线程
			 for(Iterator it = al.iterator();it.hasNext();){
				 searchThread st = (searchThread)it.next();
				 if(!st.isAlive()){
					 System.out.println("st"+num+++":"+st.url);
					 Searched.put(st.url, st.url);
					 Searching.remove(st.url);
					 StringBuffer result = st.getinfo();
					 if(result==null)continue;
					 String s = new String(result);
//					 System.out.println(s);
					 String website = st.url;
					 int end = website.indexOf("/",8);
					 if(end != -1){website = website.substring(0,end);}
					 HashMap<String,String> temp = PageParser.getlink(website,s);
					 for(Iterator jt = temp.keySet().iterator();jt.hasNext();){
						 String t = (String)jt.next();
						 t= t.replace("%3A", ":");
						 t = t.replace("%2F", "/");
						 if(!Searched.containsKey(t)&&!Searching.containsKey(t)
							 &&!toSearch.containsKey(t)){
							 if(t.endsWith(".owl") && t.contains("url=http")/*!t.contains("searchString=http")*/){
								 Searched.put(t, t);
								 System.out.println("downurl "+t);
								 boolean flag = DownloadFile.downfile(t, path, 1000,cookie);
								 if(flag){//如果成功下载文件	记录下相关信息 下一步进行建立索引工作
									 String[] metadata = new String[2];
//									 System.out.println(t);
									 metadata[0] = t.substring(t.indexOf("url=http")+4);
									 metadata[1] = path+"/"+t.substring(t.lastIndexOf("/")+1);
									 newfiles.add(metadata);
								 }
									 
							 }else if(t.contains("searchSortField")){
								 toSearch.put(t, t);
							 }
						 }
					 }
					 it.remove();
				 }//将所有查找过的放入已查找线程
			 }//查找现有的线程 去除那些已经结束的线程，并且提取出结果 放入待查找路径
					
		}
		File tempfile = new File(path+"/tempfile.txt");
		try {
			if(!tempfile.exists())
			{
				tempfile.createNewFile();		
			}
			PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(
							new FileOutputStream(tempfile)));
			for(int i=0;i<newfiles.size();i++){
				pw.println(newfiles.get(i)[0]+" "+newfiles.get(i)[1]);
			}
			pw.close();
		}
		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		System.out.println("end");
		return newfiles;
	}
}
class searchThread extends Thread{
	public String url;
	private int wait;
	private StringBuffer info;
	private String cookie;
	public searchThread(String url,int wait,String cookie){
		this.url = url;
		this.cookie = cookie;
	}
	public void run(){
		String temp = ReadHtml.read(url, wait,cookie);
		if(temp!=null){
		info = new StringBuffer(temp);}
	}
	public StringBuffer getinfo(){
		return info;
	}
}

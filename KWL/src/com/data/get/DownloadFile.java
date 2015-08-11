package com.data.get;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DownloadFile {

/**
 * 
 * @param url 需要访问的文章链接
 * @param path 需要将文章保存的文件夹路径
 * @param wait 设置的最长等待时间 以100ms为1个单位
 * @return
 */
	public static boolean downfile(String url, String path,int wait,String cookie){
		doReadAndWriteData d = new doReadAndWriteData(url,path,wait,cookie);
		if(d.down())return true;
		else return false;
	}
}

class doReadAndWriteData extends doReadData{
	protected String filepath;
	private String url;
	public doReadAndWriteData(String url,String path,int times,String cookie){
		super(url,times,cookie);
		this.url = url;
		filepath = path;
	}
	public boolean down(){
		String filecontent = doRead();
		if(filecontent==null){System.out.println("null");}
		if(filecontent!=null){
			String filename = url.substring(url.lastIndexOf("/")+1);
			String fullname = filepath+"/"+filename;
			File file = new File(fullname);
			while(file.exists()){
				//出现重名怎么办
				return false;
//				String first = fullname.substring(0,fullname.indexOf("."))+"(1)";
//				String last = fullname.substring(fullname.indexOf("."));
//				fullname = first+last;
//				file = new File(fullname);
				
			}
			System.out.println(fullname);
			
			try {
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				fw.write(filecontent);
				fw.flush();
				fw.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return false;
	}
}
package com.data.get;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class doReadData {
	protected String swooglecookie;
	protected String[] info;
	protected int waittimes;
	public doReadData(String u,int waittimes,String swooglecookie){//读取不同数据可以适当放宽等待时间
		this.info = new String[2];
		this.info[0] = u;
		this.waittimes = waittimes;
		this.swooglecookie = swooglecookie;
	}
	public String doRead(){
		Thread temp = new htmlThread(info,swooglecookie);
		temp.start();
		/**
		 * 
		 */
		synchronized(this){
			waittimes=100;
			while( waittimes-->0){
				try {
					this.wait(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(temp !=null && temp.isAlive())
						temp.interrupt();
				}//每次等待100ms
				if(!temp.isAlive()) break;
			}
			if(temp !=null && temp.isAlive())
				temp.interrupt();//超过10000ms还没有读取完 中断线程
		}
//		System.out.println(info[1]);
		return info[1];
	}
}

class htmlThread extends Thread{
	private String[] info;
	private String swooglecookie;
	public htmlThread(String[] _info,String swooglecookie){
		this.info = _info;
		this.swooglecookie = swooglecookie;
	}
	public void run(){
		
		try {
			URL myurl = new URL(info[0]);//使用的是url
			HttpURLConnection huc = (HttpURLConnection)myurl.openConnection();
			if(info[0].contains("swoogle")){
				huc.setDoInput(true);
				huc.setRequestMethod("GET");
				huc.setRequestProperty("Cookie", this.swooglecookie);
			}
			huc.connect();
			int httpResult = huc.getResponseCode();
			if(httpResult != HttpURLConnection.HTTP_OK){
				System.out.println(httpResult+":"+huc.getResponseMessage()+" "+info[0]);
			}else{
				
				InputStreamReader isr = new InputStreamReader(huc.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line = "";
				while((line = br.readLine())!=null){
					sb.append(line);//当线程被终止时 后面仍然会完成
				}
				huc.disconnect();
				isr.close();
				br.close();
				info[1] = sb.toString();
//				System.out.println("Read: "+info[1]);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
package com.DB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.structure.rule.Feature;
import com.structure.rule.Record;

public class TextDB extends MyDB{

	
	String filepath = "patternDB.db";
	String taskpath = "taskDB.db";
	@Override
	public void SaveTemplate(String name, String url, String template) {
		// TODO Auto-generated method stub
		try {
			
			File f = new File(filepath); 
			FileWriter fw = new FileWriter(f, true);
			System.out.println(f.getAbsolutePath());
			fw.append(name);
			fw.append(" ");
			fw.append(url);
			fw.append(" ");
			fw.append(template);
			fw.append("\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<String[]> ReadTemplates() {
		// TODO Auto-generated method stub
		List<String[]> resList =new ArrayList<String[]>();
		try {
			File f = new File(filepath);
			System.out.println(f.getAbsolutePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String text ;
			while ((text = br.readLine()) != null) {
				String[] res = text.split(" ",3);
				resList.add(res);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resList;
	}

	@Override
	public boolean AddTaskName(String name, String time) {
		// TODO Auto-generated method stub
		try {
			File t = new File(filepath);
			if (!t.exists()) {
				t.createNewFile();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(t)));
			String text ;
			System.out.println();
			while ((text = br.readLine()) != null) {
				String[] res = text.split(" ");
				if (res[0].compareTo(name) == 0) {
					br.close();
					return false;
				}
			}
			br.close();
			File f = new File(taskpath); 
			FileWriter fw = new FileWriter(f, true);
			System.out.println(f.getAbsolutePath());
			fw.append(name);
			fw.append(" ");
			fw.append(time);
			fw.append(" ");
			fw.append("\n");
			fw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String[]> getTaskNames() {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(taskpath))));
			String text ;
			List<String[]> resList = new ArrayList<String[]>();
			while ((text = br.readLine()) != null) {
				String[] res = text.split(" ");
				resList.add(res);
			}
			br.close();
			return resList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	String path = "result";
	private final static String splitSymbol = "@#@#@#";
	@Override
	public List<List<String[]>> getContents(String rootpath, String time) {
		// TODO Auto-generated method stub
		List<List<String[]>> resV = new ArrayList<List<String[]>>();
		BufferedReader br;
		try {
			File f = new File(rootpath +"/" + path + "/"+ time + ".txt");
			System.out.println(f.getAbsolutePath());
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			String text ;
			List<String[]> resList = new ArrayList<String[]>();
			while ((text = br.readLine()) != null) {
				System.out.println(text);
				if (text.compareTo("----") == 0) {
					resV.add(resList);
					System.out.println("find end");
					resList = new ArrayList<String[]>();
				} else {
					String[] res = text.split(TextDB.splitSymbol);
					resList.add(res);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resV;
	}

	@Override
	public void SaveContents(String rootpath, String time, Vector<Record> content) {
		// TODO Auto-generated method stub
		
		try {
			
			File file = new File("/home/shawn/crawler/result/" + time);
			file.mkdirs();
			File res;
			BufferedWriter bw;
			
			for (Record r:content) {
				res = new File("/home/shawn/crawler/result/" + time + "/" + r.uri + ".txt");
				System.out.println(res.getAbsolutePath());
				if (!res.exists()) {
					res.createNewFile();
				}
				bw = new BufferedWriter(
										new OutputStreamWriter(
												new FileOutputStream(res, true),"UTF-8"));
				
				System.out.println("");
				//bw.append(r.uri + "\n");
				for (Feature f:r.features) {
					System.out.println(f.key + " "+f.value);
					bw.append(f.key + TextDB.splitSymbol+f.value);
					bw.append("\n");
				}
				bw.flush();
				bw.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

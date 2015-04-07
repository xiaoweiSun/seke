package com.tool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ServletPackage.Config;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.index_search.IndexFile;
import com.net.ServerResponse;
import com.owl.graph.ShapeGraph;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class toolProcess {

	public void save(String realpath, HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out;
		try {
			out = response.getWriter();
			String json = request.getParameter("json");
			json = URLDecoder.decode(json,"utf-8");
			Gson gson = new Gson();
			StringMap<?> sm = gson.fromJson(json, StringMap.class);
			StringMap<?> params = (StringMap<?>) sm.get("params");
			StringMap<?> obj = (StringMap<?>) params.get("obj");
			String name = (String) obj.get("name");
			String xml = (String) obj.get("xml");
			String path = realpath + Config.editorGraphPath + name + ".ogeditor";
			path = path.replaceAll("\\\\","/").replaceAll("//", "/");
			System.out.println(path);
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
				FileWriter writer = new FileWriter(path);
				writer.write(xml);
				writer.close();
				// 添加索引   方式太简单  先注释掉
//				new IndexFile().IndexFromLocal("editor", realpath,
//						new String[] { file.getAbsolutePath() });
			} else {
				FileWriter writer = new FileWriter(path);
				writer.write(xml);
				writer.close();
			}
			
			System.out.println(xml);
			ShapeGraph graph = new ShapeGraph();
			String filepath = realpath+Config.graph2OwlPath+name+".owl";
			filepath = filepath.replaceAll("\\\\", "/").replaceAll("//", "/");
			graph.saveAsOwlFromXml(xml, filepath);
			
			response.getWriter().write("success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private InputStream String2InputStream(String str, String characterSet) throws UnsupportedEncodingException {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes(characterSet));
		return stream;
	}


	public void read(String realpath, HttpServletRequest request,
			HttpServletResponse response) {
		String json = request.getParameter("json");
		json = URLDecoder.decode(json);
		Gson gson = new Gson();
		StringMap<?> sm = gson.fromJson(json, StringMap.class);
		StringMap<?> params = (StringMap<?>) sm.get("params");
		String filename = (String) params.get("filename");
		System.out.println(filename);
		String path = realpath + Config.editorGraphPath + filename + ".ogeditor";
		path = path.replaceAll("\\\\", "/").replaceAll("//", "/");
		File file = new File(path);
		System.out.println(path);
		try {
			if (file.exists()) {
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				System.out.println("read:" + path);
				System.out.println(sb.toString());
				ServerResponse res = new ServerResponse("102", sb.toString());
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(gson.toJson(res));

			} else {
				response.getWriter().write("error");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getFiles(String realpath, HttpServletRequest request,
			HttpServletResponse response) {
		File file = new File(realpath + Config.editorGraphPath);
		String files[] = file.list();
		Gson gson = new Gson();
		ServerResponse res = new ServerResponse("102", files);
		try {
			response.getWriter().write(gson.toJson(res));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveTDB(String realpath, HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out;
		try {
			out = response.getWriter();
			String json = request.getParameter("json");
			json = URLDecoder.decode(json,"utf-8");
			Gson gson = new Gson();
			StringMap<?> sm = gson.fromJson(json, StringMap.class);
			StringMap<?> params = (StringMap<?>) sm.get("params");
			StringMap<?> model = (StringMap<?>) params.get("model");
			System.out.println(model);
			out.print("OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void saveDirectPathAsOwl(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter out;
		try {
			out = response.getWriter();
			String json = request.getParameter("json");
			json = URLDecoder.decode(json,"utf-8");
			Gson gson = new Gson();
			StringMap<?> sm = gson.fromJson(json, StringMap.class);
			StringMap<?> params = (StringMap<?>) sm.get("params");
			StringMap<?> obj = (StringMap<?>) params.get("obj");
			String name = (String) obj.get("name");
			String xml = (String) obj.get("xml");
			String path = (String) obj.get("filepath");
			path = path.replaceAll("\\\\","/").replaceAll("//", "/");
			System.out.println("direct path:"+path);			
			System.out.println(xml);
			ShapeGraph graph = new ShapeGraph();

			graph.saveAsOwlFromXml(xml, path);
			
			response.getWriter().write("success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void readDirectPathFromOwl(HttpServletRequest request,
			HttpServletResponse response) {
		String json = request.getParameter("json");
		json = URLDecoder.decode(json);
		Gson gson = new Gson();
		StringMap<?> sm = gson.fromJson(json, StringMap.class);
		StringMap<?> params = (StringMap<?>) sm.get("params");
		String filepath = (String) params.get("filepath");
		System.out.println(filepath);
		filepath = filepath.replaceAll("\\\\", "/").replaceAll("//", "/");
		
		ShapeGraph sg = new ShapeGraph();
		String xml = sg.loadAsXmlFromOwl(filepath);
		System.out.println(xml);
		try {
			ServerResponse res = new ServerResponse("102", xml);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(gson.toJson(res));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

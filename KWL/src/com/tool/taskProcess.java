package com.tool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ServletPackage.DataBaseMan;
import ServletPackage.IpGetter;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.net.ServerResponse;
import com.structure.task.Task;
import com.task.TaskProcedure;

public class taskProcess {

	//���뵱ǰ��Ӧ��task ����ontology����  ��ȡ��Ӧ��task�����û�ж�Ӧ��task���ͷ���null
	public void loadTask(String realpath, HttpServletRequest request,
			HttpServletResponse response, DataBaseMan dbMan) {
		try {
			String json = request.getParameter("json");
			json = URLDecoder.decode(json,"utf-8");
			Gson gson = new Gson();
			StringMap<?> sm = gson.fromJson(json, StringMap.class);
			StringMap<?> params = (StringMap<?>) sm.get("params");
			String ontology = (String) params.get("ontology");
			//-----start connect mysql
			Task task = new Task();
			ServerResponse res;
			if (task.loadFromDatabaseByOntology(dbMan, ontology)) {
				//�ɹ��������
				res = new ServerResponse("102", task);
			} else {
				//û�ж�Ӧ����  ��Ҫ�ڿͻ����½�
				res = new ServerResponse("102", "");
			}
			String resStr = gson.toJson(res);
//			System.out.println(resStr);
			try {
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(resStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public void saveTask(String realpath, HttpServletRequest request,
			HttpServletResponse response, DataBaseMan dbMan) {
		try {
			String json = request.getParameter("json");
			json = URLDecoder.decode(json,"utf-8");
			Gson gson = new Gson();
			StringMap<?> sm = gson.fromJson(json, StringMap.class);
			StringMap<?> params = (StringMap<?>) sm.get("params");
//			System.out.println(gson.toJson(params.get("task")));
			Task task = gson.fromJson(gson.toJson(params.get("task")), Task.class);
			ServerResponse res ;
			if (task.saveOnDatabase(dbMan)) {
				//�ɹ��������ݿ�
				res = new ServerResponse("102", "success");
			} else {
				res = new ServerResponse("102", "fail");
			}
			try {
				response.getWriter().write(gson.toJson(res));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void execTask(String realpath, HttpServletRequest request,
			HttpServletResponse response, DataBaseMan dbMan) {
		Gson gson = new Gson();
		ServerResponse res;
		String json = request.getParameter("json");
		try {
			json = URLDecoder.decode(json,"utf-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		StringMap<?> sm = gson.fromJson(json, StringMap.class);
		StringMap<?> params = (StringMap<?>) sm.get("params");
		String taskId = (String)params.get("taskId");
//		System.out.println(taskId);
		Task task = new Task();	
		if (task.loadFromDatabaseById(dbMan, taskId)) {
			//�ɹ��������
			
			System.out.println("execting task "+taskId);
			//�����������߳�
			TaskProcedure tp = new TaskProcedure(task, realpath, dbMan);
			String tdbName = tp.getTdbName();
			Thread thread = new Thread(tp,"threadTest");
			thread.start();
			String ip = "";
			int port = 8080;
			//����Ϊ�첽�Ϳɲ��ڹ���
			ip = IpGetter.getLocalIP();
			port = request.getServerPort();
			
			res = new ServerResponse("102", "task processing !      "+ ip + ":" + Integer.toString(port) +"/KWL/disfield.jsp tdbname:"+ tdbName);
		} else {
			//û�ж�Ӧ����  ��Ҫ�ڿͻ����½�
			res = new ServerResponse("102", "fail");
		}
		ServerResponse.writeJsonResult(response, res);
	}
}
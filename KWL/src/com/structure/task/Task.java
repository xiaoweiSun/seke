package com.structure.task;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import ServletPackage.DataBaseMan;
import ServletPackage.MyResultSet;

import com.google.gson.Gson;

public class Task implements Serializable{
	
	public static final long serialVersionUID = 1231212312;
	//��flex��Ӧ�����ݽṹ
	public String taskId;
	public int patternNum;
	public String ontology;
	public Vector<TaskPattern> patternList;
	
	public Task() {
		this.patternList = new Vector<TaskPattern>();
	}
	//ֱ��ת��Ϊjson��ʽ
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	//�����ݿ��м���(���ݹ����ı�����)
	public boolean loadFromDatabaseByOntology(DataBaseMan dbMan, String ontology) {
		String queryTask = "select taskId from task_profile where ontology = '"
							+ ontology + "'";
		dbMan.SetStmt(queryTask);
		MyResultSet taskrs = dbMan.ExecQuery();
		if (!taskrs.first()) {
			//û���������
			return false;
		}
		this.taskId = taskrs.getString(1);
		this.ontology = ontology;
		return this.loadFromDatabase(dbMan, this.taskId);
	}
	//�����ݿ��м���(���ݹ�����������)
	public boolean loadFromDatabaseById(DataBaseMan dbMan, String id) {
		String queryTask = "select ontology from task_profile where taskId = '"
							+ id + "'";
		dbMan.SetStmt(queryTask);
		MyResultSet ontors = dbMan.ExecQuery();
		if (!ontors.first()) {
			//û����ر���
			return false;
		}
		this.ontology = ontors.getString(1);
		this.taskId = id;
		return this.loadFromDatabase(dbMan, this.taskId);
	}	
	//�����ݿ�������
	public boolean loadFromDatabase(DataBaseMan dbMan, String id) {
		String query = 
				"select patternId, patternName, patternType , domain ," +
				" objList , description " +
				" from pattern_profile " +
				" where taskId = '" + id +" '";
		dbMan.SetStmt(query);
		MyResultSet rs = dbMan.ExecQuery();
		if (!rs.first()) {
			//û���κ���ؽ��  ˵����������� ����û���κ�pattern
			return true;
		}
		//�����1��ʼ
		for (;!rs.isAfterLast();rs.next()) {
			TaskPattern pattern = new TaskPattern();
			pattern.patternId = rs.getString(1);
			pattern.patternName = rs.getString(2);
			pattern.patternType = rs.getString(3);
			pattern.domain = rs.getString(4);
			pattern.objList = rs.getString(5);
			try {
				pattern.description = java.net.URLDecoder.decode(rs.getString(6),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.patternList.add(pattern);
		}
		//ͳ�����������
		this.patternNum = this.patternList.size();
		return true;
	}
	
	//�����е����ݴ洢�����ݿ���
	public boolean saveOnDatabase(DataBaseMan dbMan) {
		
		String pattern_PROFILE = "pattern_profile";
		String task_PROFILE = "task_profile";
		//������ɣ����� ���Ҵ��Ӧ��taskId, ����ɾ�������е���Ϣ(��ɾ��pattern_profile)
		String delete = "delete  from " + pattern_PROFILE + " where taskId = '" +
				this.taskId + "'";
		dbMan.SetStmt(delete);
		dbMan.ExecUpdate();
		//��ɾ��task_profile
		delete = "delete  from " + task_PROFILE + " where taskId = '" +
				this.taskId + "'";
		System.out.println(delete);
		dbMan.SetStmt(delete);
		dbMan.ExecUpdate();
		System.out.println("delete success!");
		
		//���task_profile
		String insert = "insert into " + task_PROFILE + 
				"(taskId, ontology) value(" +
				"'" + this.taskId + "'," +
				"'" + this.ontology + "')";
		dbMan.SetStmt(insert);
		System.out.println(insert);
		if (!dbMan.ExecUpdate()) return false;
		//���pattern_profile
		TaskPattern pattern;
		for (int i=0; i<this.patternList.size(); i++) {
			pattern = this.patternList.get(i);
			if (pattern.patternType.equals("lp")) {
				pattern.description = pattern.description.replaceAll(" ", "+");
			}
			System.out.println(pattern.description);
			try {
				insert = "insert into " + pattern_PROFILE + " value(" +
						"'" + pattern.patternId + "'," +
						"'" + pattern.patternName + "'," +
						"'" + pattern.patternType + "'," +
						"'" + pattern.domain + "'," +
						"'" + pattern.objList + "'," +
						"'" + java.net.URLEncoder.encode(pattern.description,"utf-8") + "'," +
						"'" + pattern.taskId + "')";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(insert);
			dbMan.SetStmt(insert);
			if (!dbMan.ExecUpdate()) return false;
		}
		return true;
	}
}

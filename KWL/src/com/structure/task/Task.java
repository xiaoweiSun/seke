package com.structure.task;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import ServletPackage.DataBaseMan;
import ServletPackage.MyResultSet;

import com.google.gson.Gson;

public class Task implements Serializable{
	
	public static final long serialVersionUID = 1231212312;
	//与flex对应的数据结构
	public String taskId;
	public int patternNum;
	public String ontology;
	public Vector<TaskPattern> patternList;
	
	public Task() {
		this.patternList = new Vector<TaskPattern>();
	}
	//直接转换为json格式
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	//从数据库中加载(根据关联的本体名)
	public boolean loadFromDatabaseByOntology(DataBaseMan dbMan, String ontology) {
		String queryTask = "select taskId from task_profile where ontology = '"
							+ ontology + "'";
		dbMan.SetStmt(queryTask);
		MyResultSet taskrs = dbMan.ExecQuery();
		if (!taskrs.first()) {
			//没有相关任务
			return false;
		}
		this.taskId = taskrs.getString(1);
		this.ontology = ontology;
		return this.loadFromDatabase(dbMan, this.taskId);
	}
	//从数据库中加载(根据关联的任务名)
	public boolean loadFromDatabaseById(DataBaseMan dbMan, String id) {
		String queryTask = "select ontology from task_profile where taskId = '"
							+ id + "'";
		dbMan.SetStmt(queryTask);
		MyResultSet ontors = dbMan.ExecQuery();
		if (!ontors.first()) {
			//没有相关本体
			return false;
		}
		this.ontology = ontors.getString(1);
		this.taskId = id;
		return this.loadFromDatabase(dbMan, this.taskId);
	}	
	//从数据库中载入
	public boolean loadFromDatabase(DataBaseMan dbMan, String id) {
		String query = 
				"select patternId, patternName, patternType , domain ," +
				" objList , description " +
				" from pattern_profile " +
				" where taskId = '" + id +" '";
		dbMan.SetStmt(query);
		MyResultSet rs = dbMan.ExecQuery();
		if (!rs.first()) {
			//没有任何相关结果  说明有这个任务 但是没有任何pattern
			return true;
		}
		//结果从1开始
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
		//统计最后结果数量
		this.patternNum = this.patternList.size();
		return true;
	}
	
	//将现有的数据存储到数据库中
	public boolean saveOnDatabase(DataBaseMan dbMan) {
		
		String pattern_PROFILE = "pattern_profile";
		String task_PROFILE = "task_profile";
		//两步完成，首先 ，找打对应的taskId, 并且删除掉旧有的信息(先删掉pattern_profile)
		String delete = "delete  from " + pattern_PROFILE + " where taskId = '" +
				this.taskId + "'";
		dbMan.SetStmt(delete);
		dbMan.ExecUpdate();
		//再删掉task_profile
		delete = "delete  from " + task_PROFILE + " where taskId = '" +
				this.taskId + "'";
		System.out.println(delete);
		dbMan.SetStmt(delete);
		dbMan.ExecUpdate();
		System.out.println("delete success!");
		
		//添加task_profile
		String insert = "insert into " + task_PROFILE + 
				"(taskId, ontology) value(" +
				"'" + this.taskId + "'," +
				"'" + this.ontology + "')";
		dbMan.SetStmt(insert);
		System.out.println(insert);
		if (!dbMan.ExecUpdate()) return false;
		//添加pattern_profile
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

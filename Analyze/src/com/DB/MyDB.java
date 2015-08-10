package com.DB;

import java.util.List;
import java.util.Vector;

import com.structure.rule.Record;

public abstract class MyDB {
	public abstract void SaveTemplate(String name, String url, String template);
	public abstract List<String[]> ReadTemplates();
	public abstract boolean AddTaskName(String name, String time);
	public abstract List<String[]> getTaskNames();
	public abstract List<List<String[]>> getContents(String rootpath, String time);
	public abstract void SaveContents(String rootpath, String time, Vector<Record> content);
	protected MyDB() {};
	private static MyDB Instance;
	public static MyDB getInstance() {
		if (Instance == null) Instance = new TextDB();
		return Instance;
	}
}

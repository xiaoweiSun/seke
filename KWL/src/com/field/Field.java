package com.field;


import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ServletPackage.Config;
import ServletPackage.DataBaseMan;
import ServletPackage.MainServlet;
import ServletPackage.MyResultSet;

import com.net.ServerResponse;

public class Field {
	public static void debug(String str) {
		System.out.println(Field.class.getName() + " " +str);
	}
	public static void saveField(String realpath, MainServlet servlet ,DataBaseMan dbMan, 
			HttpServletRequest req, HttpServletResponse resp){
		String eles[] = new String[]{"username","tag", "description", "fieldName", "isFinished"};
		String values[] = new String[eles.length];
		
		for (int i=0; i<eles.length; i++) {
			values[i] = req.getParameter(eles[i]);
			if (values[i] == null) {
				values[i] = "''";
				continue;
			}
			try {
				values[i] = "'"+ java.net.URLEncoder.encode(values[i],"utf-8")+ "'";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		debug(values[1]);
		MyResultSet rs = dbMan.SelectCond(Config.field_table, " fieldName = "+values[3]);
		ServerResponse sr;
		if (rs.first()) {
			//如果数据库中已经有同名的领域库  覆盖
			String sql = "update "+Config.field_table+" set ";
			for (int i=0; i<eles.length; i++) {
				sql += eles[i] + "=" + values[i] ;
				if (i < eles.length-1) sql += ",";
			}
			sql += " where "+eles[3]+" = "+values[3];
			dbMan.SetStmt(sql);
			dbMan.ExecUpdate();
			sr = new ServerResponse("0", "覆盖成功！");
		} else {
			dbMan.InsertRow(Config.field_table, eles, values);
			sr = new ServerResponse("0", "创建成功！");
		}
		ServerResponse.writeJsonResult(resp, sr);
	}
	
	public static void getTDBs(String realpath, MainServlet servlet ,DataBaseMan dbMan, 
			HttpServletRequest req, HttpServletResponse resp){
		String eles[] = new String[]{"fieldName" , "tag", "description" , "isFinished"};
		String sql = " select fieldName , tag, description , isFinished from "+Config.field_table;
		dbMan.SetStmt(sql);
		MyResultSet rs = dbMan.ExecQuery();
		ServerResponse sr ;
		if (!rs.first()) {
			sr = new ServerResponse("1", "tdb is null");
			ServerResponse.writeJsonResult(resp, sr);
			return ;
		}
		//如果为空
		String tdbArr[][] = new String[rs.total][eles.length];
		int row = 0;
		for (;!rs.isAfterLast();rs.next()) {
			for (int i=0; i<eles.length; i++) {
				try {
					tdbArr[row][i] = java.net.URLDecoder.decode(rs.getString(eles[i]),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			row ++;
		}
		sr = new ServerResponse("0", tdbArr);
		ServerResponse.writeJsonResult(resp, sr);
	}
}

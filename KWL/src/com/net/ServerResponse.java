package com.net;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ServerResponse {
	String res;
	Object data;
	public ServerResponse(String res, Object obj){
		this.res = res;
		this.data = obj;
	}
	public static void writeDirectResult(HttpServletResponse response, String value) {
		try {
			response.getWriter().write(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void writeJsonResult(HttpServletResponse response, Object value) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson(value));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

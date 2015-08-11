package com.task;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import com.structure.rule.*;



/**
 * 
 * @author lenovo
 * ʵ�ַ�����Ѱ��content�а����Ĺؼ��֣����Ұ�·����˳���¼����
 * ʵ�ֵ�ʱ�� �����ҳ�ṹ��ͬ���ܹ�׼ȷ����ȡ����ص�����
 * ���ǳ�������ҳ�ṹ���Ƶ����ݲ���ͬ�����
 * Ϊ�˽��н�׳�ԵĸĽ�   �༴����������Ҳ�����Ӧ������ ��ô�ſ�����Ѱ���ֵܽڵ�����
 */


public class WebPatternParse {
	
	public static boolean LOG = true;
	public BufferedWriter log = null;
	public Date date = null;
	public SimpleDateFormat dateformat= null;
	public String logAddr = null;
	public String charset = "utf-8";
	
	private static String charsetName = "utf-8";
	private static String ruleFile1,ruleFile4;
	private static File[] targetFiles;
	private static int total = 0;
	private static boolean debugThisClass = false;
	static {
		//static�ĳ�ʼ��
	}
	public  void debug(String str) {
		if (debugThisClass) {
			System.out.println("WebpatternParse: "+str);
		}
	}
	public void debug(Integer s) {
		if (debugThisClass) {
			System.out.println("WebpatternParse: "+s);
		}
	}
	/**
	 * �����µ�˼·�����巽�������ȸ��������������Ĳ�νṹ��������վӦ��Ϊ��νṹ����̶���
	 * ����ǣ��������ľ���ڵ�λ�ã���Ҫͨ��key�����ݣ�����Ӧ������������Ȼ���ԣ�
	 * ƥ�����Ժ������Ϊ�ǽ��뵽��ȷ������
	 * 
	 * �Ľ��ɴ�������ҳ������������⣬ ����·���ϵĹؼ��ֲ�����ÿһ�����ݳ��ֶ�Ӧ��ϵ��
	 * Ҳ����֮ǰ��Լ���������ؼ��ֱض�����·�������߼����⣬��ȷ��˵���ǣ��ض�������·���ϵĽڵ�����������У�
	 * ���Ǹĳ�������ȷ���߼�������������⣬������Ϣ�������㣬�жϲ���׼ȷ��
	 * ��Ϊ��������̫ϡ�٣�������ҪһЩ���������ݣ��ֵ����� ����  ���ڵ�spanʲô��
	 * 
	 * ������н������ ���˲��ܱ�֤�ҵ�
	 * @param args
	 */
//	public static void main(String args[]) {
////		ruleFile1 = "resource/1.htm";
////		ruleFile4 = "resource/4.htm";
//		
//		String ruleFileNew = "data/2.htm";
//		File dir = new File("data");
//		if (dir.isDirectory()) {
//			targetFiles = dir.listFiles();
//		}
//
//		
//		RuleClass rule2 = new AnalyzeHtmlTwoPart().analyzeSample(ruleFileNew);
////		Gson gson = new Gson();
////		String temp = gson.toJson(rule2);
////		System.out.println(temp);
//		Vector<Vector<String[]>> result = new Vector<Vector<String[]>>();
////		RuleClass rl = gson.fromJson(temp, RuleClass.class);
//		RuleClass rl = rule2;
//		new AnalyzeHtmlTwoPart().getKey2(targetFiles, rl, result);
////		System.out.println(new Gson().toJson(result));
//	}
	
	public void processRuleString(String srcDir, String html, Vector<Record> result) {
		RuleClass rule = analyzeSample(html);
//		debug(html);
		//��ȡ����
		debug("rule " + new Gson().toJson(rule));
		File dir = new File(srcDir);
		debug(srcDir);
		if (dir.isDirectory()) {
			debug("find dir");
			File files[] = dir.listFiles();
			getKey2(files, rule, result);
		}
	}
	
	public RuleClass analyzeSample(String html) {
		RuleClass rule2 = new RuleClass();
		Vector<ContentUnitC> matchString = new Vector<ContentUnitC>();
		
		String startBody = "**@@";
		String endName = "@@@@";
		String startContent = "||";
		String endContent = "||**";
		String startKey = "##^^";
		String endKey = "$$##";
		//Ѱ�ұ�ǵ����ֺ����ݵĹ���
		String nextHtml = html,nameOfHtml, contentOfHtml;
		int startCInd = nextHtml.indexOf(startBody);
		int endNameInd,endContentInd,startContentInd;
		while (startCInd != -1) {
			//����ҵ�һ�����ܵĹؼ���
			//�����µ��Ӵ����ԣ��������ݴӵ�һ����ʼ
			endNameInd = nextHtml.indexOf(endName);
			debug(startCInd+startBody.length());
			debug(endNameInd);
			if (endNameInd == -1) {
				debug(" match fail: "+endName);
				break;
			}
			nameOfHtml = nextHtml.substring(startCInd+startBody.length(), endNameInd);
			//���������ı�Ƕ���  �������ݴӵ�һ����ʼ
			startContentInd = nextHtml.indexOf(startContent);
			endContentInd = nextHtml.indexOf(endContent);
			contentOfHtml = nextHtml.substring(startContentInd + startContent.length(), endContentInd);
			
			nextHtml = nextHtml.substring(endContentInd + endContent.length());
			startCInd = nextHtml.indexOf(startBody);
			
			ContentUnitC cu = new ContentUnitC();
			cu.content = contentOfHtml;
			cu.name = nameOfHtml;
			debug(" name:"+cu.name+" content:"+cu.content);
			matchString.add(cu);
//			debug(cu.name+":"+cu.content);
		}

		//Ѱ�ҹؼ��ʵĹ���(ÿһ���㼶��������ȫ���ؼ��ʣ�������ϵ���
		//��ȡ�Ľ��취  ����owntext����ֱ�ӱ����� text�Ļ�ֻ�������������һ��
		String keyOfHtml;
		int startKeyInd,endKeyInd;
		nextHtml = html;
		startKeyInd = nextHtml.indexOf(startKey);
		while (startKeyInd != -1) {
			//�ҵ�һ���ؼ��ʵĿ�ͷ
			nextHtml = nextHtml.substring(startKeyInd+startKey.length());
			endKeyInd = nextHtml.indexOf(endKey);
			keyOfHtml = nextHtml.substring(0, endKeyInd);
			nextHtml = nextHtml.substring(endKeyInd + endKey.length());
			for (ContentUnitC cu: matchString) {
				cu.keys.add(keyOfHtml);
			}
			startKeyInd = nextHtml.indexOf(startKey);
		}
		new WebPatternParse().buildRule(html, rule2, matchString);
		return rule2;
	}
	
	//
	/**
	 * ������  ��������¼·����ͬʱҪ��׼ȷ���ж�Ӧ��ǩ
	 * 
	 */
	private void buildRule(String html,RuleClass rule,Vector<ContentUnitC> matchString) {
		Document doc = Jsoup.parse(html, charsetName);
		Element body = doc.body();
		String text = body.text();
		Vector<StructUnitC> path = new Vector<StructUnitC>();
		for (ContentUnitC content:matchString) {
			if (text.contains(content.content)) {
				searchKey2(body, path,rule,content);	
			}
		}
	}
	
	private void searchKey2(Element element, Vector<StructUnitC> path,RuleClass rule,ContentUnitC content) {
		Elements eles = element.children();
		int index = 0;
		//���еĺ��ӽ��б���
		for (Element ele: eles) {
			String txt = ele.text();
			if (txt.contains(content.content)) {
				String tagName = ele.tagName();
				int seri = -1,childpos = 0;
				for (Element te:eles) {
					if (te.tagName().compareTo(tagName)==0) seri++;
					if (childpos>=index) break;
					childpos++;
				}
				path = (Vector<StructUnitC>) path.clone();
				StructUnitC su = new StructUnitC();
				su.tagName = tagName;
//				debug(" tagName:"+tagName);
//				debug(" text:"+txt);
				su.index = seri;
				//��Ҫ��֮ǰ�����ݴ���һ�� ��ֹ��ʶ��ɹؼ���
				for (String key:content.keys) {
					if (txt.contains(key)) {
						//�ڵ�������ӵ�еĲŽ��м�¼
						su.keys.add(key);
//						debug(" key:"+key);
					}
				}
				path.add(su);
				searchKey2(ele, path,rule,content);
			}
			index++;
		}
		//��ֻ�����Լ������ݽ��в���  �Ƿ�����
		String owntxt = element.ownText();
		if (owntxt.contains(content.content)) {
			RuleUnit ru = new RuleUnit();
			ru.path = path;
			ru.name = content.name;
			rule.content.add(ru);
//			debug(owntxt);
		}
	}
	/**
	 * 
	 * @param files
	 * @param rule
	 * @param contents
	 */
	public void getKey2(File files[], RuleClass rule, Vector<Record> result) {
		//���뱣֤ ���û���κ����ݱ���ȡ����  ��ô�Ͳ�������Ӧ��model
		Record r;
		for (File file:files) {
			//ÿ���ļ����ж�Ӧ����������¼
			debug(total++);
			r = new Record();
			r.uri = file.getName();
			try {
				Document doc = Jsoup.parse(file,charsetName);
				Element body = doc.body();
				debug(body.text());
				String path = "";
				String spaces = "";
				Feature features = null; 
				for (RuleUnit ru:rule.content) {
					spaces = "";
//					debug(body.text());
					features = new Feature();
					if (!getPoint2(body,ru.path,0,path,ru.name, features)) {
						for (int i=0; i<5-ru.name.length(); i++) {
							spaces += "  ";
						}
						debug(ru.name+ spaces + ": " +ru.path.get(ru.path.size()-1).keys.toString()+" not found");
					} else {
						r.features.add(features);
						debug(ru.name+ spaces + ": " +ru.path.get(ru.path.size()-1).keys.toString()+" found "+features.value);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (r.features.size() <= 0) {
				debug(r.uri + " don't match pattern!!");
			}
			else {
				result.add(r);
			}
		}
	}
	
	private boolean getPoint2(Element body,Vector<StructUnitC> rule,int index, String path, String name, Feature feature) {
		/**
		 * ��������һ�� ������ս��
		 */
//		debug(path+" : name| "+body.html() + " index:"+index +" rule:"+rule.size());
		if (index >= rule.size()) {
			String spaces = "";
			for (int i=0; i<5-name.length(); i++) {
				spaces += "  ";
			}
			feature.key = name;
			//��html�еĿո�&nbsp;ת��Ϊ"" ��������ı��б���ʺ�
			feature.value = body.text().replace(Jsoup.parse("&nbsp;").text(), "");
//			debug(name + spaces + ": " + feature.value);
			return true;
		}
		StructUnitC su = rule.get(index);
		String tagName = su.tagName;
		int offsetIndex = su.index;
		Elements tagChilds = body.children();
		if (offsetIndex >= tagChilds.size()) {
//			debug(" offset:"+offsetIndex+" tag:"+tagChilds.size());
			return false;
		}
		/**
		 * �ж�ֱ�ӵ�offsetIndex�Ƿ����Ҫ�󣨼�λ����ȫ��Ӧ������   ����������
		 */
		Element child = getChild(tagChilds,offsetIndex, tagName);
		if (child!=null) {
			//����ÿһ�� ԭ����Ҫ��ؼ��ֱ���ȫ��ƥ��
			//���ڸû�Ҫ��ֻҪ��֤ƥ��������һ���ؼ���
			boolean match = false;
			for (String key:su.keys) {
//				debug(" key:"+key);
				if (child.text().contains(key)) {
					match =  true;
					break;
				}
			}
			//�������Ҫ�� ���һ�� �ж���һ���Ƿ����Ҫ��
			if ((su.keys.isEmpty() || match) && getPoint2(child, rule, index+1, path+" "+tagName+"-"+offsetIndex+" ", name, feature)) {
				return true;
			}
		}
//		debug("deep "+index+" "+tagName+"-"+offsetIndex);
		/**
		 * ������׼��λ���ϲ�������Ҫ�� ����ͬһ�㼶�ϱ������ҵ�����Ҫ������ݣ���λ�ó����˴�λ �ձ������
		 */
		for (int i= 0;i<tagChilds.size(); i++) {
			if (i == offsetIndex) continue;
			Element ele = getChild(tagChilds,i, tagName);
			if (ele==null) break;
			boolean match = false;
			for (String key:su.keys) {
				if (ele.text().contains(key)) {
					match =  true;
					break;
				}
			}
			if ((su.keys.isEmpty() || match) && getPoint2(ele,rule,index+1, path+" "+tagName+"-"+i+" ", name, feature)) {
				return true;
			}
		}
		return false;
	}
	//���ĳ���ڵ������i��tag���͵��ӽڵ�
	private Element getChild(Elements children, int i, String tagName) {
		int index = 0;
		for (Element e:children) {
			if (tagName.compareTo(e.tagName())==0) {
				if (i==index) {
					return e;
				} else {
					index ++;
				}
			}
		}
		return null;
	}
	
//	public void log(String str) {
//		
//		if (logAddr == null) {
//			logAddr =  "E:/ProgramData/WorkSpaceFor3.7.1/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/KWL/log"; 
//		}
//		if (log == null) {
//			try {
//				log = new BufferedWriter(
//						new OutputStreamWriter(
//								new FileOutputStream(new File(logAddr),true),charset));
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		date = new Date();
//		if (dateformat == null) {
//			dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
//		}
//		try {
//			log.write(dateformat.format(date)+" "+str+"\n\r");
//			log.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}
/**
 *  Ŀǰ�ķ�������ȷ�������Ĳ�Σ�����ͨ��tag��key���������������ٷ����Ǽ�¼tag��˳��������пɽ�ʡʱ��
 */
//contentUnit�ṹ����Ҫ��������¼ÿһ�����ݺͶ�Ӧ�ı�������Ĺؼ���
class ContentUnitC {
	public String name;//��Ӧ������
	public String content;
	public Vector<String> keys;
	public ContentUnitC() {
		keys = new Vector<String>();
	}
}



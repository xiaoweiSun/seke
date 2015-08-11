package com.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ServletPackage.Config;
import ServletPackage.DataBaseMan;
import ServletPackage.MyResultSet;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.VCARD;
import com.structure.rule.Feature;
import com.structure.rule.Record;
import com.structure.task.Task;
import com.structure.task.TaskPattern;

public class TaskProcedure implements Runnable{
	public Task task;
	//标记系统的相对路径名(存放下载的数据的地址）
	public String destTDBPath ;
	public String tdbName ;
	public String fetchFilesDir;
	public static String uniqueURI = "http://www.analyze.org/test/";
	public static String ns = uniqueURI+"#";
	public static boolean DEBUG = true;
	public static boolean LOG = true;
	public BufferedWriter log = null;
	public Date date = null;
	public SimpleDateFormat dateformat= null;
	public String logAddr = null;
	public String charset = "utf-8";
	Pattern p;
	public DataBaseMan dbMan;
	public void debug(String str) {
		if (DEBUG) {
			System.out.println(TaskProcedure.class.getName()+" "+str);
		}
	}
	public void log(String str) {
		if (!LOG) return;
		if (logAddr == null) {
			logAddr = this.destTDBPath + "../log/log"; 
		}
		if (log == null) {
			try {
				log = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(new File(logAddr),true),charset));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		date = new Date();
		if (dateformat == null) {
			dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		}
		try {
			log.write(dateformat.format(date)+" "+str+"\n\r");
			log.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public TaskProcedure (Task task, String realpath, DataBaseMan dbMan) {
		p = Pattern.compile("^(http|www|ftp)"); 
		this.task = task;
		this.tdbName = this.task.ontology;
		this.destTDBPath = realpath + Config.datasetPath;
		this.destTDBPath = this.destTDBPath.replaceAll("\\\\", "/").replaceAll("//", "/");
		this.dbMan = dbMan;
		this.fetchFilesDir = realpath + Config.fetchFilesPath;
		this.fetchFilesDir = this.fetchFilesDir.replaceAll("\\\\", "/").replaceAll("//", "/");
		debug(this.tdbName);
		debug(this.destTDBPath);
	}
	
	public String getTdbName() {
		return this.tdbName;
	}
	public void run() {
		this.procedure();
	}
	
	public String procedure() {
		//执行前更新数据库信息
		updateDataBase(tdbName, Config.tdbStateProcess);
		/**
		 * 1/首先  从task中间提取出全部需要爬取的信息（www.baidu.com格式）
		 * ------------------start-1--------------------
		 */
		Vector<TaskPattern> urlRelative = new Vector<TaskPattern>();
		//需要解析爬取网页的模式，需要提取对应的url并进行爬取
		Vector<TaskPattern> conceptRelative = new Vector<TaskPattern>();
		//需要进行二次解析的模式，需要提取对应的概念并进行分析
		ArrayList<String> urllist = new ArrayList<String>();
		for (TaskPattern pattern:task.patternList) {
			Matcher m = p.matcher(pattern.domain);
			debug(pattern.domain);
			if (m.find()) {
				//如果pattern的对应来源是链接格式
				urllist.add(pattern.domain);
				debug(""+pattern.domain);
				urlRelative.add(pattern);
			} else {
				conceptRelative.add(pattern);
			}
		}
		/**
		 * =======================  end-1   =====================
		 */
		
		/**
		 *  2、这里计算完了需要爬取的urlList 和 对应的 path   调用爬取方法
		 *  -----------------start-2-----------------------
		 */
		//将文件爬取到source目录下对应的url的目录之下
		// ->url->files;
		WebCrawler.crawling(urllist, this.fetchFilesDir,1000);

		/**
		 * ===================end-2===========================
		 */
		
		/**
		 * 3、下面调出所有解析html文件的webpattern来解析并存储结果于数据库中，默认全部的文件已经存储到对应的url目录下了
		 * --------------------start-3---------------------
		 */
		WebPatternParse wp = new WebPatternParse();
		Vector<Record> v = new Vector<Record>();
		//创建tdb存储库（获取）
		String fileDir;
		Model model, tempmodel;
		Resource resource;
		Property p;
		debug(destTDBPath);
		Dataset dataset = TDBFactory.createDataset(destTDBPath + "/" + tdbName);
		dataset.begin(ReadWrite.WRITE);
		try {
			for (TaskPattern pattern: urlRelative) {
				v.clear();
				//对于每一个需要解析html的webpattern 对应的文件名存放在url对应的目录下
				fileDir = this.fetchFilesDir+ WebCrawler.getFileNameByUrl(pattern.domain) ;
				wp.processRuleString(fileDir, pattern.description, v);
				//得到对应的数据后，建立tdb并且存入v数组记录的所有model相关信息
				for (Record record: v) {
					model = ModelFactory.createDefaultModel();
					//通过记录不同的下载位置
					model.setNsPrefix("test", TaskProcedure.uniqueURI);
					resource = model.createResource(TaskProcedure.uniqueURI+record.uri);
					resource.addProperty(VCARD.NAME, record.uri);
					for (Feature feature:record.features) {
						// add the property
						p = model.createProperty(TaskProcedure.uniqueURI+feature.key);
						resource.addProperty(p, feature.value, XSDDatatype.XSDstring);
					}
					tempmodel = dataset.getDefaultModel();
					tempmodel.add(model);
				}
				
			}
			dataset.commit();
		} finally {
			dataset.end();
		}
//		listDataset(dataset);
		/**
		 * ======================= end-3 =========================
		 */

		/**
		 * 4、下面调配所有的剩余pattern 解析位于数据库中的结果
		 * -------------------------start-4-------------------
		 */
		for (TaskPattern pattern: conceptRelative) {
			//对于每一个需要从数据库中解析数据的pattern
			ArrayList<String> packages = new ArrayList<String>();
			String[] pack = pattern.domain.split("&");
			for (String t:pack) {
				packages.add(TaskProcedure.uniqueURI+t);
				debug(TaskProcedure.uniqueURI+t);
			}
			ArrayList<String> concepts = new ArrayList<String>();
			String[] con = pattern.objList.split("&");
			for (String t:con) {
				concepts.add(TaskProcedure.uniqueURI+t);
				debug(TaskProcedure.uniqueURI+t);
			}
			//将domain 和  objlist的内容都解析成带有uri标记的
			LanguagePatternParse.execQuery(dataset, packages, concepts, pattern.description);
		}
		
		updateDataBase(tdbName, Config.tdbStateComplete);
		/**
		 * =========================== end-4 =======================
		 */
//		listDataset(dataset);
		return destTDBPath+"/"+tdbName;
	}
	
	//完成任务后更新数据库
	private void updateDataBase(String fieldName, String state) {
		MyResultSet mrs = dbMan.SelectCond(Config.field_table, " fieldName = '"+fieldName+ "'");
		if (mrs.first()) {
			//如果确实已经存在这个领域库  更新状态为已完成
			String sql = "update "+Config.field_table+" set isFinished = '"+state+"' ";
			sql += " where fieldName = '"+fieldName+"'";
			dbMan.SetStmt(sql);
			dbMan.ExecUpdate();
		} else {
			//如果不存在  则创造一个
			String eles[] = new String[]{"username","tag", "description", "fieldName", "isFinished"};
			String values[] = new String[]{"'machine'","'machine auto generated'","'machine auto generated'","'"+fieldName+"'","'"+state+"'"};
			dbMan.InsertRow(Config.field_table, eles, values);
		}
	}
	
	private void listDataset(Dataset dataset) {
//		debug 专区  用于展示前面一部分所生成的rdf的文件结构等信息
		Iterator<String> it = dataset.listNames();
		Model model;
		String modelName;
		while (it.hasNext()) {
			modelName = it.next();
			model = dataset.getNamedModel(modelName);
			log(modelName);
			StmtIterator sit = model.listStatements();
			while (sit.hasNext()) {
				Statement s = sit.nextStatement();
				String space = "";
				for (int t=0; t< 10 - s.getPredicate().toString().length(); t++) {
					space += "  ";
				}
				log(s.getSubject().toString() + " " +
									s.getPredicate().toString() + " " +
									space +
									s.getObject().toString());
			}
		}
	}
}

//工作告一段落，下一步是要将我的webpattern和抓取的部分的例子跑通，重点问题是在文件路径的设置上，一定要正确衔接上
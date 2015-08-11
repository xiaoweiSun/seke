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
	//���ϵͳ�����·����(������ص����ݵĵ�ַ��
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
		//ִ��ǰ�������ݿ���Ϣ
		updateDataBase(tdbName, Config.tdbStateProcess);
		/**
		 * 1/����  ��task�м���ȡ��ȫ����Ҫ��ȡ����Ϣ��www.baidu.com��ʽ��
		 * ------------------start-1--------------------
		 */
		Vector<TaskPattern> urlRelative = new Vector<TaskPattern>();
		//��Ҫ������ȡ��ҳ��ģʽ����Ҫ��ȡ��Ӧ��url��������ȡ
		Vector<TaskPattern> conceptRelative = new Vector<TaskPattern>();
		//��Ҫ���ж��ν�����ģʽ����Ҫ��ȡ��Ӧ�ĸ�����з���
		ArrayList<String> urllist = new ArrayList<String>();
		for (TaskPattern pattern:task.patternList) {
			Matcher m = p.matcher(pattern.domain);
			debug(pattern.domain);
			if (m.find()) {
				//���pattern�Ķ�Ӧ��Դ�����Ӹ�ʽ
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
		 *  2���������������Ҫ��ȡ��urlList �� ��Ӧ�� path   ������ȡ����
		 *  -----------------start-2-----------------------
		 */
		//���ļ���ȡ��sourceĿ¼�¶�Ӧ��url��Ŀ¼֮��
		// ->url->files;
		WebCrawler.crawling(urllist, this.fetchFilesDir,1000);

		/**
		 * ===================end-2===========================
		 */
		
		/**
		 * 3������������н���html�ļ���webpattern���������洢��������ݿ��У�Ĭ��ȫ�����ļ��Ѿ��洢����Ӧ��urlĿ¼����
		 * --------------------start-3---------------------
		 */
		WebPatternParse wp = new WebPatternParse();
		Vector<Record> v = new Vector<Record>();
		//����tdb�洢�⣨��ȡ��
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
				//����ÿһ����Ҫ����html��webpattern ��Ӧ���ļ��������url��Ӧ��Ŀ¼��
				fileDir = this.fetchFilesDir+ WebCrawler.getFileNameByUrl(pattern.domain) ;
				wp.processRuleString(fileDir, pattern.description, v);
				//�õ���Ӧ�����ݺ󣬽���tdb���Ҵ���v�����¼������model�����Ϣ
				for (Record record: v) {
					model = ModelFactory.createDefaultModel();
					//ͨ����¼��ͬ������λ��
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
		 * 4������������е�ʣ��pattern ����λ�����ݿ��еĽ��
		 * -------------------------start-4-------------------
		 */
		for (TaskPattern pattern: conceptRelative) {
			//����ÿһ����Ҫ�����ݿ��н������ݵ�pattern
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
			//��domain ��  objlist�����ݶ������ɴ���uri��ǵ�
			LanguagePatternParse.execQuery(dataset, packages, concepts, pattern.description);
		}
		
		updateDataBase(tdbName, Config.tdbStateComplete);
		/**
		 * =========================== end-4 =======================
		 */
//		listDataset(dataset);
		return destTDBPath+"/"+tdbName;
	}
	
	//��������������ݿ�
	private void updateDataBase(String fieldName, String state) {
		MyResultSet mrs = dbMan.SelectCond(Config.field_table, " fieldName = '"+fieldName+ "'");
		if (mrs.first()) {
			//���ȷʵ�Ѿ�������������  ����״̬Ϊ�����
			String sql = "update "+Config.field_table+" set isFinished = '"+state+"' ";
			sql += " where fieldName = '"+fieldName+"'";
			dbMan.SetStmt(sql);
			dbMan.ExecUpdate();
		} else {
			//���������  ����һ��
			String eles[] = new String[]{"username","tag", "description", "fieldName", "isFinished"};
			String values[] = new String[]{"'machine'","'machine auto generated'","'machine auto generated'","'"+fieldName+"'","'"+state+"'"};
			dbMan.InsertRow(Config.field_table, eles, values);
		}
	}
	
	private void listDataset(Dataset dataset) {
//		debug ר��  ����չʾǰ��һ���������ɵ�rdf���ļ��ṹ����Ϣ
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

//������һ���䣬��һ����Ҫ���ҵ�webpattern��ץȡ�Ĳ��ֵ�������ͨ���ص����������ļ�·���������ϣ�һ��Ҫ��ȷ�ν���
package com.index_search;

import java.io.BufferedReader;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;  
import java.io.InputStreamReader;
import java.util.ArrayList;  
import java.util.Date;
import java.util.Iterator;
import java.util.List; 

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.standard.StandardAnalyzer;  
import org.apache.lucene.document.Document;  
import org.apache.lucene.document.Field;  
import org.apache.lucene.index.CorruptIndexException;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.IndexWriter;  
import org.apache.lucene.index.IndexWriterConfig;  
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;


import ServletPackage.Config;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class IndexFile {
	private  File INDEX_DIR;
	private  Analyzer analyzer ;
	
	public IndexFile(){
		analyzer = new StandardAnalyzer(Version.LUCENE_35);
	}
	public void debug(String str) {
		System.out.println("IndexFile.java "+str);
	}
	/**
	 * �������Ϣ��¼��Ŀ���ļ��ڵ������ص��ļ����Ͻ��������Ľ���
	 * @param realpath
	 * @param desfile
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public void IndexFromInternet(String realpath, String desfile) throws CorruptIndexException, LockObtainFailedException, IOException{
		//ȷ��Ŀ¼���ҽ��������ļ� ȷ������ģʽ
		INDEX_DIR=new File(realpath+"/owl/index");
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		System.out.println("indexing...");
		System.out.println(desfile);
		//��ȡ�ⲿ�ļ� �������ʵ�����
		IndexWriter iw = new IndexWriter(FSDirectory.open(INDEX_DIR),iwc);
		File tempfile = new File(desfile);
		ArrayList<String[]> files = new ArrayList<String[]>();
		BufferedReader bbr = new BufferedReader(
								new InputStreamReader(
									new FileInputStream(tempfile)));
		String lline = "";
		while((lline = bbr.readLine())!=null){
			String[] metadata = new String[2];
			metadata[0] = lline.substring(0,lline.indexOf(" "));
			metadata[1] = lline.substring(lline.indexOf(" ")+1);
			files.add(metadata);
		}
		bbr.close();
		long starttime = new Date().getTime();
		
		for(int i=0;i<files.size();i++){
			debug("file"+i+"process");
			String fullname = files.get(i)[1];
			String filename = fullname.substring(fullname.lastIndexOf("/")+1);
			fullname = realpath+"owl/online/"+filename;
			String fileurl = files.get(i)[0];
			debug("filename:"+fullname);
			File file = new File(fullname);
			if(file.exists()&&fullname.endsWith(".owl")){//�������ļ�����
				debug("filename:"+fullname);
				BufferedReader br 
				= new BufferedReader(
						new InputStreamReader( 
								new FileInputStream(file)));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while( (line = br.readLine())!=null){
					sb.append(line);
				}
//				System.out.println("test.java function:index ["+sb.toString()+" ]");
				Document doc = new Document();
				
				Field fileName = new Field("filename",filename,
						Field.Store.YES, Field.Index.NOT_ANALYZED);
				Field url = new Field("url",fileurl,
						Field.Store.YES, Field.Index.NO);
				Field fileBody = new Field("filebody",sb.toString()+"��"+fullname+"��"+url+"��"+filename.substring(0,filename.indexOf(".")),
						Field.Store.NO,Field.Index.ANALYZED);
				//���ÿ���ļ�����Դ 
				Field position = new Field("pos","online",
						Field.Store.YES, Field.Index.NOT_ANALYZED);
				doc.add(fileName);
				doc.add(url);
				doc.add(fileBody);
				doc.add(position);
				iw.addDocument(doc);
			}
		}
		iw.close();
		long endtime = new Date().getTime();
		debug("s:"+starttime+" e:"+endtime+" t:"+(endtime-starttime));
		
	}
	public void IndexFromLocal(String virtualpath, String realpath, String[] files, String config) throws CorruptIndexException, LockObtainFailedException, IOException{
		virtualpath = Config.repositoryURL;
		//�����ж�
		if (config == null) {
			config = "";
		}
		//ȷ��Ŀ¼���ҽ��������ļ� ȷ������ģʽ
		INDEX_DIR=new File(realpath+"/owl/index");
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		debug("indexing...");
		//��ȡ�ⲿ�ļ� �������ʵ�����
		IndexWriter iw = new IndexWriter(FSDirectory.open(INDEX_DIR),iwc);
		long starttime = new Date().getTime();
		
		for(int i=0;i<files.length;i++){
			String fullname = files[i];
			debug(fullname);
			String fileurl = virtualpath+"/"+fullname.substring(fullname.lastIndexOf("/")+1);
			File file = new File(fullname);
			if(file.exists()&&fullname.endsWith(".owl")){//�������ļ�����
				debug(fullname);
				BufferedReader br 
				= new BufferedReader(
						new InputStreamReader( 
								new FileInputStream(file)));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while( (line = br.readLine())!=null){
					sb.append(line);
				}
//				System.out.println("test.java function:index ["+sb.toString()+" ]");
				Document doc = new Document();
				String filename = fullname.substring(fullname.lastIndexOf("/")+1);
				Field fileName = new Field("filename",filename,
						Field.Store.YES,Field.Index.NOT_ANALYZED);
				Field url = new Field("url",fileurl,
						Field.Store.YES,Field.Index.NO);
				Field fileBody = new Field("filebody",sb.toString()+"��"+fullname+"��"+url+"��"+filename.substring(0,filename.lastIndexOf("."))+" "+config,
						Field.Store.NO,Field.Index.ANALYZED);
				//���ÿ���ļ�����Դ 
				Field position = new Field("pos","local",
						Field.Store.YES, Field.Index.NOT_ANALYZED);
			
				doc.add(fileName);
				doc.add(url);
				doc.add(fileBody);
				doc.add(position);
				iw.addDocument(doc);
			}
		}
		iw.close();
		long endtime = new Date().getTime();
		debug("s:"+starttime+" e:"+endtime+" t:"+(endtime-starttime));
		
	}
//	public void addFieldByJena(Document doc,File file) throws FileNotFoundException{
//		OntModel m = ModelFactory.createOntologyModel();
//		
//		StringBuffer c = new StringBuffer();
//		StringBuffer r = new StringBuffer();
//		try{
//			m.read(new FileInputStream(file),"");
//			
//			if(null != m.validate()){System.out.print("validate");}
//			for(Iterator<OntClass> cit = m.listClasses();cit.hasNext();){
//				OntClass oc = cit.next();
//				if(!oc.isAnon()){//ֻҪ����������
//					c.append(" "+oc.getLocalName());
//				}
//			}
//			for(Iterator<OntProperty> rit = m.listAllOntProperties();rit.hasNext();){
//				//��ȡ����
//				OntProperty op = rit.next();
//				if(!op.isAnon()){//������������
//					r.append(" "+op.getLocalName());
//				}
//			}
//			m.close();
//		}catch(Exception e){
//			e.printStackTrace();
//			System.out.println(file.getName());
//		}
//		Field con = new Field("concept",c.toString(),
//				Field.Store.NO,Field.Index.ANALYZED);
//		Field pro = new Field("property",r.toString(),
//				Field.Store.NO,Field.Index.ANALYZED);
//		doc.add(con);
//		doc.add(pro);
//		
//		
//	}
}

package com.index_search;
//package SearchPackage;
//
//import java.io.BufferedReader;
//import java.io.File;  
//import java.io.FileInputStream;
//import java.io.IOException;  
//import java.io.InputStreamReader;
//import java.util.ArrayList;  
//import java.util.Date;
//import java.util.List;  
//
//  
//import org.apache.lucene.analysis.Analyzer;  
//import org.apache.lucene.analysis.standard.StandardAnalyzer;  
//import org.apache.lucene.document.Document;  
//import org.apache.lucene.document.Field;  
//import org.apache.lucene.index.CorruptIndexException;  
//import org.apache.lucene.index.IndexReader;  
//import org.apache.lucene.index.IndexWriter;  
//import org.apache.lucene.index.IndexWriterConfig;  
//import org.apache.lucene.index.IndexWriterConfig.OpenMode;  
//import org.apache.lucene.queryParser.ParseException;  
//import org.apache.lucene.queryParser.QueryParser;  
//import org.apache.lucene.search.IndexSearcher;  
//import org.apache.lucene.search.Query;  
//import org.apache.lucene.search.ScoreDoc;  
//import org.apache.lucene.search.TopDocs;  
//import org.apache.lucene.store.FSDirectory;  
//import org.apache.lucene.util.Version;  
//
//public class SearchEngineer {
//	private  File INDEX_DIR;
//	private  File FILE_DIR ;
//	private  Analyzer analyzer ;
//	
//	public SearchEngineer(String realpath){
//		INDEX_DIR=new File(realpath+"/index");
//		FILE_DIR = new File(realpath+"/wsdl");
//		analyzer = new StandardAnalyzer(Version.LUCENE_35);
//	}
//	/**
//	 * 首先建立索引
//	 * @throws IOException 
//	 */
//	public void index() throws IOException{
//		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,analyzer);
//		iwc.setOpenMode(OpenMode.CREATE);
//		System.out.println("1");
//		IndexWriter iw = new IndexWriter(FSDirectory.open(INDEX_DIR),iwc);
//		File[] wsdlfiles = FILE_DIR.listFiles();
//		long starttime = new Date().getTime();
//		
//		for(int i=0;i<wsdlfiles.length;i++){
//			if(wsdlfiles[i].isFile() && wsdlfiles[i].getName().endsWith(".wsdl")){
//				BufferedReader br 
//				= new BufferedReader(
//						new InputStreamReader( 
//								new FileInputStream(wsdlfiles[i])));
//				StringBuilder sb = new StringBuilder();
//				String line = "";
//				while( (line = br.readLine())!=null){
//					sb.append(line);
//				}
////				System.out.println("test.java function:index ["+sb.toString()+" ]");
//				Document doc = new Document();
//				Field fileName = new Field("filename",wsdlfiles[i].getName(),
//						Field.Store.YES,Field.Index.ANALYZED);
//				Field fileBody = new Field("filebody",sb.toString(),
//						Field.Store.NO,Field.Index.ANALYZED);
//				doc.add(fileName);
//				doc.add(fileBody);
//				iw.addDocument(doc);
//			}
//		}
//		iw.close();
//		long endtime = new Date().getTime();
//		System.out.println("s:"+starttime+" e:"+endtime+" t:"+(endtime-starttime));
//		
//	}
//	
//	/**
//	 * 查询
//	 * @throws IOException 
//	 * @throws CorruptIndexException 
//	 * @throws ParseException 
//	 */
//	public void search(ArrayList<String> resultList,String field,String value,int type) throws CorruptIndexException, IOException, ParseException{
//		IndexReader ir = IndexReader.open(FSDirectory.open(INDEX_DIR));
//		IndexSearcher is = new IndexSearcher(ir);
//		QueryParser qp = new QueryParser(Version.LUCENE_35,field,analyzer);
//		Query q = qp.parse(value);
//		TopDocs td = is.search(q,10);
//		int total = td.totalHits;
//		ScoreDoc[] sd = td.scoreDocs;
//		for(int i=0;i<sd.length;i++){
//			Document doc = is.doc(sd[i].doc);
//			System.out.println("resultfile "+i+": "+doc.get("filename"));
//			resultList.add(doc.get("filename"));		
//		}
//		is.close();
//	}
//
//}
//

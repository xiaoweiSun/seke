package com.index_search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFile {
	private  File INDEX_DIR;
	private  Analyzer analyzer ;
	private  String realpath;
	private  String value;
	
	public SearchFile(String realpath){
		this.realpath = realpath;
		INDEX_DIR=new File(realpath+"/owl/index");
		analyzer = new StandardAnalyzer(Version.LUCENE_35);
	}
	public void search(ArrayList<String[]> resultList,String field,String value,int type) throws CorruptIndexException, IOException, ParseException{
		this.value = value;
		IndexReader ir = IndexReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher is = new IndexSearcher(ir);
		//创建查询语句
		QueryParser qp = new QueryParser(Version.LUCENE_35,field,analyzer);
		Query q = qp.parse(value);
		//创建排序语句
		 
        SortField sortField = new SortField("pos", SortField.STRING); 
        Sort sort = new Sort(sortField); 
        //执行搜索
		TopDocs td = is.search(q,1000,sort);
		int total = td.totalHits;
		
		ScoreDoc[] sd = td.scoreDocs;
		for(int i=0;i<sd.length;i++){
			Document doc = is.doc(sd[i].doc);
			System.out.println("resultfile "+i+": "+doc.get("filename"));
			String metadata[] = new String[4];
			metadata[0] = doc.get("url");
			metadata[1] = doc.get("filename");
			metadata[3] = doc.get("pos");
			getRelativeContent(metadata);
			resultList.add(metadata);		
		}
		is.close();
	}
	public void getRelativeContent(String[] metadata){
		String filename = metadata[1];
		String filepath;
		if(metadata[3].compareTo("online")==0){
			filepath = realpath+"/owl/online/"+filename;
		}else{
			filepath = realpath+"/owl/local/"+filename;			
		}
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(
									new File(filepath))));
			String temp;
			StringBuilder sb = new StringBuilder();
			while((temp=br.readLine())!=null){
				sb.append(temp);
			}
			String content = sb.toString();
			temp = content;
			ArrayList<Integer> place = new ArrayList<Integer>();
			int t;
			t = temp.indexOf(value);//找到关键字
			while(t!=-1){
				place.add(new Integer(t));
				temp = temp.substring(t+value.length()+1);
				t = temp.indexOf(value);
			}//获取全部关键字次数，并且获得相应位置
			//接下来截取第一个字段的相关标签
			int size = 200;
			if(place.size()==0)return;
			int start = 0;
			if(start<place.get(0)-size){
				start = place.get(0)-size;
			}
			int end = content.length()-1;
			if(end>place.get(0)+size){
				end = place.get(0)+size;
			}
			temp = content.substring(start,end+1);
//			start = temp.indexOf("<");
//			end = temp.lastIndexOf(">");
//			
//			if(start==-1){
//			}else{
//				temp = temp.substring(start,end+1);
//			}
			metadata[2] = temp;
//			System.out.println(metadata[2]);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

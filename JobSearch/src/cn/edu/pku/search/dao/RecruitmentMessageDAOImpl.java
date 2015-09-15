package cn.edu.pku.search.dao;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Repository;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

import cn.edu.pku.search.domain.Pager;
import cn.edu.pku.search.domain.RecruitmentMessage;
import cn.edu.pku.search.domain.SystemContext;
import cn.edu.pku.util.KeySet;

@Repository
public class RecruitmentMessageDAOImpl implements RecruitmentMessageDAO{

	@Override
	public Pager<RecruitmentMessage> search(String field, String queryString,int offset) {
		String index = "/home/shawn/workspace/eclipse/InvertIndex/index";

		Pager<RecruitmentMessage> res = null; 
		
		try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(index)))){
			
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new SimpleAnalyzer();
	
			QueryParser parser = new QueryParser(field, analyzer);
	
			String line = queryString;
			
			line = line.trim();
	
			Query query = parser.parse(line);
			System.out.println("Searching for: " + query.toString(field));
	
			res = doPagingSearch(searcher, query, offset);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Pager<RecruitmentMessage> search(String[] field, String[] queryString,int offset) {
		String index = "/home/shawn/workspace/eclipse/InvertIndex/index";

		Pager<RecruitmentMessage> res = null; 
		
		try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(index)))){
			
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new SimpleAnalyzer();
	
			BooleanClause.Occur[] clauses = new BooleanClause.Occur[field.length];
			Arrays.fill(clauses, BooleanClause.Occur.MUST);

			Query query = MultiFieldQueryParser.parse(queryString, field,
					clauses, analyzer);

			res = doPagingSearch(searcher, query, offset);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	private Pager<RecruitmentMessage> doPagingSearch(IndexSearcher searcher,
			Query query, int offset)
			throws IOException {

		Pager<RecruitmentMessage> res = new Pager<>();
		List<RecruitmentMessage> list = new ArrayList<>();
		int hitsPerPage = SystemContext.getSize();

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int end = Math.min(numTotalHits, offset + hitsPerPage);

		if (end > hits.length) {
			// the 5 pages to show is not enough
			hits = searcher.search(query, numTotalHits).scoreDocs;
		}

		end = Math.min(hits.length, offset + hitsPerPage);

		for (int i = offset; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);
			String url = doc.get("url");
			RecruitmentMessage msg = new RecruitmentMessage();
			msg.setUrl(url);
			msg.setJobName(doc.get(KeySet.jobName));
			msg.setJobDescription(doc.get(KeySet.jobDescription));
			list.add(msg);
		}
		res.setDatas(list);
		res.setTotal(numTotalHits);
		res.setOffset(offset);
		res.setSize(hitsPerPage);
		return res;
	}
	
}

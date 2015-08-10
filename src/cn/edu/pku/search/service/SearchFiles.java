package cn.edu.pku.search.service;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

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

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

/** Simple command-line based search demo. */
public class SearchFiles {
	
	private int numTotalHits;

	public SearchFiles() {
	}

	/** Simple command-line based search demo. */
	public List<Map<String,String>> doSearch(String field, String queryString){
		

		String index = "D:\\Workspaces\\Eclipse\\InvertIndex\\index";
		boolean raw = false;
		int hitsPerPage = 10;

		List<Map<String,String>> res = null; 
		
		try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(index)))){
			
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new SimpleAnalyzer();
	
			BufferedReader in = null;
	
			in = new BufferedReader(new InputStreamReader(System.in,
					StandardCharsets.UTF_8));
	
			QueryParser parser = new QueryParser(field, analyzer);
	
			String line = queryString;
			
			line = line.trim();
	
			Query query = parser.parse(line);
			System.out.println("Searching for: " + query.toString(field));
	
	
			res = doPagingSearch(in, searcher, query, hitsPerPage, 0, raw,
					queryString == null);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public List<Map<String,String>> doSearch(String[] field, String[] queryString){
		

		String index = "D:\\Workspaces\\Eclipse\\InvertIndex\\index";
		boolean raw = false;
		int hitsPerPage = 10;

		List<Map<String,String>> res = null; 
		
		try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(index)))){
			
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new SimpleAnalyzer();
	
			BufferedReader in = null;
	
			in = new BufferedReader(new InputStreamReader(System.in,
					StandardCharsets.UTF_8));
			BooleanClause.Occur[] clauses = new BooleanClause.Occur[field.length];
			Arrays.fill(clauses, BooleanClause.Occur.MUST);

			Query query = MultiFieldQueryParser.parse(queryString, field,
					clauses, analyzer);

			res = doPagingSearch(in, searcher, query, hitsPerPage, 0, raw,
					queryString == null);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
	private List<Map<String,String>> doPagingSearch(BufferedReader in,
			IndexSearcher searcher, Query query, int hitsPerPage, int start, boolean raw,
			boolean interactive) throws IOException {

		List<Map<String,String>> res = new ArrayList<>();
		
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int end = Math.min(numTotalHits, start + hitsPerPage);
		
		while (true) {
			if (end > hits.length) {
				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {
				if (raw) { // output raw format
					System.out.println("doc=" + hits[i].doc + " score="
							+ hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				String content = doc.get("content");
				if (content != null) {
					JSONObject jsonObject = JSONObject.fromObject(content);
					Map<String,String> map = (Map)jsonObject;
					res.add(map);
					System.out.println((i + 1) + ". " + content);
					//res.add();
					String title = doc.get("title");
					if (title != null) {
						System.out.println("   Title: " + doc.get("title"));
					}
				} else {
					System.out.println((i + 1) + ". "
							+ "No path for this document");
				}

			}

			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out
							.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0) == 'q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start += hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit)
					break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
		return res;
	}
	
	private List<String> getContent(String path) {
		List<String> res = new ArrayList<>();
		File file = new File(path);
		res.add(path.substring(path.lastIndexOf("\\"),path.indexOf(".txt")));
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	res.add(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
	}
	
	public int getNumTotalHits() {
		return numTotalHits;
	}

}

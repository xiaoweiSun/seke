/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ServletPackage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.net.ServerResponse;

/** Example of a READ transaction. */
public class ExecQuery {
//	public static void main(String... argv) {
//		String directory = "E:/ProgramData/WorkSpaceFor3.7.1/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/KWL/FetchFiles/dataset";
//
//		String sparqlQueryString = "SELECT * { ?s ?p ?o }";
//		execQuery(sparqlQueryString, directory);
//
//	}

	public static void queryTDB(String realpath, MainServlet servlet ,DataBaseMan dbMan, 
			HttpServletRequest req, HttpServletResponse resp) {
		String tdbname = req.getParameter("tdbname");
		String query = req.getParameter("sparqlIn");
		String tdbpath = realpath+Config.datasetPath+tdbname;
		tdbpath = tdbpath.replaceAll("\\\\","/").replaceAll("//", "/");
		
		ServerResponse.writeJsonResult(resp,execQuery(query, tdbpath));
	}
	public static ArrayList<ArrayList<String>> execQuery(String sparqlQueryString, String path) {

		Dataset dataset = TDBFactory.createDataset(path);
		dataset.begin(ReadWrite.READ);

		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		try {
			ResultSet results = qexec.execSelect();
			//ResultSetFormatter.out(results) ;
			List<String> vars = results.getResultVars();
			System.out.println(vars);
			for (; results.hasNext();) 
			{
				ArrayList<String> cur = new ArrayList<String>();
				QuerySolution soln = results.nextSolution();
				for(String var : vars) {
					//System.out.println(pro);
					RDFNode node = soln.get(var);
					if(node.isResource())
						cur.add(node.asResource().getLocalName());
						//System.out.println(node.asResource().getLocalName());
					if(node.isLiteral())
						cur.add(node.asLiteral().getValue().toString());
						//System.out.println(node.asLiteral().getValue());
					//System.out.print(soln.get(var) + "\t\t");
				}
				ret.add(cur);
				// int count = soln.getLiteral("count").getInt() ;
				// System.out.println("count = "+count) ;
			}
		} finally {
			qexec.close();
			dataset.end();
		}
		dataset.close();
		System.out.println(ret);
		return ret;
	}

}

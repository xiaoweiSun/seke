package ServletPackage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.net.ServerResponse;

public class Sparql {

	public static void debug(String str) {
		System.out.println("Sparql.java "+str);
	}
	public static void queryRDF(String realpath, MainServlet servlet ,DataBaseMan dbMan, 
			HttpServletRequest request, HttpServletResponse response) {
		String sparql = request.getParameter("sparqlIn");
        String querytype = request.getParameter("querytype");
        String filename = request.getParameter("filename");
        String dirtype = request.getParameter("dirtype");
        //String rstype = request.getParameter("resulttype");
       
        //这里应该是遍历owl文件，检索得都有的概念
        OntModel m = ModelFactory.createOntologyModel();
        String path;
        if ("local".equals(dirtype)) {
        	path = realpath + Config.uploadOwlPath + filename;
        } else {
        	path = realpath + Config.onlineOwlPath + filename;
        }
        path = path.replaceAll("\\\\", "/").replaceAll("//", "/");
        //获取要检索的本体文件
        debug(path);
        m.read("file:"+path);
        Query query;
        QueryExecution qe;
                   
		   
		if( !sparql.isEmpty() && !querytype.isEmpty() ){
			query = QueryFactory.create(sparql);
			qe = QueryExecutionFactory.create(query,m);
			
			try{
				if(querytype.equals("select")){
					debug("--in select--");
					ResultSet rs = qe.execSelect();		
					
					String r = ResultSetFormatter.asText(rs);
					ServerResponse sr = new ServerResponse("0", r);
					ServerResponse.writeJsonResult(response, sr);
//				}else if(querytype.equals("construct") || querytype.equals("describe")){
//					
//					String rspath = "E:/AppProg/Java/Tomcat 5.0/webapps/KwLibrary/temp.ttl"; 
//					File rsfile = new File(rspath);
//					FileOutputStream rsout = new FileOutputStream(rsfile);
//					
//					if(querytype.equals("construct")){
//						debug("--in construct--");
//						Model rs = qe.execConstruct();
//						rs.write(rsout,"Turtle");
//						
//					}else if(querytype.equals("describe")){
//						debug("--in describe--");
//						Model rs = qe.execDescribe();
//						rs.write(rsout,"Turtle");
//					}								
//					
//					StringBuffer sbuf = new StringBuffer();
//					BufferedReader reader = new BufferedReader(new FileReader(rsfile));
//					String s = null;
//					int line = 1;
//					while((s = reader.readLine()) != null){
//						sbuf = sbuf.append(s + "\n");
//						line++;
//					}
//					String r = sbuf.toString();
//					ServerResponse.writeResult(response, r);
//					reader.close();	
				}else if(querytype.equals("ask")){
					debug("--in ask--");
					boolean askrs = qe.execAsk();
					ServerResponse sr = new ServerResponse("0", Boolean.toString(askrs));
					ServerResponse.writeJsonResult(response, sr);
					
				}else{
					debug("RADIO SELECT ERROR!");
				}
			}finally{
				qe.close();
			}
		}
	}
}

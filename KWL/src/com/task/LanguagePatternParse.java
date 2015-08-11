package com.task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.atlas.io.IndentedWriter;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;

public class LanguagePatternParse {
    public static final String testFile = "rdfFiles/test.rdf";
	static public final String NL = System.getProperty("line.separator");
	public static final String TEST = "http://example.org/test/";
	private static final String TEST_FILE = "rdfFiles/test.rdf";
	private static void debug(String str) {
		System.out.println(LanguagePatternParse.class.getName()+" "+str);
	}
//	public static void main(String[] args)
//	{
//		initDataset();
//        Dataset dataset = TDBFactory.createDataset("TESTTDB");
//        
//        ArrayList<String> packages = new ArrayList<String>();
//        packages.add("http://example.org/test/适应症");
//        ArrayList<String> concepts = new ArrayList<String>();
//        concepts.add("http://example.org/test/病");
//        concepts.add("http://example.org/test/症");
//        String pattern = "用于(?:预防|和|因|由|缓解|治疗|防止)*([^等所而及和或与由于。]+(?:[、及和或与由于]+[^等所而及和或与由于。]+)*)[^。]*?引起的([^等及和或与。]+(?:[、及和或与]+[^等及和或与。]+)*)[^。]*";
//        execQuery(dataset, packages, concepts, pattern);
//        printDataset();
//        
//	}

	public static void execQuery(Dataset dataset, ArrayList<String> packages,
		ArrayList<String> concepts, String pattern) {
		ArrayList<Property> ps = null;	//properties
		ArrayList<Property> cs = null;	//properties
//		ArrayList<ArrayList<String>> objsList = new ArrayList<ArrayList<String>>(); //objects list

//		Iterator<String> iter = dataset.listNames();
//		ArrayList<String> modelNames = new ArrayList<String>();
//		String t;
//		while (iter.hasNext()) {
//			t = iter.next();
////			System.out.println(t);
//			modelNames.add(t);
//		}
//		debug(modelNames.toString());
//		for(String modelName : modelNames){
//			Model model = dataset.getNamedModel(modelName);
		dataset.begin(ReadWrite.WRITE);
		try {
			Model model = dataset.getDefaultModel();
			ResIterator ress;
				if (ps == null || ps.size() == 0)
					ps = getProperties(model, packages);// model.getProperty(packages.get(0));
				if (ps.size() == 0) {
					dataset.commit();
					return;
				}
				if (cs == null || cs.size() == 0)
					cs = getProperties(model, concepts);// model.getProperty(packages.get(0));
				if (ps.size() == 0) {
					dataset.commit();
					return;
				}
				ress = model.listResourcesWithProperty(ps.get(0));
				while (ress.hasNext()) {
					Resource res = ress.next();
					//for each property, 得到property value的值，用pattern解析出一个列表，创建一个bag将其添加入res的concept property中
					for (Property p : ps) {
						Statement st = res.getProperty(p);
						RDFNode obj = st.getObject();
						if (!obj.isLiteral()) {
							System.out.println("Strange - not a literal: " + obj);
							continue;
						}
						String value = obj.asLiteral().toString()/*.split("\\^\\^")[0]*/;
						System.out.println(ps.toString()+"    " + value);
						ArrayList<String> objsList = extractPatternValues(value, pattern);
						for(int i = 0; i < objsList.size() && i < cs.size(); i++)
						{
							System.out.println(cs.get(i).toString()+" "+objsList.get(i).toString());
					        res.addProperty(cs.get(i), objsList.get(i));
						}
					}
				}
	//	
				model.commit();
				dataset.commit();
		}finally {
			dataset.end();
		}
		}
//	}
//	private static void printDataset() {
//		Dataset dataset = TDBFactory.createDataset("TESTTDB");//这是一个DATASET，名字为TEST
//        
//        Iterator<String> iter = dataset.listNames();
//        while(iter.hasNext())
//        {
//        	System.out.println("-------------------------------------");
//        	String modelName = iter.next();
//        	System.out.println(modelName);
//        	Model model = dataset.getNamedModel(modelName);
//    		printModel(model);
////    		model.close();
//        }
//        dataset.close();
//        
//	}
//	private static void initDataset() {
//        Dataset dataset = TDBFactory.createDataset("TESTTDB");//这是一个DATASET，名字为TEST
//        System.out.println("-------------------------------------");
//        Iterator<String> iter = dataset.listNames();
//        while(iter.hasNext())
//        {
//        	System.out.println(iter.next());
//        }
//        //Model model = dataset.getDefaultModel();//这里是创建一个默认MODEL，要是想创建一个自己命名的MODEL可以用如下的代码
////        Model model = dataset.getNamedModel("test");//注意这里只能是一个URI的形式，否则无法外部访问！！！
//        Model model = ModelFactory.createDefaultModel();
////        
////		try {
////			InputStreamReader in = new InputStreamReader(new FileInputStream(testFile), "GBK");
////			model.read(in, null);
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//        
//        FileManager.get().readModel(model, testFile);//读取FOAF文件到MODEL里面
////        model.commit();//提交MODEL
//        
////        dataset.addNamedModel("test", model);
//        dataset.addNamedModel("test", model);
//        
//		try {
//			OutputStreamWriter stdout = new OutputStreamWriter(System.out,
//					"GBK");
//			model.write(stdout);// 默认输出为XML/RDF
////			ResultSetFormatter.out(results);
////			results.getResourceModel().write(stdout);
//
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
////		model.commit();
////		model.close();
//		dataset.close();
//		
//	}
	public static void printModel(Model model)
	{
		try {
			OutputStreamWriter stdout = new OutputStreamWriter(System.out,
					"GBK");
			model.write(stdout);// 默认输出为XML/RDF
//			ResultSetFormatter.out(results);
//			results.getResourceModel().write(stdout);

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static ArrayList<String> extractPatternValues(String content, String pattern) {
		ArrayList<String> ret = new ArrayList<String>();
		Pattern p = Pattern.compile(pattern);// ,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		if(m.find()) {
			//System.out.println(m.group());
			System.out.println("------------------------------------");
			for (int i = 1; i <= m.groupCount(); i++) {
				ret.add(m.group(i));
			}
		}
		return ret;
	}
//	private ArrayList<ArrayList<String>> splitPattern(File f) {
//		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
//		String reg = "[、及和或与由于]";
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(f));
//			String line = null;
//			while((line = br.readLine()) != null)
//			{
//				String[] strs = line.split(reg);
//				ArrayList<String> list = new ArrayList<String>();
//				for(String str : strs)
//				{
//					if(str.equals(""))
//						continue;
//					list.add(str);
//				}
//				ret.add(list);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ret;
//	}

	private static ArrayList<Property> getProperties(Model model,
			ArrayList<String> pros) {
		ArrayList<Property> ps = new ArrayList<Property>();
		for (String pro : pros) {
			ps.add(model.getProperty(pro));
		}
		return ps;
	}

	public static void _main(String[] args) {
		// Create the data.
		// This wil be the background (unnamed) graph in the dataset.
		Model model = ModelFactory.createDefaultModel();
		FileManager.get().readModel(model, TEST_FILE);
		model.write(System.out);
		System.out.println("---------------------------------------------");

		// First part or the query string
		String prolog = "PREFIX test: <" + TEST + ">";

		// Query string.
		String queryString = prolog + NL
				+ "SELECT ?p WHERE {?x test:pattern ?p}";

		Query query = QueryFactory.create(queryString);
		// Print with line numbers
		query.serialize(new IndentedWriter(System.out, true));
		System.out.println();

		// Create a single execution of this query, apply to a model
		// which is wrapped up as a Dataset

		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		// Or QueryExecutionFactory.create(queryString, model) ;

		System.out.println("Ps: ");

		try {
			// Assumption: it's a SELECT query.
			ResultSet rs = qexec.execSelect();

			// The order of results is undefined.
			for (; rs.hasNext();) {
				QuerySolution rb = rs.nextSolution();

				// Get title - variable names do not include the '?' (or '$')
				RDFNode x = rb.get("p");

				// Check the type of the result value
				if (x.isLiteral()) {
					Literal titleStr = (Literal) x;
					System.out.println("    " + titleStr);
				} else
					System.out.println("Strange - not a literal: " + x);

			}
		} finally {
			// QueryExecution objects should be closed to free any system
			// resources
			qexec.close();
		}
	}

	public static Model createModel() {
		Model m = ModelFactory.createDefaultModel();

		Resource r1 = m.createResource("http://example.org/book#1");
		Resource r2 = m.createResource("http://example.org/book#2");

		r1.addProperty(DC.title, "SPARQL - the book").addProperty(
				DC.description, "A book about SPARQL");

		r2.addProperty(DC.title, "Advanced techniques for SPARQL");

		return m;
	}
}

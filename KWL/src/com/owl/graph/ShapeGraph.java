package com.owl.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;

import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;
import org.xml.sax.helpers.AttributesImpl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class ShapeGraph {

	public ShapeGraph fatherGraph;
	public static Random r = null;
	public Vector<ShapeGraph> sonGraphs ;
	public String uri;
	public String version;
	public String comment;
	public String defaultNamespace;
	public String name;
	//XML store as string
	public String namespaces;
	public String urlspaces;

	//size  info
	public double x;
	public double y;
	public double width;
	public double height;
	public int color;
	
	public static final int STRING=0;
	public static final int NORMALIZEDSTRING=1;
	public static final int BOOLEAN=2;
	public static final int DECIMAL=3;
	public static final int FLOAT=4;
	public static final int DOUBLE=5;
	public static final int INTEGER=6;
	public static final int NONNEGATIVEINTEGER=7;
	public static final int POSITIVEINTEGER=8;
	public static final int NONPOSITIVEINTEGER=9;
	public static final int NEGATIVEINTEGER=10;
	public static final int LONG=11;
	public static final int INT=12;
	public static final int SHORT=13;
	public static final int BYTE=14;
	public static final int UNSIGNEDLONG=15;
	public static final int UNSIGNEDINT=16;
	public static final int UNSIGNEDSHORT=17;
	public static final int UNSIGNEDBYTE=18;
	public static final int HEXBINARY=19;
	public static final int BASE64BINARY=20;
	public static final int DATATIME=21;
	public static final int TIME=22;
	public static final int DATE=23;
	public static final int GYEARMONTH=24;
	public static final int GYEAR=25;
	public static final int GMONTHDAY=26;
	public static final int GDAY=27;
	public static final int GMONTH=28;
	public static final int ANYURI=29;
	public static final int TOKEN=30;
	public static final int LANGUAGE=31;
	public static final int NMTOKEN=32;
	public static final int NAME=33;
	public static final int NCNAME=34;
	
	public static Random getRandom() {
		if (r ==null) {
			r = new Random();
			r.setSeed(System.currentTimeMillis());
		}
		return r;
	}
	public static void debug(String str) {
		System.out.println(str);
	}
	
	public void p(String s) {
		System.out.println(s);
	}
	public void e(String s) {
		System.err.println(s);
	}
	//从xml中读取
	public void saveAsOwlFromXml(String xml, String filepath) {
		System.out.println("savaAsOwlFromXml!!!!!!!!!!"+filepath);
		Document doc = Jsoup.parse(xml, "", new Parser(new XmlTreeBuilder()));
		String name = doc.child(0).attr("name");
		String uri = doc.child(0).attr("uri");
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		String ns = uri+name+"#";
		ontModel.setNsPrefix("base", ns);
		Ontology ontol = ontModel.createOntology(ns.replace("#", ""));
		ontol.addProperty(RDFS.comment,
				"This is an ontology for a process-oriented requirement modeling tool.");
		ontol.addProperty(RDFS.label, "Concept Ontology");
		ontol.addProperty(OWL.versionInfo, "$serialVersionUID:2.0 Dec. 2013$");
		//采用jsoup的访问xml方式来访问xml
//		p(doc.child(0).tagName());  这一层刚好到g
		Elements eles = doc.children();
		Element t;
		Iterator<Element> it = eles.iterator();
		while (it.hasNext()) {
			t = it.next();
			saveElement(t,ontModel, ns);
		}
		p(ontModel.toString());
		//String path = realpath+Config.graph2OwlPath+filename+".owl";
		
		File file = new File(filepath);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ontModel.write(fos, "RDF/XML-ABBREV");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//逐层解析
	private void saveElement(Element ele, OntModel ontModel, String ns) {
		Attribute attr;
		String temp,src,tar;
		OntClass curClass, fatherClass, tarClass, srcClass;
		ObjectProperty objProp;
		Element sonele;
		Iterator<Attribute> iter = ele.attributes().iterator();
		
		if (ele.tagName().equals("cls")) {
			//如果是概念，添加的是属性，包括父节点
			if (null == (temp = ele.attr("n"))) {
				e("ontology class don't have name! ");
				return;
			}
			
			//1创建名字的本体
			curClass = ontModel.getOntClass(ns + temp);
			if(curClass == null)
				curClass = ontModel.createClass(ns+temp);
			//2、查看是否有父类
			if (ele.hasAttr("f")) {
				temp = ele.attr("f");
				fatherClass = ontModel.getOntClass(ns+temp);
				if (fatherClass == null ) {
					e("father "+ temp +" is null");
					return;
				}
				curClass.addSuperClass(fatherClass);
			}
			Iterator<Element> it = ele.children().iterator();
			List<String> oproList = new ArrayList<String>();
			List<String> dproList = new ArrayList<String>();
			List<String> clsList = new ArrayList<String>();
			while (it.hasNext()) {
				sonele = it.next();
				if(sonele.tagName().equals("opro")) {
					Iterator<Element> subit = sonele.children().iterator();
					while(subit.hasNext())
					{
						Element subsonele = subit.next();
						if(subsonele.tagName().equals("property"))
						{
							temp = subsonele.attr("name");
							String tarClsName = subsonele.attr("tar");
							oproList.add(temp);
							clsList.add(tarClsName);
							if(ontModel.getObjectProperty(ns + temp) == null)
								ontModel.createObjectProperty(ns + temp);
							if(ontModel.getOntClass(ns + tarClsName) == null)
								ontModel.createClass(ns + tarClsName);
						}
					}
				} else if(sonele.tagName().equals("dpro")) {
					Iterator<Element> subit = sonele.children().iterator();
					while(subit.hasNext())
					{
						Element subsonele = subit.next();
						if(subsonele.tagName().equals("property"))
						{
							temp = subsonele.attr("name");
							dproList.add(temp);
							if(ontModel.getDatatypeProperty(ns + temp) == null)
							{
								DatatypeProperty dataPro = ontModel.createDatatypeProperty(ns + temp);
								dataPro.addDomain(curClass);
								int range = Integer.parseInt(subsonele.attr("range"));
								switch(range) {
								case STRING:
									dataPro.addRange(XSD.xstring);
									break;
								case NORMALIZEDSTRING:
									dataPro.addRange(XSD.normalizedString);
									break;
								case BOOLEAN:
									dataPro.addRange(XSD.xboolean);
									break;
								case DECIMAL:
									dataPro.addRange(XSD.decimal);
									break;
								case FLOAT:
									dataPro.addRange(XSD.xfloat);
									break;
								case DOUBLE:
									dataPro.addRange(XSD.xdouble);
									break;
								case INTEGER:
									dataPro.addRange(XSD.integer);
									break;
								case NONNEGATIVEINTEGER:
									dataPro.addRange(XSD.nonNegativeInteger);
									break;
								case POSITIVEINTEGER:
									dataPro.addRange(XSD.positiveInteger);
									break;
								case NONPOSITIVEINTEGER:
									dataPro.addRange(XSD.nonPositiveInteger);
									break;
								case NEGATIVEINTEGER:
									dataPro.addRange(XSD.negativeInteger);
									break;
								case LONG:
									dataPro.addRange(XSD.xlong);
									break;
								case INT:
									dataPro.addRange(XSD.xint);
									break;
								case SHORT:
									dataPro.addRange(XSD.xshort);
									break;
								case BYTE:
									dataPro.addRange(XSD.xbyte);
									break;
								case UNSIGNEDLONG:
									dataPro.addRange(XSD.unsignedLong);
									break;
								case UNSIGNEDINT:
									dataPro.addRange(XSD.unsignedInt);
									break;
								case UNSIGNEDSHORT:
									dataPro.addRange(XSD.unsignedShort);
									break;
								case UNSIGNEDBYTE:
									dataPro.addRange(XSD.unsignedByte);
									break;
								case HEXBINARY:
									dataPro.addRange(XSD.hexBinary);
									break;
								case BASE64BINARY:
									dataPro.addRange(XSD.base64Binary);
									break;
								case DATATIME:
									dataPro.addRange(XSD.dateTime);
									break;
								case TIME:
									dataPro.addRange(XSD.time);
									break;
								case DATE:
									dataPro.addRange(XSD.date);
									break;
								case GYEARMONTH:
									dataPro.addRange(XSD.gYearMonth);
									break;
								case GYEAR:
									dataPro.addRange(XSD.gYear);
									break;
								case GMONTHDAY:
									dataPro.addRange(XSD.gMonthDay);
									break;
								case GDAY:
									dataPro.addRange(XSD.gDay);
									break;
								case GMONTH:
									dataPro.addRange(XSD.gMonth);
									break;
								case ANYURI:
									dataPro.addRange(XSD.anyURI);
									break;
								case TOKEN:
									dataPro.addRange(XSD.token);
									break;
								case LANGUAGE:
									dataPro.addRange(XSD.language);
									break;
								case NMTOKEN:
									dataPro.addRange(XSD.NMTOKEN);
									break;
								case NAME:
									dataPro.addRange(XSD.Name);
									break;
								case NCNAME:
									dataPro.addRange(XSD.NCName);
									break;
								}
								
								
							}
						}
					}
				} else if(sonele.tagName().equals("ind")) {
					Iterator<Element> subit = sonele.children().iterator();
					while(subit.hasNext())
					{
						Element subsonele = subit.next();
						if(subsonele.tagName().equals("individual")) {
							temp = subsonele.attr("name");
							Individual ind = ontModel.createIndividual(ns + temp, curClass);
							for(int i = 0; i < oproList.size(); i++) {
								if(subsonele.hasAttr(oproList.get(i))) {
									if(ontModel.getIndividual(ns + subsonele.attr(oproList.get(i))) == null) {
										ontModel.createIndividual(ns + subsonele.attr(oproList.get(i)), ontModel.getResource(ns + clsList.get(i)));
									}
									ind.addProperty(ontModel.getProperty(ns + oproList.get(i)), ontModel.getIndividual(ns + subsonele.attr(oproList.get(i))));
								}
							}
							for(int i = 0; i < dproList.size(); i++) {
								if(subsonele.hasAttr(dproList.get(i))) {
									ind.addProperty(ontModel.getProperty(ns + dproList.get(i)), subsonele.attr(dproList.get(i)));
								}
							}
						}
					}
				} 
			}
		} else if (ele.tagName().equals("rel")) {
			//如果是关系
			if ( !ele.hasAttr("n") ||
					!ele.hasAttr("f") ||
					!ele.hasAttr("t") ) {
				e("ontology property don't have name or relative class");
				return;
			}
			temp = ele.attr("n");
			src = ele.attr("f");
			tar = ele.attr("t");
			String isFunc = ele.attr("func");
			String isSyme = ele.attr("syme");
			String isTran = ele.attr("tran");
			String Inverse = ele.attr("inve");
			String domain = ele.attr("domain");
			String range = ele.attr("range");
				
			
			objProp = ontModel.getObjectProperty(ns+temp);
			if (null == objProp) 
				objProp = ontModel.createObjectProperty(ns+temp);
			if (null == (srcClass = ontModel.getOntClass(ns+src)) || 
					null == (tarClass = ontModel.getOntClass(ns+tar))) {
				e("srcClass or tarClass don't exist");
				return;
			}
			if(isFunc.equals("true"))
				objProp.convertToFunctionalProperty();
			if(isSyme.equals("true"))
				objProp.convertToSymmetricProperty();
			if(isTran.equals("true"))
				objProp.convertToTransitiveProperty();
			if(!Inverse.equals(""))
			{
				if(ontModel.getObjectProperty(ns+Inverse)==null)
					ontModel.createObjectProperty(ns + Inverse);
				objProp.addInverseOf(ontModel.getObjectProperty(ns + Inverse));
			}
			if(!domain.equals(""))
				objProp.addDomain(ontModel.getOntClass(ns + domain));
			if(!range.equals(""))
				objProp.addRange(ontModel.getOntClass(ns + range));
			srcClass.addProperty(objProp, tarClass);
			//add object property
		} else if (ele.tagName().equals("g")){
			//如果是图,添加的是子图g
			Iterator<Element> it = ele.children().iterator();
			while (it.hasNext()) {
				sonele = it.next();
				saveElement(sonele, ontModel, ns);				
			}
		}
	}
	
	public String loadAsXmlFromOwl(String owlpath) {
		System.out.println("loadAsXmlFromOwl!!!!!!!!!!"+owlpath);
		OntModel ontModel = ModelFactory.createOntologyModel();
		String strURL = "file:"+owlpath;
		StringWriter sw = new StringWriter();
		try {
			ontModel.read(strURL);
			//create xml
			SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();  
	        TransformerHandler handler = factory.newTransformerHandler();
	        
	        handler.setResult(new StreamResult(sw));
	        //set xml property
	        
	        //
	        handler.startDocument();
	        AttributesImpl attr = new AttributesImpl(); 
	        
	        attr.clear();
	        attr.addAttribute("", "", "name", "", "root");
	        attr.addAttribute("", "", "uri", "", "");
	        handler.startElement("", "", "g", attr);
	        
	        HashSet<String> classset = new HashSet<String>();
	        //build component 
			Iterator<OntClass> ontit = ontModel.listNamedClasses();
			//读取概念
			while(ontit.hasNext()){	
				OntClass ontClass = ontit.next();
				String name = ontClass.getLocalName();
				classset.add(name);
				debug(name);
				attr.clear();
				attr.addAttribute("", "", "n", "", name);
				attr.addAttribute("", "", "w" , "", "120");
				attr.addAttribute("", "", "h" , "", "50");
				int pos[] = new int[2];
				getPos(pos);
				attr.addAttribute("", "", "x" , "", String.valueOf(pos[0]));
				attr.addAttribute("", "", "y" , "", String.valueOf(pos[1]));
				attr.addAttribute("", "", "c" , "", "16030750");
				handler.startElement("", "", "c", attr);
				handler.endElement("", "", "c");
			}
			
			int totaledge = 0;
			ontit = ontModel.listNamedClasses();
			while (ontit.hasNext()) {
				OntClass ontClass = ontit.next();
				String name = ontClass.getLocalName();
//				Iterator<OntClass> supit = ontClass.listSuperClasses();
//				debug("ontModel name "+name);
//				//读类内部的继承关系
//				while (supit.hasNext()) {
//					OntClass supClass = supit.next();
//					String supname = supClass.getLocalName();
//					debug("ontModel supname "+supname);
//					if (!classset.contains(supname)) continue;
//					attr.clear();
//					debug("subClassOf "+name+" "+supname);
//					attr.addAttribute("", "", "n", "", "subClassOf");
//					attr.addAttribute("", "", "f", "", name);
//					attr.addAttribute("", "", "t", "", supname);
//					attr.addAttribute("", "", "i", "", "edge:"+totaledge+++"|Relation");
//					handler.startElement("", "", "r", attr);
//					handler.endElement("", "", "r");				
//				}
				//读类内部的关联关系
				StmtIterator st =  ontClass.listProperties();
				while (st.hasNext()) {
					Statement s = st.nextStatement();
					String pro = s.getPredicate().getLocalName();
					RDFNode rdf = s.getObject();
					if (!rdf.isResource()) {
						continue;
					}
					String objname = rdf.asResource().getLocalName();
					if (!classset.contains(objname)) continue;
					if (pro.equals("subClassOf") && objname.endsWith(name)) continue;
					attr.clear();
					debug(pro+" "+name+" "+objname);
					attr.addAttribute("", "", "n", "", pro);
					attr.addAttribute("", "", "f", "", name);
					attr.addAttribute("", "", "t", "", objname);
					attr.addAttribute("", "", "i", "", "edge:"+totaledge+++"|Relation");
					handler.startElement("", "", "r", attr);
					handler.endElement("", "", "r");	
				}
			}
			//build relation
			//读独立的关联关系
			Iterator<ObjectProperty> proit = ontModel.listObjectProperties();
			while (proit.hasNext()) {
				ObjectProperty objPro = proit.next();
				OntResource fs = objPro.getDomain();
				if (fs == null || !fs.canAs(OntClass.class)) continue;
				OntClass f = fs.as(OntClass.class);
				if (!classset.contains(f.getLocalName())) continue;
				OntResource ts = objPro.getRange();
				if (ts == null || !ts.canAs(OntClass.class)) continue;
				OntClass t = ts.as(OntClass.class);
				if (!classset.contains(t.getLocalName())) continue;
				attr.clear();
				debug(objPro.getLocalName()+" "+f.getLocalName()+" "+t.getLocalName());
				attr.addAttribute("", "", "n", "", objPro.getLocalName());
				attr.addAttribute("", "", "f", "", f.getLocalName());
				attr.addAttribute("", "", "t", "", t.getLocalName());
				attr.addAttribute("", "", "i", "", "edge:"+totaledge+++"|Relation");
				handler.startElement("", "", "r", attr);
				handler.endElement("", "", "r");
			}
			
	        handler.endElement("", "", "g");
	        handler.endDocument();
	        
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sw.toString();
		
	}
	private void getPos(int pos[]) {
		Random r = getRandom();
		pos[0] = r.nextInt(600)*3;
		pos[1] = r.nextInt(600)*3;
	}
	
}

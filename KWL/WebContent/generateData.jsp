<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@	page import=" com.hp.hpl.jena.ontology.*" %>
<%@	page import=" java.util.*" %>
<%@	page import=" java.io.*" %>
<%@ page import=" java.io.Writer" %>
<%@ page import="java.sql.*"%>
<%@	page import=" com.hp.hpl.jena.reasoner.*" %>
<%@	page import=" java.util.Iterator" %>
<%@	page import=" com.hp.hpl.jena.rdf.model.InfModel" %>
<%@	page import=" com.hp.hpl.jena.rdf.model.Model" %>
<%@	page import=" com.hp.hpl.jena.rdf.model.ModelFactory" %>
<%@	page import=" com.hp.hpl.jena.reasoner.ValidityReport" %>
<%@	page import=" com.hp.hpl.jena.util.FileManager" %>

<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="com.structure.graph.Graph" %>
<%@ page import="com.structure.graph.Edge" %>
<%@ page import="com.structure.graph.Element" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<title>北京大学知件库</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/query.css" rel="stylesheet" type="text/css" />
	

</head>
<body>

	<%!
	
	public void  getclass(Graph t_graph,int depth, OntClass father, Element ft)//深度表示他是第几个结点   记录打开的对象
	{
//		System.out.println(father.getLocalName());
		
		for(Iterator j = father.listSubClasses();j.hasNext();)
		{
			
			OntClass cc = (OntClass) j.next();
			if(!cc.isAnon())
			{
				Element temp = new Element();
				String prostr =  cc.getLocalName();
				temp.setProperty("name",prostr);
				temp.father = depth;
				temp.myself = t_graph.total;
				
				Edge subedge = new Edge();
				subedge.setProperty("name","subclass");
				subedge.setFrom(ft);
				subedge.setTo(temp);
				t_graph.setEdge(subedge);
				
				/*
				*/
				for( Iterator spp = cc.listSuperClasses();spp.hasNext();)
				{
					
					OntClass tempsub = (OntClass)spp.next();
					
					if (tempsub.isRestriction()) 
					{
				    	Restriction r = tempsub.asRestriction();
				    	if (r.isAllValuesFromRestriction()) 
				    	{		    		
				    		AllValuesFromRestriction sv = r.asAllValuesFromRestriction();			      
				    		if(sv.getAllValuesFrom().getLocalName()!=null)
				    		{
				    		
				    			Element tempele = new Element();//构造一个结点	
				    			String pro2str =  sv.getAllValuesFrom().getLocalName();
				    			tempele.setProperty("name",pro2str);//将节点名称赋值
				    			Edge edge = new Edge();//构造一条边
				    			edge.setProperty("name",r.getOnProperty().getLocalName());
				    			edge.setFrom(temp);//边的来龙去脉都要交代，但是不需要添加进入图中
				    			edge.setTo(tempele);
				    			t_graph.setEdge(edge);
				    		}
							
				    	}
				    	if (r.isSomeValuesFromRestriction()) 
				    	{			      			    	
				    		SomeValuesFromRestriction ss = r.asSomeValuesFromRestriction();			      
				    		
				    		Element tempele = new Element();//构造一个结点
				    		
				    		String pro2str =  ss.getSomeValuesFrom().getLocalName();
							tempele.setProperty("name",pro2str);//将节点名称赋值
							
							Edge edge = new Edge();//构造一条边
							edge.setProperty("name",r.getOnProperty().getLocalName());
							edge.setFrom(temp);
							edge.setTo(tempele);
							t_graph.setEdge(edge);//添加一条边
				    	}			    	
					}
				}

		        //实验区代码
				for(Iterator ipp = cc.listDeclaredProperties(); ipp.hasNext();)
			    {
			     	OntProperty p = (OntProperty)ipp.next();
			      		      	
			      	if(p.getRange()!=null)
			      	{			
			      		String pro3str = p.getLocalName()+"~";
			      		pro3str += p.getRange().getLocalName();
			      		temp.setProperty(p.getLocalName(),pro3str);
			      		
			      	}
			      	/*
			      	*/
			      	
			    }
				
				/**
				*/
				t_graph.setElement( temp );
				t_graph.total++;
				getclass(t_graph,temp.myself,cc,temp);
				
			}
		}
	}
	public void debug(String str) {
		System.out.println(str);
	}
	%>
	<%
	request.setCharacterEncoding("UTF-8");
	

		String file = (String)request.getParameter("filename");
		String pos = (String)request.getParameter("pos");
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		debug(file);
		String filepath;
		if (pos.compareTo("online")==0){
			filepath = this.getServletConfig().getServletContext().getRealPath("/")+"/owl/online/"+file;
		}else{
			filepath = this.getServletConfig().getServletContext().getRealPath("/")+"/owl/local/"+file;
		}
		filepath = filepath.replaceAll("\\\\", "/").replaceAll("//", "/");//进行符号转换
		try {
			ontModel.read("file:"+filepath);
			
			debug(filepath);
			Graph graph = new Graph();
			graph.total = 0;
			Element root = new Element();
			root.father = -1;
			root.myself = graph.total;
			String propertyname =  file;
			root.setProperty("name",propertyname);
			graph.setElement( root );
			graph.total++;
			
			for(Iterator i=ontModel.listHierarchyRootClasses();i.hasNext();)
			{
				OntClass c = (OntClass)i.next();
				if(!c.isAnon())
				{
					Element t = new Element();
					String prostr =  c.getLocalName();
					t.setProperty("name",prostr);
					
					t.father = 0;
					t.myself = graph.total;
					/*
					*/
	//System.out.println("hiperclass:  "+c.getLocalName());				
					for( Iterator spp = c.listSuperClasses();spp.hasNext();)
					{
						
						OntClass tempsub = (OntClass)spp.next();
						
						if (tempsub.isRestriction()) 
						{
					    	Restriction r = tempsub.asRestriction();
					    	if (r.isAllValuesFromRestriction()) 
					    	{		    		
					    		AllValuesFromRestriction sv = r.asAllValuesFromRestriction();			      
					    		
					    		Element tempele = new Element();//构造一个结点
					    		String pro2str = sv.getAllValuesFrom().getLocalName();
								tempele.setProperty("name",pro2str);//将节点名称赋值
								
								Edge edge = new Edge();//构造一条边
								edge.setProperty("name",r.getOnProperty().getLocalName());
								edge.setFrom(t);//边的来龙去脉都要交代，但是不需要添加进入图中
								edge.setTo(tempele);
								graph.setEdge(edge);
								
					    	}
					    	if (r.isSomeValuesFromRestriction()) 
					    	{			      			    	
					    		SomeValuesFromRestriction ss = r.asSomeValuesFromRestriction();			      
					    		
					    		Element tempele = new Element();//构造一个结点
					    		String pro2str = ss.getSomeValuesFrom().getLocalName();
								tempele.setProperty("name",pro2str);//将节点名称赋值
								
								Edge edge = new Edge();//构造一条边
								edge.setProperty("name",r.getOnProperty().getLocalName());
								edge.setFrom(t);
								edge.setTo(tempele);
								graph.setEdge(edge);//添加一条边
					    	}			    	
						}
					}
	
			        //实验区代码
					for(Iterator ipp = c.listDeclaredProperties(); ipp.hasNext();)
				    {
				     	OntProperty p = (OntProperty)ipp.next();
				      		      	
				      	if(p.getRange()!=null)
				      	{		
				      		String pro3str = p.getLocalName()+"~";
				      		pro3str += p.getRange().getLocalName();
				      		t.setProperty(p.getLocalName(),pro3str);
				      		
				      	}
				      	/*
				      	*/
				      	
				    }
					/*
					*/
					graph.total++;
					graph.setElement(t);
					
					
					getclass(graph,t.myself,c,t);
				}
			}
			
			String elearray="";
			
			int ele=0;
			for(Iterator j=graph.listElement();j.hasNext();)
			{
				ele++;
				Element t = (Element)j.next();	
				String name = t.getProperty("name");
				elearray += name+"/"+t.father+"/"+t.myself;
				for(Iterator s=t.listProperty();s.hasNext();)
				{
					String temp = s.next().toString();
					if(temp!=name)
					{
						elearray += "/"+temp;
					}
				}
				elearray +="|";
			}
			String edgarray="";
			int edg=0;
			for(Iterator k=graph.listEdges();k.hasNext();)
			{
				edg++;
				Edge t = (Edge)k.next();
				edgarray += (t.getProperty("name")+"/"+t.getFrom().getProperty("name")+"/"+t.getTo().getProperty("name")+"|");
			}
	
			debug("end");
			session.setAttribute("Server1ResEle",elearray);
			session.setAttribute("Server1ResElen",ele);
			session.setAttribute("Server1ResEdg",edgarray);
			session.setAttribute("Server1ResEdgn",edg);
			response.sendRedirect("display.jsp?result=success");//后台运行结束，转移到前台显示
		} catch (Exception e) {
			response.sendRedirect("display.jsp?result=fail");//后台运行结束，转移到前台显示
		}
	%>


</body>
</html>
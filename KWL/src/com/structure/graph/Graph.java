package com.structure.graph;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Graph {

		public Hashtable<String,String>  property;//图结构本身属性
		public Hashtable<String,Edge> edgelist;				//存放边
		public Vector<Element> elementlist;			//存放点
		public int total;
		
		public Graph()
		{
			property = new Hashtable();
			edgelist = new Hashtable();
			elementlist = new Vector();
		}
		public void setProperty(String key,String value)
		{
			property.put(key, value);
		}
		public String getProperty(String key)
		{
			String value = (String) ( property.get(key));
			return value;
		}
		
		public void setEdge(Edge e)
		{
			edgelist.put(e.getProperty("name")+e.getFrom().getProperty("name")+e.getTo().getProperty("name"),e);
		}
		
		public Iterator listEdges()
		{
			Collection<Edge>c = edgelist.values();
			return c.iterator();
		}
		public void setElement(Element p)
		{
			elementlist.add(p);
		}
		public Iterator listElement()
		{
			return elementlist.iterator();
		}
		public Iterator listProperty()
		{
			return property.entrySet().iterator();
		}
		
	}
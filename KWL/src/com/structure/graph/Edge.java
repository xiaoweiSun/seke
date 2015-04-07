package com.structure.graph;

import java.util.Hashtable;
import java.util.Iterator;

 public class Edge {

		public Hashtable<String,String> property;
		public Element from;
		public Element to;
		public Edge()
		{
			property = new Hashtable();
		}
		public void setFrom(Element _from)
		{
			from = _from;
		}
		public void setTo(Element _to)
		{
			to = _to;
		}
		public Element getFrom()
		{
			return from;
		}
		public Element getTo()
		{
			return to;
		}
		public void setProperty(String key,String value)
		{
			property.put(key, value);
		}
		public String getProperty(String key)
		{
			return (String)(property.get(key));
		}
		public Iterator listProperty()
		{
			return property.entrySet().iterator();
		}
		
	}
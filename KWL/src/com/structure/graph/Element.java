package com.structure.graph;

import java.util.Hashtable;
import java.util.Iterator;

 public class Element {

		public Hashtable<String,String> property;
		public int father;
		public int myself;
		public Element()
		{
			
			property = new Hashtable();
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
			return property.values().iterator();
		}
	}
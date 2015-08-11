package com.graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MySAXHandler extends DefaultHandler {
	public HashMap<String,HashMap<String,HashSet<String>>> rm = new HashMap<String,HashMap<String,HashSet<String>>>();
	public HashMap<String,HashSet<String>> cm = new HashMap<String,HashSet<String>>();
	ArrayList<String> gStack = new ArrayList<String>();
	int depth=-1;
	// 瀵拷顬婄憴锝嗙�XML閺傚洣娆�
	public void startDocument() throws SAXException {
	}

	// 缂佹挻娼憴锝嗙�XML閺傚洣娆�
	public void endDocument() throws SAXException {
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("g")) {
			gStack.add(++depth,attributes.getValue("name"));
			
		}else if (qName.equals("c")) {
			String curG = gStack.get(depth);
			if (!cm.containsKey(curG))
				cm.put(curG, new HashSet<String>());
			cm.get(curG).add(attributes.getValue("n"));
		}else if (qName.equals("r")) {
			String curG = gStack.get(depth);
			if (!rm.containsKey(curG))
				rm.put(curG, new HashMap<String,HashSet<String>>());
			String from = attributes.getValue("f");
			String to = attributes.getValue("t");
			if (!rm.get(curG).containsKey(from))
				rm.get(curG).put(from,new HashSet<String>());
			if (!rm.get(curG).containsKey(to) || !rm.get(curG).get(to).contains(from))
				rm.get(curG).get(from).add(to);
		}

	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("g")) {
			--depth;
		}
	}
}
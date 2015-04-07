package com.graph;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Main {
	static Random random = new Random();
	static SAXParserFactory saxfac = SAXParserFactory.newInstance();
	//Map<g.name,Map<point.name,Point>>
	//Point p = dMap.get("disease").get("大病");
	//x = p.x;
	//y = p.y;
	public static HashMap<String,HashMap<String, Point>> dMap = new HashMap<String,HashMap<String, Point>>();

	public static void main(String[] args) {
		parserXml("C:\\Users\\Felix\\Desktop\\lab\\disease.ogeditor");
		
		System.out.println("finish");
	}

	public static void parserXml(String fileName) {

		try {

			MySAXHandler h = parseXml(fileName, saxfac);

			HashMap<String, HashSet<String>> cm = h.cm;
			HashMap<String, HashMap<String, HashSet<String>>> rm = h.rm;
			HashMap<String, Point> d = new HashMap<String, Point>();

			for (Iterator<String> it = cm.keySet().iterator(); it.hasNext();) {
				String g = it.next();
				HashSet<String> cSet = cm.get(g);
				HashMap<String, HashSet<String>> rMap = rm.get(g);

				init(d, cSet);

				boolean cont = true;
				while (cont) {
					update(d, cSet);
					//log(d);
					push(d, cSet);
					//log(d);
					pull(d, rMap);
					//log(d);
					res(d, cSet);
					//log(d);
					col(d, cSet);
					//log(d);
					cont=stopCheck(d, cSet);
				}
				log(d,g);
			}
		} catch (ParserConfigurationException e) {

			e.printStackTrace();

		} catch (SAXException e) {

			e.printStackTrace();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		
	}

	private static boolean stopCheck(HashMap<String, Point> d, HashSet<String> cSet) {
		// stop check
		boolean z=false;
		for (Iterator<String> itc = cSet.iterator(); itc.hasNext();) {
			String c1 = itc.next();
			Point p1 = d.get(c1);
			z=z||p1.checkStop();
		}
		return z;
	}

	private static void update(HashMap<String, Point> d, HashSet<String> cSet) {
		// update
		for (Iterator<String> itc = cSet.iterator(); itc.hasNext();) {
			String c1 = itc.next();
			Point p1 = d.get(c1);
			p1.update();
		}
	}

	private static void col(HashMap<String, Point> d, HashSet<String> cSet) {
		// edge collision
		for (Iterator<String> itc = cSet.iterator(); itc.hasNext();) {
			String c1 = itc.next();
			Point p1 = d.get(c1);
			p1.checkEdge();
		}
	}

	private static void res(HashMap<String, Point> d, HashSet<String> cSet) {
		// resistance
		for (Iterator<String> itc = cSet.iterator(); itc.hasNext();) {
			String c1 = itc.next();
			Point p1 = d.get(c1);
			p1.resistance();
		}
	}

	private static void pull(HashMap<String, Point> d,
			HashMap<String, HashSet<String>> rMap) {
		// edge pull
		if (rMap==null) return;
		for (Iterator<Entry<String, HashSet<String>>> itr = rMap
				.entrySet().iterator(); itr.hasNext();) {
			Entry<String, HashSet<String>> e = itr.next();
			String c1 = e.getKey();
			Point p1 = d.get(c1);
			for (Iterator<String> itr2 = e.getValue().iterator(); itr2
					.hasNext();) {
				String c2 = itr2.next();
				Point p2 = d.get(c2);
				p1.pull(p2);
			}
		}
	}

	private static void push(HashMap<String, Point> d, HashSet<String> cSet) {
		// point push
		for (Iterator<String> itc = cSet.iterator(); itc.hasNext();) {
			String c1 = itc.next();
			Point p1 = d.get(c1);
			for (Iterator<String> itc2 = cSet.iterator(); itc2
					.hasNext();) {
				String c2 = itc2.next();
				Point p2 = d.get(c2);
				if (c1.equals(c2))
					continue;
				p1.push(p2);
			}
		}
	}

	private static void log(HashMap<String, Point> d, String g) {
		System.out.println("--------------------------------------------");
		for (Iterator<Entry<String, Point>> t = d.entrySet().iterator();t.hasNext();){
			Entry<String, Point> e = t.next();
			System.out.println(e.getKey()+" - "+e.getValue().toString());
		}
		dMap.put(g, d);
	}

	private static MySAXHandler parseXml(String fileName,
			SAXParserFactory saxfac) throws ParserConfigurationException,
			SAXException, FileNotFoundException, IOException {
		SAXParser saxparser = saxfac.newSAXParser();

		InputStream is = new FileInputStream(fileName);

		MySAXHandler h = new MySAXHandler();
		saxparser.parse(is, h);
		return h;
	}

	private static void init(HashMap<String, Point> d, HashSet<String> cSet) {
		for (Iterator<String> itc = cSet.iterator(); itc.hasNext();) {
			String c = itc.next();
			Point point = new Point();
			point.x = random.nextInt(20)+Point.R-10;
			point.y = random.nextInt(20)+Point.R-10;
			d.put(c, point);
		}
	}

}

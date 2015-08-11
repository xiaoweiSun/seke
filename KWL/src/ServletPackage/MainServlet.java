package ServletPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.lxh.smart.SmartUpload;
import org.lxh.smart.SmartUploadException;

import com.data.get.crawler;
import com.field.Field;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.index_search.IndexFile;
import com.index_search.SearchFile;
import com.net.ServerResponse;
import com.tool.taskProcess;
import com.tool.toolProcess;

/**
 * Servlet implementation class MainServlet
 */
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static DataBaseMan dbMan;
	private crawler c;
	public static ConnectionMan connectionMan;
	public static long outTime;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainServlet() {
		super();
		// TODO Auto-generated constructor stub
		dbMan = new DataBaseMan();
		outTime = 1000 * 60 * 30;

		if (dbMan == null)
			ErrorLog("dbMan is null In EnergyServlet!");
		connectionMan = new ConnectionMan();
	}
	
	public static void debug(String str) {
		System.out.println("MainServlet.java "+str);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		handleReq(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		handleReq(request, response);
	}

	void ErrorLog(String s) {
		System.err.print("Error: " + s);
	}

	void Log(String s) {
		System.out.print(s);
	}

	private void handleReq(HttpServletRequest request,
			HttpServletResponse response) {
		String type = request.getParameter("type");
		String json = request.getParameter("json");
		if (json != null) {
			try {
				json = URLDecoder.decode(json,"utf-8");
			} catch(Exception e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			StringMap<?> sm = gson.fromJson(json, StringMap.class);
			type = sm.get("type").toString();
			debug(type);

		}
		debug(" type " + type);
		String projectpath = this.getServletConfig().getServletContext()
				.getRealPath("/");

		// ����ϵͳ���½������� ��û��Ҫʹ�ã�
		if ("0".compareTo(type) == 0) {
			StartIndex(request, response, projectpath);
		}
		// �����ļ�
		else if ("1".compareTo(type) == 0) {
			String param[] = new String[2];
			boolean flag = this.CheckLoginState(request, param);
			StartSearch(request, response, projectpath, param[0], param[1]);
		}
		// ����ѡ�е��ļ�
		else if ("2".compareTo(type) == 0) {
			StartDownload(request, response, projectpath);
		}
		// ���ѡ�е��ļ�
		else if ("3".compareTo(type) == 0) {
			StartScan(request, response, projectpath);
		}
		// ץȡ��Ӧ���ļ�
		else if ("4".compareTo(type) == 0) {
			String url = request.getParameter("url");
			c = new crawler(url, projectpath + "owl/online");
			String cookie = request.getParameter("cookie");
			ArrayList<String[]> newfiles = c.start(cookie);
		}
		// ֹͣץȡ
		else if ("5".compareTo(type) == 0) {
			c.stop();
		}
		// �û���¼
		else if ("6".compareTo(type) == 0) {
			login(request, response);
		}
		// �û�ע��
		else if ("7".compareTo(type) == 0) {
			Register(request, response);
		}
		// �����������������Ҫ����¼״̬
		// �ϴ��Լ���֪��
		else if ("8".compareTo(type) == 0) {
			debug("happen");
			String param[] = new String[2];
			boolean flag = this.CheckLoginState(request, param);
			if (!flag) {
				debug("notEqual");
				try {
					response.getWriter().write("login?");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				debug("equal");
				KnowwareProcess.upload(projectpath, this, dbMan, param[0], param[1],
						request, response);
			}
		}
		// �鿴�Լ���֪��
		else if ("9".compareTo(type) == 0) {
			String param[] = new String[2];
			boolean flag = this.CheckLoginState(request, param);
			if (!flag) {
				debug("notEqual");
				ServerResponse res = new ServerResponse("101", "login first");
				ServerResponse.writeJsonResult(response, res);
			} else {
				debug("equal");
				KnowwareProcess.listKnowware(projectpath, this, dbMan, param[0], 
						request, response);
			}
		}
		else if ("11".compareTo(type) == 0) {
			// ���ܴ洢ģ�͵�����
			new toolProcess().save(projectpath, request, response);
		} else if ("12".compareTo(type) == 0) {
			//��ȡģ��
			new toolProcess().read(projectpath, request, response);
		} else if ("13".compareTo(type) == 0) {
			//��ȡ�ļ����б�
			new toolProcess().getFiles(projectpath, request, response);
		} else if ("14".compareTo(type) == 0) {
			//���������������
			new taskProcess().loadTask(projectpath, request, response, dbMan);
		} else if ("15".compareTo(type) == 0) {
			//����Ǵ洢������
			new taskProcess().saveTask(projectpath, request, response, dbMan);
		} else if ("16".compareTo(type) == 0) {
			//�����ִ������
			new taskProcess().execTask(projectpath, request, response, dbMan);
		} else if ("17".compareTo(type) == 0) {
			//Ŀǰ����ֶ����ڲ��� �����µĴ洢��ʽ
			new toolProcess().saveTDB(projectpath, request, response);
		} else if ("18".compareTo(type) == 0) {
			//Ŀǰ����ֶ����ڲ��� �����µĴ洢��ʽ
			Sparql.queryRDF(projectpath, this, dbMan, request, response);
		} else if ("19".compareTo(type) == 0) {
			//��ѯ���е�tdb
			Field.getTDBs(projectpath, this, dbMan, request, response);
		} else if ("20".compareTo(type) == 0) {
			//���tdb���в�ѯ
			ExecQuery.queryTDB(projectpath, this, dbMan, request, response);
		} else if ("21".compareTo(type) == 0) {
			//�г�ȫ���༭��owl
			getEditor(projectpath, this, dbMan, request, response);
		} else if ("22".compareTo(type) == 0) {
			//�洢�ϴ���ȫ���������Ϣ
			Field.saveField(projectpath, this, dbMan, request, response);
		} else if ("23".compareTo(type) == 0) {
			String param[] = new String[2];
			boolean flag = this.CheckLoginState(request, param);
			ServerResponse sr = new ServerResponse("0", flag);
			ServerResponse.writeJsonResult(response, sr);
		} else if ("24".compareTo(type) == 0) {
			//Ҫ���ȡֱ��·��
			new toolProcess().readDirectPathFromOwl(request, response);
		} else if ("25".compareTo(type) == 0) {
			//Ҫ�󱣴�ֱ��·��
			new toolProcess().saveDirectPathAsOwl(request, response);
		} else if ("26".compareTo(type) == 0) {
			//Ҫ�󱣴�ֱ��·��
			StartSearchForService(request, response, projectpath);
		} else if ("27".compareTo(type) == 0) {
			//��������ֹ�ڷ������ض�Ŀ¼���ļ�����������������
			StartIndexFromLocal(request, response, projectpath);
		}
	}

	private void StartIndex(HttpServletRequest request,
			HttpServletResponse response, String realpath) {
		try {
			try {
				new IndexFile().IndexFromInternet(realpath, realpath
						+ "/owl/online/tempfile.txt");
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LockObtainFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("index online has been builded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void StartSearch(HttpServletRequest request,
			HttpServletResponse response, String realpath, String username, String session) {
		long start = new Date().getTime();
		String field = request.getParameter("field");
		String value = request.getParameter("value");
		ArrayList<String[]> al = new ArrayList<String[]>();
		try {
			debug("search");
			new SearchFile(realpath).search(al, field, value, 0);
			/**
			 * ���ò����ش�
			 */
			request.setAttribute("key", value);
			request.setAttribute("hitsnum", al.size());
			request.setAttribute("hits", new Integer(al.size()).toString());
			for (int i = 0; i < al.size(); i++) {
				request.setAttribute("hit" + i, al.get(i)[0] + "|"
						+ al.get(i)[1]);
				request.setAttribute("con" + i, al.get(i)[2]);
				request.setAttribute("pos" + i, al.get(i)[3]);
				// debug(al.get(i)[1]+"|"+al.get(i)[2]);
			}
			long end = new Date().getTime();
			request.setAttribute("time", end - start);
			request.setAttribute("username", username);
			request.setAttribute("session", session);
			this.getServletContext().getRequestDispatcher("/present.jsp?username"+username+"&session="+session)
					.forward(request, response);

		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void StartSearchForService(HttpServletRequest request,
			HttpServletResponse response, String realpath) {
		String field = "filebody";
		String value = request.getParameter("value");
		ArrayList<String[]> al = new ArrayList<String[]>();
		try {
			debug("search");
			new SearchFile(realpath).search(al, field, value, 0);
			/**
			 * ���ò����ش�
			 */
			ServerResponse sr = new ServerResponse("0", al);
			ServerResponse.writeJsonResult(response, sr);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void StartIndexFromLocal(HttpServletRequest request,
			HttpServletResponse response, String realpath) {
		String localdir = realpath+Config.uploadOwlPath;
		ArrayList<String> files = FileProcess.listNames(localdir);
		String config = "wiki";
		String virtualpath = request.getScheme()+"://"
				+ request.getServerName()+":"
				+ request.getServerPort();
		String fileStr[] = new String[files.size()];
		for (int i=0; i<files.size(); i++) {
			fileStr[i] = localdir+files.get(i);
		}
		try {
			new IndexFile().IndexFromLocal(virtualpath, realpath, fileStr, config);
			response.getWriter().write("index local has been builded");
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void StartDownload(HttpServletRequest request,
			HttpServletResponse response, String realpath) {
		String filename = request.getParameter("filename");
		String pos = request.getParameter("pos");

		try {
			SmartUpload smart = new SmartUpload();
			smart.initialize(this.getServletConfig(), request, response);
			smart.setContentDisposition(null);
			if (pos.compareTo("online") == 0) {
				smart.downloadFile(realpath + Config.onlineOwlPath + filename);
			} else if (pos.compareTo("local") == 0) {
				smart.downloadFile(realpath + Config.uploadOwlPath + filename);
			} else {
				smart.downloadFile(realpath + Config.uploadFilesPath + filename);
			}
			debug("download");
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void StartScan(HttpServletRequest request,
			HttpServletResponse response, String realpath) {
		String filename = request.getParameter("filename");
		String pos = request.getParameter("pos");

		File file;
		if (pos.compareTo("online") == 0) {
			file = new File(realpath + "/owl/online/" + filename);
		} else {
			file = new File(realpath + "/owl/local/" + filename);
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			response.getWriter().write(sb.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		debug("scan");
	}

	public void getEditor(String realpath, MainServlet servlet ,DataBaseMan dbMan, 
			HttpServletRequest req, HttpServletResponse resp) {
		String graph2OwlPath = realpath + Config.graph2OwlPath;
		graph2OwlPath = graph2OwlPath.replaceAll("\\\\", "/").replaceAll("//", "/");
		ServerResponse.writeJsonResult(resp, FileProcess.listNames(graph2OwlPath));
	}
	
	// �û���¼
	public void login(HttpServletRequest request, HttpServletResponse response) {
		String user = request.getParameter("username");
		String psw = request.getParameter("password");
		// ת���ɼ��ܺ���ַ���
		debug("what happen");
		String password = MyMD5(psw.getBytes());
		MyResultSet rs = dbMan.SelectCond("user_profile", "userName=" + "'"
				+ user + "'");
		try {
			if (!rs.first()) {
				response.getWriter().write("usr err");
				return;
			}
			if (rs.getString(3).compareTo(password) == 0) {
				String session = MyMD5((user + System.currentTimeMillis() + "")
						.getBytes());
				MyConnection myConn = new MyConnection(user, session,
						System.currentTimeMillis());
				if (connectionMan.Get(user) != null)
					connectionMan.Remove(user);
				connectionMan.Put(myConn);
				response.getWriter().write(session + "&username=" + user);

				Log("us=" + user + " Se=" + session);
			} else {
				debug("no equal");
				response.getWriter().write("failed");
			}
			rs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean CheckLoginState(HttpServletRequest request, String[] param) {
		String name = request.getParameter("username");
		String session = request.getParameter("session");
		MyConnection myConn = connectionMan.Get(name);
		param[0] = name;
		param[1] = session + "&username=" + name;

		debug("client:" + session + " server:" + name);
		if (myConn == null) {
			return false;
		} else if (myConn.session.compareTo(session) != 0) {
			return false;
		} else if (System.currentTimeMillis() - myConn.lastTime > outTime) {
			return false;
		}
		return true;
	}

	private void Register(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("username");
		String psw = request.getParameter("password");
		String[] zd = { "userName", "password" };
		String[] vals = { "'" + name + "'", "'" + MyMD5(psw.getBytes()) + "'" };

		MyResultSet rs = dbMan.SelectCond("user_profile", "userName='" + name
				+ "'");
		String reS = "0";
		if (rs.first()) {
			reS = "fail";
		} else {
			boolean reval = dbMan.InsertRow("user_profile", zd, vals);
			if (reval) {
				reS = "success!";
			}
		}
		rs.close();

		try {
			response.getWriter().write(reS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// md5��������
	public String MyMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
			// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
			// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
				// ת���� 16 �����ַ���ת��
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
				// >>> Ϊ�߼����ƣ�������λһ������
				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ����Ľ��ת��Ϊ�ַ���

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
}

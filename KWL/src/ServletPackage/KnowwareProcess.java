package ServletPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.lxh.smart.SmartUpload;
import org.lxh.smart.SmartUploadException;

import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.index_search.IndexFile;
import com.net.ServerResponse;

public class KnowwareProcess {

	public static void debug (String str) {
//		System.out.println("FileUpload.java "+str);
	}
	public static void upload(String realpath, MainServlet servlet ,DataBaseMan dbMan, 
			String username, String cookie, HttpServletRequest req, HttpServletResponse resp)
	{
		debug("���ڴ����ļ�");
		try {
			SmartUpload smart = new SmartUpload();
			smart.initialize(servlet.getServletConfig(), req, resp);
			smart.upload();
			debug("�ϴ������ɹ�");
		
			String from = smart.getRequest().getParameter("from");
			String owlfile = "";
			//�ж�owl�ļ���Դ�ڿͻ��˻����Ƿ����
			if ("server".equals(from)) {
				owlfile = smart.getRequest().getParameter("serverfile");
				//����Դ��ַ�ж��Ƿ���� 
				String owlorgpath = FileProcess.genPurePath(realpath + Config.graph2OwlPath + owlfile);
				String owldespath = FileProcess.genPurePath(realpath + Config.uploadOwlPath + owlfile);
				File read = new File(owlorgpath);
				debug(owlorgpath);
				debug(owldespath);
				if (!read.exists() || !read.isFile() || !owlfile.endsWith(".owl")) {
					//����owl�ļ�	ҳ����תʵ��		��ת�����ҳ��	���ҷ���һ������	 �ոշ�����֪���ǲ��Ϸ���
					debug(Boolean.toString(read.exists()));
					debug(Boolean.toString(read.isFile()));
					debug(Boolean.toString(owlfile.endsWith(".owl")));
					resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					resp.setHeader("Location","listKnowware.jsp?session="+cookie+"&errno=owl&filename="+owlfile);
					return;				
				}
				//���ļ��ӱ༭Ŀ¼�������ϴ�Ŀ¼
				int length = 4092, bufflen;
				InputStream in = new FileInputStream(owlorgpath);
				OutputStream out = new FileOutputStream(owldespath);
				byte[] buffer = new byte[length];
				while ( (bufflen = in.read(buffer)) != -1) {
					out.write(buffer, 0, bufflen);
				}
				out.flush();
				out.close();
				in.close();
			} else {
				org.lxh.smart.File f = smart.getFiles().getFile(0);
				owlfile = f.getFileName();
				System.out.println(owlfile);
				if (!owlfile.endsWith(".owl") || f.isMissing()) {
					//�ϴ��Ĳ���owl�ļ�	ҳ����תʵ��		��ת�����ҳ��	���ҷ���һ������	 �ոշ�����֪���ǲ��Ϸ���
					resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					resp.setHeader("Location","listKnowware.jsp?session="+cookie+"&errno=owl&filename="+owlfile);
					return;
				}
				//�����owl�ļ� ��˳������ݴ������ݿ��� ����owl��������
				f.saveAs(Config.uploadOwlPath+owlfile,SmartUpload.SAVE_AUTO);
				//�����ж�
				if (!isvalid(realpath+Config.uploadOwlPath + owlfile)) {
					//������Ϸ�
					resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					resp.setHeader("Location","listKnowware.jsp?session="+cookie+"&errno=owl&filename="+owlfile);
					return;
				}
			}
			//����ȷ����Ҫ�����ݿ��в����������
			/**
			 * ���������ݿ�
			 */
			//��owl�������Ϣ��knowware����
			String ele[] = {"knowwareName","description","tag","owl","userName"};	
			String value[] = new String[ele.length];
			String config = "";
			String knowwareName = smart.getRequest().getParameter(ele[0]);
			//
			for(int j=0;j<ele.length-2;j++)
			{
				value[j] = "'"+smart.getRequest().getParameter(ele[j])+"'";
				config += " "+value[j];
				debug(value[j]);
			}
			value[ele.length-2] = "'"+owlfile+"'";
			value[ele.length-1] = "'"+username+"'";
			dbMan.InsertRow(Config.knowware_table, ele, value);
			Vector<String> filelist = new Vector<String>();
			filelist.add(realpath+Config.uploadOwlPath+owlfile);
			/**
			 * ��ÿ���ϴ��ļ����д���   
			 */
			for(int i=1;i<smart.getFiles().getCount();i++){
				org.lxh.smart.File file = smart.getFiles().getFile(i);
				if(file.isMissing())continue;
				
				debug("upload "+smart.getFiles().getFile(i).getFilePathName());
				String filename = file.getFileName();
					//��ͨ�ļ� ���� ���ұ���һ�����ݿ⼴��
					String par[] = new String[]{"knowwareName","fileName"};
					String val[] = new String[2];
					val[0] = "'"+knowwareName+"'";
					val[1] = "'"+knowwareName+"_"+filename+"'";
					dbMan.InsertRow(Config.file_table, par, val);
					file.saveAs(Config.uploadFilesPath+val[1],SmartUpload.SAVE_AUTO);
			}
			String files[] = new String[filelist.size()];
			for(int i = 0; i < files.length; i++){
				files[i] = (String) filelist.get(i);
			}
			//��������ϴ��ļ�������
			String virtualpath = req.getScheme()+"://"
							+ req.getServerName()+":"
							+ req.getServerPort();
			debug(config);
			new IndexFile().IndexFromLocal(virtualpath, realpath, files, config);
		
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
		//ҳ����תʵ��
		resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		resp.setHeader("Location","listKnowware.jsp?session="+cookie);
	
	}
	
	//�ж�owl�Ƿ�����
	private static boolean isvalid(String owlpath) {
		try{
			owlpath = owlpath.replaceAll("\\\\", "/").replaceAll("//", "/");
			debug("owlpath "+owlpath);
			debug("--validity 1--");
			//Model data = FileManager.get().loadModel(fpath + "upload/"+ pathName)
			Model model = ModelFactory.createDefaultModel();
			model.read("file:/"+owlpath);
		  	InfModel infmodel = ModelFactory.createRDFSModel(model);
		  	ValidityReport validity = infmodel.validate();
		  	if(validity.isValid()){
		  		//�����Ч
		   		debug("<h1>��OWL�ļ���������</h1><br/>");
		   		return true;
		  	}
		  	debug("��OWL�ļ�����������");
	  	}catch(Exception e){
	  		//���ﴦ��
	  		debug(e.getMessage());
	  		e.printStackTrace();
	  		debug("<h1>OWL�ļ��������ж��쳣</h1>");
	  	}
		return false;
	}
	//�г��༭����ȫ��֪����
	public static void listEditor(String realpath, MainServlet servlet, DataBaseMan dbMan, 
			String username, HttpServletRequest req, HttpServletResponse resp) {
		
	}
	//�г������ϴ���֪��
	public static void listKnowware(String realpath, MainServlet servlet, DataBaseMan dbMan, 
			String username, HttpServletRequest req, HttpServletResponse resp) {
		String query = "select a.knowwareName as knowwareName, tag, description, owl, fileName from "
				+Config.knowware_table+" a left join "
				+Config.file_table + " b on a.knowwareName = b.knowwareName where userName = '"
				+username+"' order by finaldate desc";
		debug(query);
		dbMan.SetStmt(query);		
		MyResultSet mrs = dbMan.ExecQuery();
		Vector<Knowware> klist = new Vector<Knowware>();
		if (!mrs.first()) {
			debug("no knowware");
		} else {
			String nowKnowwareName = "";
			Knowware knowware = null;
			for (;!mrs.isAfterLast();mrs.next()) {
				//����ÿ��֪�� ����ȡ��Ӧ���ļ� ���ڲ�ѯ���������һ��֪������ļ� 
				//�ж��Ƿ�Ϊͬһ��֪��
				if (nowKnowwareName.compareTo(mrs.getString("knowwareName")) == 0) {
					knowware.file.add(mrs.getString("knowwareName"));
					continue;
				}
				if (knowware != null) klist.add(knowware);
				nowKnowwareName = mrs.getString("knowwareName");
				knowware = new Knowware();
				knowware.knowwareName = nowKnowwareName;
				debug(knowware.knowwareName);
				knowware.description = mrs.getString("description");
				knowware.tag = mrs.getString("tag");
				knowware.owl = mrs.getString("owl");
				if (mrs.getString("fileName") != null) knowware.file.add(mrs.getString("fileName"));
			}
			klist.add(knowware);
		}
		ServerResponse res = new ServerResponse("0", klist);
		try {
			resp.getWriter().write(new Gson().toJson(res));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class Knowware {
	String knowwareName;
	String tag;
	String description;
	String owl;
	Vector<String> file;
	public Knowware() {
		file = new Vector<String>();
	}
}
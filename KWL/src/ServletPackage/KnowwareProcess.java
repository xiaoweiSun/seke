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
		debug("正在处理文件");
		try {
			SmartUpload smart = new SmartUpload();
			smart.initialize(servlet.getServletConfig(), req, resp);
			smart.upload();
			debug("上传参数成功");
		
			String from = smart.getRequest().getParameter("from");
			String owlfile = "";
			//判断owl文件来源于客户端或者是服务端
			if ("server".equals(from)) {
				owlfile = smart.getRequest().getParameter("serverfile");
				//从来源地址判断是否存在 
				String owlorgpath = FileProcess.genPurePath(realpath + Config.graph2OwlPath + owlfile);
				String owldespath = FileProcess.genPurePath(realpath + Config.uploadOwlPath + owlfile);
				File read = new File(owlorgpath);
				debug(owlorgpath);
				debug(owldespath);
				if (!read.exists() || !read.isFile() || !owlfile.endsWith(".owl")) {
					//不是owl文件	页面跳转实现		跳转到结果页面	并且返回一个错误	 刚刚发布的知件是不合法的
					debug(Boolean.toString(read.exists()));
					debug(Boolean.toString(read.isFile()));
					debug(Boolean.toString(owlfile.endsWith(".owl")));
					resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					resp.setHeader("Location","listKnowware.jsp?session="+cookie+"&errno=owl&filename="+owlfile);
					return;				
				}
				//把文件从编辑目录拷贝到上传目录
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
					//上传的不是owl文件	页面跳转实现		跳转到结果页面	并且返回一个错误	 刚刚发布的知件是不合法的
					resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					resp.setHeader("Location","listKnowware.jsp?session="+cookie+"&errno=owl&filename="+owlfile);
					return;
				}
				//如果是owl文件 则顺便把数据存入数据库中 并对owl建立索引
				f.saveAs(Config.uploadOwlPath+owlfile,SmartUpload.SAVE_AUTO);
				//良构判断
				if (!isvalid(realpath+Config.uploadOwlPath + owlfile)) {
					//如果不合法
					resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					resp.setHeader("Location","listKnowware.jsp?session="+cookie+"&errno=owl&filename="+owlfile);
					return;
				}
			}
			//首先确认需要往数据库中插入多少内容
			/**
			 * 保存入数据库
			 */
			//将owl的相关信息和knowware保存
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
			 * 对每个上传文件进行处理   
			 */
			for(int i=1;i<smart.getFiles().getCount();i++){
				org.lxh.smart.File file = smart.getFiles().getFile(i);
				if(file.isMissing())continue;
				
				debug("upload "+smart.getFiles().getFile(i).getFilePathName());
				String filename = file.getFileName();
					//普通文件 保存 并且保存一个数据库即可
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
			//建立针对上传文件的索引
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
		//页面跳转实现
		resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		resp.setHeader("Location","listKnowware.jsp?session="+cookie);
	
	}
	
	//判断owl是否良构
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
		  		//如果有效
		   		debug("<h1>此OWL文件是良构的</h1><br/>");
		   		return true;
		  	}
		  	debug("此OWL文件不是良构的");
	  	}catch(Exception e){
	  		//这里处理
	  		debug(e.getMessage());
	  		e.printStackTrace();
	  		debug("<h1>OWL文件良构性判断异常</h1>");
	  	}
		return false;
	}
	//列出编辑过的全部知件的
	public static void listEditor(String realpath, MainServlet servlet, DataBaseMan dbMan, 
			String username, HttpServletRequest req, HttpServletResponse resp) {
		
	}
	//列出我所上传的知件
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
				//对于每个知件 都获取对应的文件 由于查询结果可能是一个知件多个文件 
				//判断是否为同一个知件
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
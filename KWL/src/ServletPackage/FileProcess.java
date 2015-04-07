package ServletPackage;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.net.ServerResponse;

public class FileProcess {
//	public static void main(String[] args)
//	{
//		String dir = "MyDataBases";
//		System.out.println(listNames(dir));
//	}

	public static String genPurePath(String originalPath) {
		return originalPath.replaceAll("\\\\", "/").replaceAll("//", "/");
	}
	
	public static ArrayList<String> listNames(String dir) {
		ArrayList<String> ret = new ArrayList<String>();
		File f = new File(dir);
		if(!f.exists() || !f.isDirectory())
			return ret;
		File[] files = f.listFiles();
		for(File file : files)
		{
			ret.add(file.getName());
		}
		return ret;
	}
	
}

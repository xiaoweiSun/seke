package ServletPackage;

public class Config {
	public static String mysqlURL = "jdbc:mysql://localhost:3306/kwl_database";
	public static String mysqlDatabase = "kwl_database";
	public static String mysqlUser = "root";
	public static String mysqlPassword = "root";
	
	public static String file_table = "file_list";
	public static String knowware_table = "knowware_list";
	public static String pattern_table = "pattern_profile";
	public static String task_table = "task_profile";
	public static String user_table = "user_profile";
	public static String field_table = "field_list";
	
	public static String uploadFilesPath = "/commonFiles/";
	public static String uploadOwlPath = "/owl/local/";
	public static String onlineOwlPath = "/owl/online/";
	public static String editorGraphPath = "/owl/graph/";
	public static String graph2OwlPath = "/owl/graph2owl/";
	public static String tempOWLDir = "/owl/tempowl/";
	public static String datasetPath = "/dataset/";
	
	public static String fetchFilesPath = "/fetchfiles/";
	
	public static String tdbStateBuild = "building";
	public static String tdbStateProcess = "processing";
	public static String tdbStateComplete = "complete";
	
	public static String repositoryURL = "http://knowware.seforge.org";
	
	public static boolean debug = false;//确定不在debug状态
	public static void D(String str){
		if(debug){
			System.out.println(str);
		}
	}
}

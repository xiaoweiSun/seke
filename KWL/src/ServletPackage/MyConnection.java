package ServletPackage;

import com.mysql.jdbc.Connection;

public class MyConnection {
	public String username;
	public String session;
	public long lastTime;
	public Connection conn;
	public MyConnection(String usName,String Sess, long lastT){
		username = usName+"";
		session = Sess+"";
		lastTime = lastT;
		conn=null;
	};
	
}

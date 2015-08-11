package ServletPackage;

import java.util.HashMap;

public class ConnectionMan {
	public HashMap<String,MyConnection> hMap;
	ConnectionMan(){
		hMap = new HashMap<String,MyConnection>(1000,(float) 0.5);		
	}
	public boolean Put(MyConnection myConn){
		Object reval = hMap.put(myConn.username,myConn);
		return reval==null;		
	}
	public MyConnection Get(String key){
		MyConnection myConn = (MyConnection) hMap.get(key);
		return myConn;		
	}
	public boolean Remove(String key){
		Object reval = hMap.remove(key);
		return reval!=null;		
	}
}

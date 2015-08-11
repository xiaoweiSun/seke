package ServletPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseMan {
	Connection conn;
	Statement stmt;
	String s;
	ReentrantLock lock;
	public DataBaseMan(){
		lock = new ReentrantLock();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
			
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
	        try {
				Connection conn = DriverManager.getConnection(Config.mysqlURL, Config.mysqlUser, Config.mysqlPassword);
				this.conn = conn;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}//这里需要关闭数据库
	private void reCreateConn(){
		System.out.println("---------ReCREATE Conn!!!!!-----------");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
			
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
	        String url = Config.mysqlURL;
	        try {
	        	if (this.conn!=null) this.conn.close();
				Connection conn = DriverManager.getConnection(url,Config.mysqlUser,Config.mysqlPassword);
				
				this.conn = conn;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	};
	public boolean SetStmt(String _s){
		lock.lock();
		try{
		if (conn==null||conn.isClosed()) reCreateConn();
		stmt = conn.createStatement();
		s = _s;
		return true;
		}catch(SQLException e){
			System.err.println("DataBaseMan:SetStmt Error "+e.getMessage());
			return false;
		}
	}
	public	MyResultSet ExecQuery(){
		try{
		ResultSet rst = stmt.executeQuery(s);
		MyResultSet myrs = new MyResultSet(rst);
//		System.out.println(s);
		lock.unlock();
		return myrs;
		}catch(SQLException e){
			System.err.println("DataBaseMan:ExecQuery Error "+e.getMessage());
			MyResultSet myrs = new MyResultSet(null);
			lock.unlock();
			return myrs;
		}
	}
	public boolean ExecUpdate(){
		try{
//			System.out.println(s);
			int reVal = stmt.executeUpdate(s);
			lock.unlock();
			return reVal>0;
			}catch(SQLException e){
				System.err.println("DataBaseMan:ExecUpdate Error "+e.getMessage());
				lock.unlock();
				return 0>0;
			}
	}
	/*
	 * Exec insert into t1(zd1,zd2..) values(v1,v2..);
	 * */
	public boolean InsertRow(String tableName,String[] zd,String[] values){
		int i;
		String s = "insert into "+tableName+"(";
		for (i=0;i<zd.length;i++)
			s+=(zd[i]+(i<zd.length-1?",":""));
		s+=") values(";
		for (i=0;i<values.length;i++)
			s+=(values[i]+(i<values.length-1?",":""));
		s+=");";
		this.SetStmt(s);
		return this.ExecUpdate();		
	}
	/*
	 * Exec delete from t1 where condition;
	 * */
	public boolean Delete(String tableName,String cond){
		int i;
		String s = "delete from "+tableName+" where "+cond+";";
		this.SetStmt(s);
		return this.ExecUpdate();		
	}
	/*
	 * Select [] from [table] where []
	 * */
	public MyResultSet SelectItemOnCond(String tableName, String[] id, String[] cd){
		String sql = "select ";
		for(int i = 0; i < id.length-1; i++){
			sql += id[i] + ",";
		}
		sql += id[id.length-1];
		sql += " from " + tableName;
		sql += " where ";
		for(int i = 0; i < cd.length-1; i++){
			sql += cd[i]+" and ";
		}
		sql += cd[cd.length-1];
		this.SetStmt(sql);
		return this.ExecQuery();
	}
	/*
	 * Exec select * from t1;
	 * */
	public MyResultSet SelectAll(String tableName){
		this.SetStmt("select * from "+tableName+";");
		return this.ExecQuery();
	}
	/*
	 * Exec select * from t1 where condition;
	 * */
	public MyResultSet SelectCond(String tableName,String cond){
		this.SetStmt("select * from "+tableName+" where "+cond+";");
		return this.ExecQuery();
	}
	public boolean UpdatePsw(String username, String psw) {
		this.SetStmt("update user Set psw='"+psw+"' where username='"+username+"';");
		return this.ExecUpdate();
	}
	
	public MyResultSet SelectTables(String tableName) {
		try{
			conn.getMetaData().getTables(Config.mysqlDatabase, null, tableName, new String[]{"TABLE"});
			ResultSet rst = stmt.executeQuery(s);
			MyResultSet myrs = new MyResultSet(rst);
	//		System.out.println(s);
			lock.unlock();
			return myrs;
		}catch(SQLException e){
			System.err.println("DataBaseMan:SelectTables Error "+e.getMessage());
			MyResultSet myrs = new MyResultSet(null);
			lock.unlock();
			return myrs;
		}
	}

}

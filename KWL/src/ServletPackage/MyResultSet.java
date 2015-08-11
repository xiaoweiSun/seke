package ServletPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MyResultSet {
	public int total;
	private int column;
	private int row;
	private int currentrow;
	public String content[][];
	public Map<String,Integer> string2int;
	public MyResultSet(ResultSet rs){
		if(rs==null){row=-1;}
		else{
			content = null;
			row = -1;
			currentrow = 0;
			total = 0;
			refresh(rs);
			}
		
	}
	
	private void refresh(ResultSet rs){
		try {
				if (!rs.first()){
					System.out.println("DisplayRs End----"+"---------------");
					return;
				}
				column = rs.getMetaData().getColumnCount();
				rs.last();
				content = new String[rs.getRow()][];
				rs.first();
				row = 0;
				currentrow = 0;
				total = 0;
				//
				string2int = new HashMap<String, Integer>();
				for (int i=1;i<=column;i++){
					string2int.put(rs.getMetaData().getColumnName(i), i);	
				}
				//
				for (;!rs.isAfterLast();rs.next()){
					System.out.print(row+" "+column);
					content[row] = new String[column+1];
					for (int i=1;i<=column;i++){
						content[row][i] = rs.getString(i);	
					}	
					row ++;
					total++;
				}
				row -=1;
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
	}
	public int getColumnCount(){return column;}
	public boolean first(){currentrow=0;return row>=0;}
	public boolean isAfterLast(){return currentrow>row;}
	public void next(){currentrow++;}
	public String getString(int index){
		System.out.print(" "+currentrow+"-"+row+"-"+index+" ");
		return content[currentrow][index];
	}
	public String getString(String columnName) {
		//这里需要添加针对字符串 查询结果
		return content[currentrow][string2int.get(columnName)];
	}
	public void close(){}
	
}


package models;

import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.db.*;

class DatabaseWrapper{

	protected Database db;
	
    public DatabaseWrapper(Database db) {
        this.db = db;
    }

	public void registerMonitor(){
		
	}
    
    public JSONArray getAllMonitors() throws Exception{
		return executeQuery("select * from monitors join personaldata on monitors.id = personaldata.id");
	}
    
    protected void executeInsert(String sql, String[] values, String errorMessage) throws Exception{
		Connection conn = null;
		try{
			conn = db.getConnection();
			Statement statement = conn.createStatement();
		    sql = addValues(values, sql);
		    int result = statement.executeUpdate(sql);
		    if(result == 0){
		    	throw new Exception(errorMessage);
		    }
		}
		finally{
			closeConnection(conn);
		}
	}

	protected JSONArray executeQuery(String sql) throws Exception{
		Connection conn = null;
		try{
		    conn = db.getConnection();
			Statement statement = conn.createStatement();
		    ResultSet result = statement.executeQuery(sql);
		    String[] columnNames = getColumnNames(result);
		    JSONArray jsonArray = new JSONArray();
		    while(result.next()){
		    	JSONObject jsonObject = new JSONObject();
		    	for(int i = 0; i < columnNames.length; i++){
					jsonObject.put(columnNames[i], result.getString(columnNames[i]));
				}
		    	jsonArray.add(jsonObject);
		    }
		    return jsonArray;
		}
		finally{
			closeConnection(conn);
		}
	}
	
	private String[] getColumnNames(ResultSet result) throws Exception{
		ResultSetMetaData metaData = result.getMetaData();
		int count = metaData.getColumnCount();
		String columnNames[] = new String[count];
		for (int i = 1; i <= count; i++){
		   columnNames[i-1] = metaData.getColumnLabel(i); 
		}
		
		return columnNames;
	}
	
	private String addValues(String[] args, String sql){
		for(int i = 0; i < args.length; i++){
			String arg = args[i];
			if(arg == null || arg.isEmpty()){
				sql += "null";
			}
			else{
				sql += "'" + arg + "'"; 
			}
			if(i == (args.length - 1)){
				sql += ");";
			}
			else{
				sql += ",";
			}
		}
		return sql;
	}
	
	private void closeConnection(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

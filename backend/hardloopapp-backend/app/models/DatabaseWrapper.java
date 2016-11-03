package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.db.*;

class DatabaseWrapper{

	private Database db;
	
    public DatabaseWrapper(Database db) {
        this.db = db;
    }
    
    public JSONArray getAllMonitors() throws Exception{
		return executeQuery("select monitors.id, personalData_id, accessLevel, firstName, lastName, phoneNumber, username from monitors join personaldata on monitors.personalData_id = personaldata.id");
	}
    
    public JSONArray getAllClients() throws Exception{
		return executeQuery("select clients.id, personalData_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id");
	}
    
    public void registerClient(String firstName, String lastName, String phoneNumber, String username, String password) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = addValues("insert into personaldata values(0,", values);
		sql += "insert into clients values(0, last_insert_id());";
		executeUpdate(sql, "Could not register client.");
	}
    
    public void removeUser(String personalDataId) throws Exception{
		executeUpdate("delete from personaldata where id = " + personalDataId, "No user found to remove.");
	}
    
    public void assignClientToMonitor(String monitorId, String clientId, String monitorNumber) throws Exception{
		String[] values = {monitorId, clientId, monitorNumber};
		String sql = addValues("insert into monitors_clients values(", values);
		executeUpdate(sql, "Could not assign client to monitor.");
	}
    
    private void executeUpdate(String sql, String errorMessage) throws Exception{
		Connection conn = null;
		try{
			conn = db.getConnection();
		    if(conn.createStatement().executeUpdate(sql) == 0){
		    	throw new Exception(errorMessage);
		    }
		}
		finally{
			closeConnection(conn);
		}
	}
	
	private JSONArray executeQuery(String sql) throws Exception{
		Connection conn = null;
		try{
		    conn = db.getConnection();
		    return resultToJSONArray(conn.createStatement().executeQuery(sql));
		}
		finally{
			closeConnection(conn);
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray resultToJSONArray(ResultSet result) throws Exception{
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
	
	private String[] getColumnNames(ResultSet result) throws Exception{
		ResultSetMetaData metaData = result.getMetaData();
		int count = metaData.getColumnCount();
		String columnNames[] = new String[count];
		for (int i = 1; i <= count; i++){
		   columnNames[i-1] = metaData.getColumnLabel(i); 
		}
		return columnNames;
	}
	
	private String addValues(String sql, String[] args){
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

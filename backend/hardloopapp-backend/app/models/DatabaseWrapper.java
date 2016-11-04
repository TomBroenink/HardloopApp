package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.db.*;

class DatabaseWrapper{

	protected Database db;
	
    public DatabaseWrapper(Database db) {
        this.db = db;
    }
    
    public JSONArray getAllMonitors() throws Exception{
		return executeQuery("select monitors.id, personalData_id, accessLevel, firstName, lastName, phoneNumber, username from monitors join personaldata on monitors.personalData_id = personaldata.id");
	}
    
    public JSONArray getAllClients() throws Exception{
		return executeQuery("select clients.id, personalData_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id");
	}
    
    public int registerClient(String firstName, String lastName, String phoneNumber, String username, String password) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = addValues("insert into personaldata values(0,", values);
		sql += "insert into clients values(0, last_insert_id());";
		return executeInsertReturnId(sql, "Could not register client.");
	}
    
    public int registerMonitor(String firstName, String lastName, String phoneNumber, String username, String password, String accessLevel) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = addValues("insert into personaldata values(0,", values);
		sql += "insert into monitors values(0, last_insert_id(), '" + accessLevel + "');";
		return executeInsertReturnId(sql, "Could not register monitor.");
	}
    
    public void removeUser(String personalDataId) throws Exception{
		executeUpdate("delete from personaldata where id = " + personalDataId, "No user found to remove.");
	}
    
    public void assignClientToMonitor(String monitorId, String clientId, String monitorNumber) throws Exception{
		String[] values = {monitorId, clientId, monitorNumber};
		String sql = addValues("insert into monitors_clients values(", values);
		executeUpdate(sql, "Could not assign client to monitor.");
	}
    
    public int createRunSchema(String name, String description) throws Exception{
		String[] values = {name, description};
		String sql = addValues("insert into runschemas values(0,", values);
		return executeInsertReturnId(sql, "Could not create run schema.");
	}
    
    public void assignRunSchemaToClient(String clientId, String runSchemaId) throws Exception{
		String[] values = {clientId, runSchemaId};
		String sql = addValues("insert into clients_runschemas values(", values);
		executeUpdate(sql, "Could not assign run schema to client.");
	}
    
    public void assignRunToRunSchema(String runSchemaId, String runId, String day, String time) throws Exception{
		String[] values = {runSchemaId, runId, day, time};
		String sql = addValues("insert into runschemas_runs values(", values);
		executeUpdate(sql, "Could not assign run to run schema.");
	}
    
    public int createRun(String name, String description, String routeId) throws Exception{
		String[] values = {name, description, routeId};
		String sql = addValues("insert into runs values(0,", values);
		return executeInsertReturnId(sql, "Could not create run.");
	}
    
    public void createRoute(String distance, String[][] coordinates) throws Exception{
    	
	}
    
    private int executeInsertReturnId(String sql, String errorMessage) throws Exception{
		Connection conn = null;
		try{
			conn = db.getConnection();
			Statement statement = conn.createStatement();
			boolean results = statement.execute(sql + " select last_insert_id() as id;");
			int count = 0;
			int id = 0;
			while(results || count != -1){
				if(results){
					ResultSet rs = statement.getResultSet();
					rs.next();
			        id = rs.getInt("id");
			    }
				else{
					count = statement.getUpdateCount();
					if(count == 0){
						throw new Exception(errorMessage);
					}
			    }
			    results = statement.getMoreResults();
			}
			return id;
		}
		finally{
			closeConnection(conn);
		}
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

	protected JSONArray executeQuery(String sql) throws Exception{
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

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
    	String sql = "select monitors.id, personalData_id, accessLevel, firstName, lastName, phoneNumber, username from monitors join personaldata on monitors.personalData_id = personaldata.id";
		return executeQuery(sql, "Could not retrieve monitors.");
	}
    
    public JSONArray getAllClients() throws Exception{
    	String sql = "select clients.id, personalData_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id";
		return executeQuery(sql, "Could not retrieve clients.");
	}
    
    public int registerClient(String firstName, String lastName, String phoneNumber, String username, String password) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = addValues("insert into personaldata values(0,", values);
		sql += "; insert into clients values(0, last_insert_id());";
		return executeInsertReturnId(sql, "Could not register client.", db.getConnection(), false, true, true);
	}
    
    public int registerMonitor(String firstName, String lastName, String phoneNumber, String username, String password, String accessLevel) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = addValues("insert into personaldata values(0,", values);
		sql += "; insert into monitors values(0, last_insert_id(), '" + accessLevel + "');";
		return executeInsertReturnId(sql, "Could not register monitor.", db.getConnection(), false, true, true);
	}
    
    public void removeUser(String personalDataId) throws Exception{
		executeUpdate("delete from personaldata where id = " + personalDataId, "No user found to remove.", db.getConnection(), true, true, true);
	}
    
    public void assignClientToMonitor(String monitorId, String clientId, String monitorNumber) throws Exception{
		String[] values = {monitorId, clientId, monitorNumber};
		String sql = addValues("insert into monitors_clients values(", values);
		executeUpdate(sql, "Could not assign client to monitor.", db.getConnection(), true, true, true);
	}
    
    public int createRunSchema(String name, String description) throws Exception{
		String[] values = {name, description};
		String sql = addValues("insert into runschemas values(0,", values);
		return executeInsertReturnId(sql, "Could not create run schema.", db.getConnection(), true, true, true);
	}
    
    public void assignRunSchemaToClient(String clientId, String runSchemaId) throws Exception{
		String[] values = {clientId, runSchemaId};
		String sql = addValues("insert into clients_runschemas values(", values);
		executeUpdate(sql, "Could not assign run schema to client.", db.getConnection(), true, true, true);
	}
    
    public void assignRunToRunSchema(String runSchemaId, String runId, String day, String time) throws Exception{
		String[] values = {runSchemaId, runId, day, time};
		String sql = addValues("insert into runschemas_runs values(", values);
		executeUpdate(sql, "Could not assign run to run schema.", db.getConnection(), true, true, true);
	}
    
    public int createRun(String name, String description, String distance, JSONArray route) throws Exception{
    	Connection conn = null;
    	ResultSet rs = null;
		Statement statement = null;
    	int runId = 0;
    	try{
    		conn = db.getConnection();
    		String sql = addValues("insert into routes values(0,", new String[]{distance}) + ";";
    		int routeId = executeInsertReturnId(sql, "Could not create route.", conn, false, false, false);
    		sql = "insert into coordinates values(0,";
    		String[] values = new String[2];
    		for(int i = 0; i < route.size(); i++){
    			JSONArray coordinates = (JSONArray) route.get(i);
    			values[0] = (String) coordinates.get(0);
    			values[1] = (String) coordinates.get(1);
    			sql = addValues(sql, values);
    			if(i < (route.size() - 1)){
    				sql += ", (0,";
    			}
    		}
    		executeUpdate(sql + ";", "Could not create coordinates.", conn, false, false, false);
    		statement = conn.createStatement();
    		rs = statement.executeQuery("select last_insert_id() as endId, row_count() as insertCount;");
    		rs.next();
    		int endId = rs.getInt("endId");
    		System.out.println("EndId: " + endId);
    		System.out.println("InsertCount: " + rs.getInt("insertCount"));
    		int startId = endId - rs.getInt("insertCount") + 1;
    		sql = "insert into routes_coordinates values(";
    		for(int i = startId; i <= endId; i++){
				sql += routeId + ", " + i + ", " + (i - startId + 1) + ")";
				if(i <= (endId - 1)){
    				sql += ", (";
    			}
			}
    		System.out.println(sql);
    		executeUpdate(sql + ";", "Could not create coordinates for route.", conn, false, false, false);
    		sql = addValues("insert into runs values(0,", new String[]{name, description, String.valueOf(routeId)}) + ";";
    		runId = executeInsertReturnId(sql, "Could not create run.", conn, false, false, true);
    		return runId;
		}
    	catch(Exception e){
    		conn.rollback();
    		e.printStackTrace();
    		throw e;
    	}
    	finally{
    		closeResultSet(rs);
    		closeStatement(statement);
			closeConnection(conn);
		}
	}
    
    private int executeInsertReturnId(String sql, String errorMessage) throws Exception{
    	return executeInsertReturnId(sql, errorMessage, db.getConnection(), true, true, true);
    }
    
    private int executeInsertReturnId(String sql, String errorMessage, Connection conn, boolean autoCommit, boolean closeConnection, boolean commit) throws Exception{
		ResultSet rs = null;
		Statement statement = null;
    	try{
			conn.setAutoCommit(autoCommit);
			statement = conn.createStatement();
			if(statement.executeUpdate(sql) == 0){
				throw new Exception("Update count == 0.");
			}
			rs = statement.executeQuery("select last_insert_id() as id;");
			rs.next();
			int id = rs.getInt("id");
			if(!autoCommit && commit){
				conn.commit();
			}
			return id;
		}
		catch(Exception e){
			if(!autoCommit){
				conn.rollback();;
			}
			e.printStackTrace();
			throw new Exception(errorMessage);
		}
		finally{
			closeResultSet(rs);
			closeStatement(statement);
			if(closeConnection){
				closeConnection(conn);
			}
		}
	}
    
    private void executeUpdate(String sql, String errorMessage) throws Exception{
    	executeUpdate(sql, errorMessage, db.getConnection(), true, true, true);
    }
    
    private void executeUpdate(String sql, String errorMessage, Connection conn, boolean autoCommit, boolean closeConnection, boolean commit) throws Exception{
    	Statement statement = null;
    	try{
			conn.setAutoCommit(autoCommit);
			statement = conn.createStatement();
		    if(statement.executeUpdate(sql) == 0){
		    	throw new Exception("Update count == 0.");
		    }
		    if(!autoCommit && commit){
				conn.commit();
			}
		}
		catch(Exception e){
			if(!autoCommit){
				conn.rollback();;
			}
			e.printStackTrace();
			throw new Exception(errorMessage);
		}
		finally{
			closeStatement(statement);
			if(closeConnection){
				closeConnection(conn);
			}
		}
	}
	
	private JSONArray executeQuery(String sql, String errorMessage) throws Exception{
		Connection conn = null;
		ResultSet rs = null;
		Statement statement = null;
		try{
			conn = db.getConnection();
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
		    return resultToJSONArray(rs);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new Exception(errorMessage);
		}
		finally{
			closeResultSet(rs);
			closeStatement(statement);
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
				sql += ")";
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
	
	private void closeResultSet(ResultSet rs){
		if(rs != null){
			try{
				rs.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void closeStatement(Statement statement){
		if(statement != null){
			try{
				statement.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

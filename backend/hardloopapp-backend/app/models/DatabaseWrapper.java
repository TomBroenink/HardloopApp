package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.db.*;

abstract class DatabaseWrapper{

	protected Database db;
	
    public DatabaseWrapper(Database db) {
        this.db = db;
    }

    protected int executeInsertReturnId(String sql, String errorMessage) throws Exception{
    	return executeInsertReturnId(sql, errorMessage, db.getConnection(), true, true, true);
    }
    
    protected int executeInsertReturnId(String sql, String errorMessage, Connection conn, boolean autoCommit, boolean closeConnection, boolean commit) throws Exception{
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
    
    protected void executeUpdate(String sql, String errorMessage) throws Exception{
    	executeUpdate(sql, errorMessage, db.getConnection(), true, true, true);
    }
    
    protected void executeUpdate(String sql, String errorMessage, Connection conn, boolean autoCommit, boolean closeConnection, boolean commit) throws Exception{
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
	
	protected JSONArray executeQuery(String sql, String errorMessage) throws Exception{
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
	protected JSONArray resultToJSONArray(ResultSet result) throws Exception{
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
	
	protected String[] getColumnNames(ResultSet result) throws Exception{
		ResultSetMetaData metaData = result.getMetaData();
		int count = metaData.getColumnCount();
		String columnNames[] = new String[count];
		for (int i = 1; i <= count; i++){
		   columnNames[i-1] = metaData.getColumnLabel(i); 
		}
		return columnNames;
	}
	
	protected String addValues(String sql, String[] args){
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
	
	protected void closeConnection(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	protected void closeResultSet(ResultSet rs){
		if(rs != null){
			try{
				rs.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	protected void closeStatement(Statement statement){
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

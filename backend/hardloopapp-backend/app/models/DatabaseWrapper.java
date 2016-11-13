package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    abstract int create(JSONObject args) throws Exception;
    
    abstract void delete(int id) throws Exception;
    
    abstract JSONArray getAll() throws Exception;
    
    protected int executeInsertReturnId(String sql, String[] values, String errorMessage) throws Exception{
    	return executeInsertReturnId(sql, values, errorMessage, db.getConnection(), true, true, true);
    }
    
    protected int executeInsertReturnId(String sql, String[] values, String errorMessage, Connection conn, boolean autoCommit, boolean closeConnection, boolean commit) throws Exception{
    	ResultSet rs = null;
    	PreparedStatement statement = null;
    	try{
			conn.setAutoCommit(autoCommit);
			statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			addValues(statement, values);
			if(statement.executeUpdate() == 0){
				throw new Exception("Update count == 0.");
			}
			int id = 0;
			rs = statement.getGeneratedKeys();
			if(rs.next()){
                id = rs.getInt(1);
            }
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
    
    protected void executeUpdate(String sql, String[] values, String errorMessage) throws Exception{
    	executeUpdate(sql, values, errorMessage, db.getConnection(), true, true, true);
    }
    
    protected void executeUpdate(String sql, String[] values, String errorMessage, Connection conn, boolean autoCommit, boolean closeConnection, boolean commit) throws Exception{
    	PreparedStatement statement = null;
    	try{
			conn.setAutoCommit(autoCommit);
			statement = conn.prepareStatement(sql);
			addValues(statement, values);
		    if(statement.executeUpdate() == 0){
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
	
	protected JSONArray executeQuery(String sql, String[] values, String errorMessage) throws Exception{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		try{
			conn = db.getConnection();
			statement = conn.prepareStatement(sql);
			addValues(statement, values);
			rs = statement.executeQuery();
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
	
	protected void addValues(PreparedStatement statement, String[] values) throws Exception{
		if(values != null){
			for(int i = 0; i < values.length; i++){
				statement.setString(i + 1, values[i]);
			}
		}
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

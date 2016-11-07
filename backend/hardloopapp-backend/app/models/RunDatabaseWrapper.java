package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;

import play.db.Database;

public class RunDatabaseWrapper extends DatabaseWrapper{
	
	public RunDatabaseWrapper(Database db) {
		super(db);
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
    		executeUpdate(sql + ";", "Could not create coordinates.", conn, false, false, true);
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
}

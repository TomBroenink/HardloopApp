package models;

import java.sql.Connection;

import org.json.simple.JSONArray;

import play.db.Database;

public class RunDatabaseWrapper extends DatabaseWrapper{
	
	public RunDatabaseWrapper(Database db) {
		super(db);
	}

	public int createRun(String name, String description, String distance, JSONArray route) throws Exception{
    	Connection conn = null;
    	try{
    		conn = db.getConnection();
    		int routeId = createRoute(conn, distance);
    		createCoordinates(conn, route);
    		int[] insertIdRange = super.getInsertIdRange(conn);
    		insertCoordinatesForRoute(conn, insertIdRange[0], insertIdRange[1], routeId);
    		return createRun(conn, name, description, routeId);
		}
    	catch(Exception e){
    		conn.rollback();
    		e.printStackTrace();
    		throw new Exception("Failed to create run.");
    	}
    	finally{
			closeConnection(conn);
		}
	}
	
	private int createRoute(Connection conn, String distance) throws Exception{
		String sql = addValues("insert into routes values(0,", new String[]{distance}) + ";";
		return executeInsertReturnId(sql, "Failed to create route.", conn, false, false, false);
	}
	
	private void createCoordinates(Connection conn, JSONArray route) throws Exception{
		String sql = "insert into coordinates values(0,";
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
		executeUpdate(sql + ";", "Failed to create coordinates.", conn, false, false, false);
	}
	
	private void insertCoordinatesForRoute(Connection conn, int startId, int endId, int routeId) throws Exception{
		String sql = "insert into routes_coordinates values(";
		for(int i = startId; i <= endId; i++){
			sql += routeId + ", " + i + ", " + (i - startId + 1) + ")";
			if(i <= (endId - 1)){
				sql += ", (";
			}
		}
		executeUpdate(sql + ";", "Failed to create coordinates for route.", conn, false, false, false);
	}
	
	private int createRun(Connection conn, String name, String description, int routeId) throws Exception{
		String sql = addValues("insert into runs values(0,", new String[]{name, description, String.valueOf(routeId)}) + ";";
		return executeInsertReturnId(sql, "Failed to create run.", conn, false, false, true);
	}
}

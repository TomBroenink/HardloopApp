package models;

import java.sql.Connection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import play.db.Database;

public class RunDatabaseWrapper extends DatabaseWrapper{
	
	public RunDatabaseWrapper(Database db) {
		super(db);
	}

	@Override
	public int create(JSONObject args) throws Exception{
    	Connection conn = null;
    	try{
    		conn = db.getConnection();
    		int routeId = createRoute(conn, (String) args.get("distance"));
    		int[] idRange = createCoordinates(conn, (JSONArray) args.get("route"));
    		insertCoordinatesForRoute(conn, idRange[0], idRange[1], routeId);
    		return createRun(conn, (String) args.get("name"), (String) args.get("description"), routeId);
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
	
	@Override
	public void delete(int id) throws Exception{
		throw new Exception("Method not implemented.");
	}
	
	@Override
	public JSONArray getAll() throws Exception{
		String sql = "select runs.id, name, description, routes_id, distance from runs join routes on runs.routes_id = routes.id;";
		return super.executeQuery(sql, null, "Failed to retrieve runs.");
	}
	
	private int createRoute(Connection conn, String distance) throws Exception{
		return executeInsertReturnId("insert into routes values(0,?);", new String[]{distance}, "Failed to create route.", conn, false, false, false);
	}
	
	private int[] createCoordinates(Connection conn, JSONArray route) throws Exception{
		String sql = "insert into coordinates values ";
		String[] values = new String[route.size() * 2];
		for(int i = 0; i < route.size(); i++){
			JSONArray coordinates = (JSONArray) route.get(i);
			values[i * 2] = (String) coordinates.get(0);
			values[i * 2 + 1] = (String) coordinates.get(1);
			sql += "(0,?,?),";
		}
		sql = sql.substring(0, sql.length() - 1);
		int startId = super.executeInsertReturnId(sql + ";", values, "Failed to create coordinates.", conn, false, false, false);
		return new int[]{startId, startId + route.size() - 1};
	}
	
	private void insertCoordinatesForRoute(Connection conn, int startId, int endId, int routeId) throws Exception{
		String sql = "insert into routes_coordinates values ";
		String[] values = new String[(endId - startId + 1) * 3];
		for(int i = startId; i <= endId; i++){
			values[(i - startId) * 3] = String.valueOf(routeId);
			values[(i - startId) * 3 + 1] = String.valueOf(i);
			values[(i - startId) * 3 + 2] = String.valueOf(i - startId + 1);
			sql += "(?,?,?),";
		}
		sql = sql.substring(0, sql.length() - 1);
		executeUpdate(sql + ";", values, "Failed to create coordinates for route.", conn, false, false, false);
	}
	
	private int createRun(Connection conn, String name, String description, int routeId) throws Exception{
		String[] values = {name, description, String.valueOf(routeId)};
		return executeInsertReturnId("insert into runs values(0,?,?,?);", values, "Failed to create run.", conn, false, false, true);
	}
}

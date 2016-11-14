package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import play.db.Database;

public class RunSchemaDatabaseWrapper extends DatabaseWrapper{
	
	public RunSchemaDatabaseWrapper(Database db) {
		super(db);
	}
	
	@Override
	public int create(JSONObject args) throws Exception{
		String[] values = {(String) args.get("name"), (String) args.get("description")};
		String sql = "insert into runschemas values(0,?,?);";
		return super.executeInsertReturnId(sql, values, "Failed to create run schema.");
	}
	
	@Override
	public void delete(int id) throws Exception{
		throw new Exception("Method not implemented.");
	}
	
	@Override
	public JSONArray getAll() throws Exception{
		String sql = "select * from runSchemas;";
		return super.executeQuery(sql, null, "Failed to retrieve run schemas.");
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getRunSchemaById(String id) throws Exception{
		String sql = "select * from runschemas where id = ?;";
		JSONObject runSchema = (JSONObject) super.executeQuery(sql, new String[]{id}, "Failed to retrieve run schema.").get(0);
		runSchema.put("runs", getRunsForRunSchema(id));
		return runSchema;
	}
	
	public void assignRunToRunSchema(String runSchemaId, String runId, String day, String time) throws Exception{
		String[] values = {runSchemaId, runId, day, time};
		String sql = "insert into runschemas_runs values(?,?,?,?);";
		super.executeUpdate(sql, values, "Failed to assign run to run schema.");
	}
	
	public void deleteRunFromRunSchema(String runSchemaId, String runId) throws Exception{
		String[] values = {runSchemaId, runId};
		String sql = "delete from runschemas_runs where runSchemas_id = ? and runs_id = ?;";
		executeUpdate(sql, values, "Failed to delete run from run schema.");
	}
	
	public JSONArray getRunsForRunSchema(String runSchemaId) throws Exception{
		String sql = "select time, day, runs_id, name, description, routes_id, distance from runschemas_runs join runs on runschemas_runs.runs_id = runs.id join routes on runs.routes_id = routes.id where runSchemas_id = ? order by day, time;";
		return super.executeQuery(sql, new String[]{runSchemaId}, "Failed to retrieve runs for run schema.");
	}
}

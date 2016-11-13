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
		throw new Exception("Method not implemented.");
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
}

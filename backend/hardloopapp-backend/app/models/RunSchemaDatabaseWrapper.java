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
		String sql = super.addValues("insert into runschemas values(0,", values);
		return super.executeInsertReturnId(sql, "Failed to create run schema.");
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
		String sql = super.addValues("insert into runschemas_runs values(", values);
		super.executeUpdate(sql, "Failed to assign run to run schema.");
	}
	
	public void deleteRunFromRunSchema(String runSchemaId, String runId) throws Exception{
		executeUpdate("delete from runschemas_runs where runSchemas_id = '" + runSchemaId + "' and runs_id = '" + runId + "'", "Failed to delete run from run schema.");
	}
}

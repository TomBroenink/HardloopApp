package models;

import play.db.Database;

public class RunSchemaDatabaseWrapper extends DatabaseWrapper{
	
	public RunSchemaDatabaseWrapper(Database db) {
		super(db);
	}
	
	public int createRunSchema(String name, String description) throws Exception{
		String[] values = {name, description};
		String sql = super.addValues("insert into runschemas values(0,", values);
		return super.executeInsertReturnId(sql, "Failed to create run schema.");
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

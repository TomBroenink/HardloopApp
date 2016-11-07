package models;

import play.db.Database;

public class RunSchemaDatabaseWrapper extends DatabaseWrapper{
	
	public RunSchemaDatabaseWrapper(Database db) {
		super(db);
	}
	
	public int createRunSchema(String name, String description) throws Exception{
		String[] values = {name, description};
		String sql = super.addValues("insert into runschemas values(0,", values);
		return super.executeInsertReturnId(sql, "Could not create run schema.");
	}
	
	public void assignRunToRunSchema(String runSchemaId, String runId, String day, String time) throws Exception{
		String[] values = {runSchemaId, runId, day, time};
		String sql = super.addValues("insert into runschemas_runs values(", values);
		super.executeUpdate(sql, "Could not assign run to run schema.");
	}
}

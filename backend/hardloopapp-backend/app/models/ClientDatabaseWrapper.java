package models;

import org.json.simple.JSONArray;

import play.db.Database;

public class ClientDatabaseWrapper extends DatabaseWrapper{

	public ClientDatabaseWrapper(Database db) {
		super(db);
	}
	
	public JSONArray getAllClients() throws Exception{
    	String sql = "select clients.id, personalData_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id";
		return super.executeQuery(sql, "Failed to retrieve clients.");
	}
	
	public int registerClient(String firstName, String lastName, String phoneNumber, String username, String password) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = super.addValues("insert into personaldata values(0,", values);
		sql += "; insert into clients values(0, last_insert_id());";
		return super.executeInsertReturnId(sql, "Failed to register client.", db.getConnection(), false, true, true);
	}
	
	public void assignRunSchemaToClient(String clientId, String runSchemaId) throws Exception{
		String[] values = {clientId, runSchemaId};
		String sql = super.addValues("insert into clients_runschemas values(", values);
		super.executeUpdate(sql, "Failed to assign run schema to client.");
	}
	
	public void deleteRunSchemaFromClient(String clientId, String runSchemaId) throws Exception{
		executeUpdate("delete from clients_runschemas where runSchemas_id = '" + runSchemaId + "' and clients_id = '" + clientId + "'", "Failed to delete run schema from client.");
	}
	
	public JSONArray getAchievementsForClient(String clientId) throws Exception{
    	String sql = "select id, `name`, description, `condition` from clients_achievements join achievements on clients_achievements.achievements_id = achievements.id where clients_id = '" + clientId + "'";
		return super.executeQuery(sql, "Failed to retrieve achievements for client.");
	}
	
	public JSONArray getRunSchemasForClient(String clientId) throws Exception{
    	String sql = "select id, `name`, description from clients_runschemas join runschemas on clients_runschemas.runSchemas_id = runschemas.id where clients_id = '" + clientId + "'";
		return super.executeQuery(sql, "Failed to retrieve run schemas for client.");
	}
}

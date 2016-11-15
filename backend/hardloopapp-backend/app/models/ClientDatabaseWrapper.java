package models;

import java.sql.Connection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import play.db.Database;

public class ClientDatabaseWrapper extends PersonDatabaseWrapper{

	public ClientDatabaseWrapper(Database db) {
		super(db);
	}
	
	@Override
	public int create(JSONObject args) throws Exception{
		String[] values = {(String) args.get("firstName"), (String) args.get("lastName"), (String) args.get("phoneNumber"), (String) args.get("username"), (String) args.get("password")};
		String sql = "insert into personaldata values(0,?,?,?,?,?);";
		Connection conn =  db.getConnection();
		int id = super.executeInsertReturnId(sql, values, "Failed to register client.", conn, false, false, false);
		sql = "insert into clients values(0, " + id + ", ?)";
		return super.executeInsertReturnId(sql, new String[]{(String) args.get("careProfileId")}, "Failed to register client.", conn, false, true, true);
	}
	
	@Override
	public JSONArray getAll() throws Exception{
    	String sql = "select clients.id, personalData_id, care_profile_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id";
		return super.executeQuery(sql, null, "Failed to retrieve clients.");
	}
	
	@Override
	public JSONObject getById(String id) throws Exception{
		String sql = "select clients.id, personalData_id, care_profile_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id where clients.id = ?;";
		return (JSONObject) super.executeQuery(sql, new String[]{id}, "Failed to retrieve client.").get(0);
	}
	
	public void assignRunSchemaToClient(String clientId, String runSchemaId) throws Exception{
		String[] values = {clientId, runSchemaId};
		String sql = "insert into clients_runschemas values(?,?);";
		super.executeUpdate(sql, values, "Failed to assign run schema to client.");
	}
	
	public void deleteRunSchemaFromClient(String clientId, String runSchemaId) throws Exception{
		String[] values = {runSchemaId, clientId};
		String sql = "delete from clients_runschemas where runSchemas_id = ? and clients_id = ?;";
		executeUpdate(sql, values, "Failed to delete run schema from client.");
	}
	
	public JSONArray getAchievementsForClient(String clientId) throws Exception{
    	String sql = "select id, `name`, description, `condition` from clients_achievements join achievements on clients_achievements.achievements_id = achievements.id where clients_id = ?;";
		return super.executeQuery(sql, new String[]{clientId}, "Failed to retrieve achievements for client.");
	}
	
	public JSONArray getRunSchemasForClient(String clientId) throws Exception{
    	String sql = "select id, `name`, description from clients_runschemas join runschemas on clients_runschemas.runSchemas_id = runschemas.id where clients_id = ?;";
		return super.executeQuery(sql, new String[]{clientId}, "Failed to retrieve run schemas for client.");
	}
}

package models;

import org.json.simple.JSONArray;

import play.db.Database;

public class ClientDatabaseWrapper extends DatabaseWrapper{

	public ClientDatabaseWrapper(Database db) {
		super(db);
	}
	
	public JSONArray getAllClients() throws Exception{
    	String sql = "select clients.id, personalData_id, firstName, lastName, phoneNumber, username from clients join personaldata on clients.personalData_id = personaldata.id";
		return super.executeQuery(sql, "Could not retrieve clients.");
	}
	
	public int registerClient(String firstName, String lastName, String phoneNumber, String username, String password) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = super.addValues("insert into personaldata values(0,", values);
		sql += "; insert into clients values(0, last_insert_id());";
		return super.executeInsertReturnId(sql, "Could not register client.", db.getConnection(), false, true, true);
	}
	
	public void assignRunSchemaToClient(String clientId, String runSchemaId) throws Exception{
		String[] values = {clientId, runSchemaId};
		String sql = super.addValues("insert into clients_runschemas values(", values);
		super.executeUpdate(sql, "Could not assign run schema to client.");
	}
}
package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Henderikus on 2-11-2016.
 */
public class MonitorDatabaseWrapper extends PersonDatabaseWrapper{

    public MonitorDatabaseWrapper(Database db) {
        super(db);
    }

    /**
     * insert monitor
     * @param monitor
     * @throws Exception
     */
    @Override
    public int create(JSONObject monitor) throws Exception{
        final String query = "INSERT INTO monitors (personalData_id, accessLevel) VALUES (?, ?) " ;
        int personalDataId = super.createPerson(monitor);
        Connection con = null;

        if(personalDataId != 0){
            try{
                con = super.db.getConnection();

                final PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, personalDataId);
                stmt.setInt(2, Integer.parseInt(monitor.get("accessLevel").toString()));

                return super.insertRow(stmt);
            }finally {
                con.close();
            }
        }else{
            throw new Exception("Sorry something went wrong. Try again with another username");
        }
    }

    @Override
    public JSONArray getAll() throws Exception{
    	String sql = "select monitors.id, personalData_id, accessLevel, firstName, lastName, phoneNumber, username from monitors join personaldata on monitors.personalData_id = personaldata.id";
		return super.executeQuery(sql, null, "Failed to retrieve monitors.");
	}
    
    public void assignClientToMonitor(String monitorId, String clientId, String monitorNumber) throws Exception{
		String[] values = {monitorId, clientId, monitorNumber};
		String sql = "insert into monitors_clients values(?,?,?);";
		super.executeUpdate(sql, values, "Failed to assign client to monitor.");
	}
    
    public JSONArray getClientsForMonitor(String monitorId) throws Exception{
    	String sql = "select clients.id, personalData_id, firstName, lastName, phoneNumber, username from monitors_clients join clients on monitors_clients.clients_id = clients.id join personaldata on clients.personalData_id = personaldata.id where monitors_id = ?;";
		return super.executeQuery(sql, new String[]{monitorId}, "Failed to retrieve clients for monitor.");
	}
    
    /**
     * Validate username and passwords. Password is validated by using PasswordUtil class
     * When password or username is incorrect an exception is thrown
     * @param username
     * @param password
     * @return JSONObject user
     * @throws Exception
     */
    public JSONObject validateLogin(String username, String password) throws Exception{
        final String query = "SELECT p.id, p.firstName, p.lastName, p.username, p.password, m.accessLevel " +
                        "FROM personaldata p, monitors m " +
                        "WHERE m.personalData_id = p.id " +
                        "AND username = ?;";

        JSONObject user = (JSONObject) super.executeQuery(query, new String[]{username}, "Invalid username or password").get(0);

        final PasswordUtil passwordUtil = PasswordUtil.getInstance();

        if(!user.isEmpty() && passwordUtil.validatePassword(password, user.get("password").toString())){
            user.remove("password");
            return user;
        }else{
            throw new Exception("Invalid username or password");
        }
    }
}

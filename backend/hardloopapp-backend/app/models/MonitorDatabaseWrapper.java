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
public class MonitorDatabaseWrapper extends PersonDatabaseWrapper implements Wrapper{

    private final int ACCESSLEVEL = 2;

    public MonitorDatabaseWrapper(Database db) {
        super(db);
    }

    /**
     * insert monitor
     * @param monitor
     * @throws Exception
     */
    public int create(JSONObject monitor) throws Exception{
        final String query = "INSERT INTO monitors (personalData_id, accessLevel) VALUES (?, ?) " ;
        int personalDataId = super.create(monitor);
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

    public JSONArray getAllMonitors() throws Exception{
    	String sql = "select monitors.id, personalData_id, accessLevel, firstName, lastName, phoneNumber, username from monitors join personaldata on monitors.personalData_id = personaldata.id";
		return super.executeQuery(sql, "Could not retrieve monitors.");
	}
    
    public int registerMonitor(String firstName, String lastName, String phoneNumber, String username, String password, String accessLevel) throws Exception{
		String[] values = {firstName, lastName, phoneNumber, username, password};
		String sql = super.addValues("insert into personaldata values(0,", values);
		sql += "; insert into monitors values(0, last_insert_id(), '" + accessLevel + "');";
		return super.executeInsertReturnId(sql, "Failed to register monitor.", db.getConnection(), false, true, true);
	}
    
    public void assignClientToMonitor(String monitorId, String clientId, String monitorNumber) throws Exception{
		String[] values = {monitorId, clientId, monitorNumber};
		String sql = super.addValues("insert into monitors_clients values(", values);
		super.executeUpdate(sql, "Failed to assign client to monitor.");
	}

}

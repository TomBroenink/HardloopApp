package models;

import org.json.simple.JSONObject;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Henderikus on 2-11-2016.
 */
public class MonitorDataseWrapper extends PersonDatabaseWrapper {

    private final int ACCESSLEVEL = 2;

    public MonitorDataseWrapper(Database db) {
        super(db);
    }

    /**
     * insert monitor
     * @param monitor
     * @throws Exception
     */
    public void insertMonitor(JSONObject monitor) throws Exception{
        final String query = "INSERT INTO monitors (personalData_id, accessLevel) VALUES (?, ?) " ;
        int personalDataId = super.insertPerson(monitor);
        Connection con = null;

        if(personalDataId != 0){
            try{
                con = super.db.getConnection();

                final PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, personalDataId);
                stmt.setInt(2, ACCESSLEVEL);

                super.insertRow(stmt);
            }finally {
                con.close();
            }
        }else{
            throw new Exception("Sorry something went wrong. Try again with another username");
        }
    }

}

package models;

import org.json.simple.JSONObject;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Henderikus on 2-11-2016.
 */
abstract class PersonDatabaseWrapper extends DatabaseWrapper{

    public PersonDatabaseWrapper(Database db) {
        super(db);
    }
    
    @Override
    public void delete(int id) throws Exception {
        executeUpdate("delete from personaldata where id = ?;", new String[]{String.valueOf(id)}, "Failed to delete user.");
    }
    
    protected void updateUser(JSONObject args) throws Exception{
    	String id = (String) args.get("personalDataId");
    	if(id != null && !id.isEmpty()){
	    	String sql = "update personalData set ";
	    	ArrayList<String> values = new ArrayList<String>();
	    	String[] keys = new String[]{"firstName", "lastName", "phoneNumber", "username", "password"};
	    	for(int i = 0; i < keys.length; i++){
				String key = keys[i];
				String value = (String) args.get(key);
				if(value != null && !value.isEmpty()){
					sql += key + " = ?,";
					values.add(value);
				}
			}
	    	sql = sql.substring(0, sql.length() - 1) + " where id = ?;";
	    	values.add(id);
	    	super.executeUpdate(sql, values.toArray(new String[values.size()]), "Failed to update user.");
    	}
    	else{
    		throw new Exception("No personalDataId supplied.");
    	}
    }

    /**
     * insert personal data using prepared statement
     * @param person
     * @return int 0 or id inserted row
     * @throws Exception
     */
    protected int createPerson(JSONObject person) throws Exception{
        System.out.println("jsonObject: " + person);

        final String query = "INSERT INTO personaldata (firstName, lastName, phoneNumber, username, password)"+
                        "SELECT ?, ?, ?, ?, ? FROM dual " +
                        "WHERE NOT EXISTS (SELECT username FROM personaldata WHERE username = ?);"  ;
        Connection con = null;

        try{
            con = db.getConnection();

            final PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, person.get("firstName").toString());
            stmt.setString(2, person.get("lastName").toString());
            stmt.setString(3, person.get("phoneNumber").toString());
            stmt.setString(4, person.get("username").toString());
            stmt.setString(5, PasswordUtil.getInstance().encryptPassword(person.get("password").toString()));
            stmt.setString(6, person.get("username").toString());

            return insertRow(stmt);
        }finally {
            con.close();
        }
    }

    /**
     * execute preparedStatement and retun id
     * written for insert personaldata
     * @param stmt
     * @return
     * @throws Exception
     */
    protected int insertRow(PreparedStatement stmt) throws Exception{
        int id = 0;
        final int rowAffected = stmt.executeUpdate();
        if(rowAffected == 1){
            final ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        return id;
    }
}

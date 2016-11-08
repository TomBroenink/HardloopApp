package models;

import org.json.simple.JSONObject;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Henderikus on 2-11-2016.
 */
public class PersonDatabaseWrapper extends DatabaseWrapper implements Wrapper{

    public PersonDatabaseWrapper(Database db) {
        super(db);
    }


    /**
     * insert personal data using prepared statement
     * @param person
     * @return int 0 or id inserted row
     * @throws Exception
     */
    public int create(JSONObject person) throws Exception{
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
                        "AND username = " + username + "";

        JSONObject user = (JSONObject) super.executeQuery(query, "Invalid username or password").get(0);

        final PasswordUtil passwordUtil = PasswordUtil.getInstance();

        if(!user.isEmpty() && passwordUtil.validatePassword(password, user.get("password").toString())){
            user.remove("password");
            return user;
        }else{
            throw new Exception("Invalid username or password");
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
    
    public void deleteUser(String personalDataId) throws Exception{
		executeUpdate("delete from personaldata where id = '" + personalDataId + "'", "Failed to delete user.");
	}

}

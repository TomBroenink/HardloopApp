package models;

import org.json.simple.JSONObject;
import play.db.Database;

/**
 * Created by hbh13 on 7-11-2016.
 */
public class CareProfileWrapper extends DatabaseWrapper {

    public CareProfileWrapper(Database db) {
        super(db);
    }

    public int create(JSONObject careProfile) throws Exception{
        String[] values = {careProfile.get("name").toString()};
        String query = addValues("INSERT INTO care_profiles VALUES(0,", values);
        int id = executeInsertReturnId(query, "Failed to save CareProfile!");
        return id;
    }
}

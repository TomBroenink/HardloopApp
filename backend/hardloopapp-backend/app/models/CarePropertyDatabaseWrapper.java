package models;

import org.json.simple.JSONObject;
import play.db.Database;

/**
 * Created by Henderikus on 7-11-2016.
 */
public class CarePropertyDatabaseWrapper extends DatabaseWrapper implements Wrapper{

    public CarePropertyDatabaseWrapper(Database db) {
        super(db);
    }

    public int create(JSONObject careProperty) throws Exception{
        final String[] values = {careProperty.get("name").toString(), careProperty.get("description").toString()};
        final String query = addValues("INSERT INTO care_properties VALUES(0,", values);
        return super.executeInsertReturnId(query, "Something went wrong while saving care property! Please try again later.");
    }
}

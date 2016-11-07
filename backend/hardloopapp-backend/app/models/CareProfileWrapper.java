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

    public int create(JSONObject careProfile){
        String query;
        return 0;
    }
}

package models;

import play.db.*;

/**
 * Created by hbh13 on 7-11-2016.
 */
public class DatabaseWrapperFactory {

    private Database db;

    public DatabaseWrapperFactory(Database db){
        this.db = db;
    }

    public DatabaseWrapper getWrapper(String type) throws Exception{
        DatabaseWrapper wrapper = null;
        switch(type){
            case "careProfile":
                wrapper = new CareProfileWrapper(db);
                break;
            default:
                throw new Exception("Invalid wrapper type");
        }
        return wrapper;
    }
}

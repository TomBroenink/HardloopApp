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
        switch(type){
            case "person":
                return new PersonDatabaseWrapper(db);
            case "monitor":
                return new MonitorDatabaseWrapper(db);
            case "careProfile":
                return new CareProfileWrapper(db);
            case "careProperty":
                return new CarePropertyDatabaseWrapper(db);
            case "client":
                return new ClientDatabaseWrapper(db);
            case "run":
                return new RunDatabaseWrapper(db);
            case "runSchema":
                return new RunSchemaDatabaseWrapper(db);
            default:
                throw new Exception("Invalid wrapper type");
        }
    }
}

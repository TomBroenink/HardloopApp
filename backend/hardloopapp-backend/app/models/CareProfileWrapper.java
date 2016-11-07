package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.db.Database;

/**
 * Created by hbh13 on 7-11-2016.
 */
public class CareProfileWrapper extends DatabaseWrapper implements Wrapper {

    public CareProfileWrapper(Database db) {
        super(db);
    }

    public int create(JSONObject careProfile) throws Exception{
        final String[] values = {careProfile.get("name").toString()};
        final String query = addValues("INSERT INTO care_profiles VALUES(0,", values);
        return executeInsertReturnId(query, "Failed to save CareProfile!");
    }

    /**
     * add given properties to careProfile
     * @param careProfileId
     * @param properties
     * @throws Exception
     */
    public void addPropertiesToProfile(int careProfileId, JSONArray properties) throws Exception{
        for (Object prop : properties){

            JSONObject property = (JSONObject) prop;
            String propertyId = property.get("carePropertyId").toString();

            if(!this.hasProperty(careProfileId, Integer.parseInt(propertyId))){

                String[] values = {Integer.toString(careProfileId), propertyId, property.get("applies").toString()};
                String query = addValues("INSERT INTO care_profile_properties VALUES(", values);

                super.executeInsert(query, "Property with id: " + propertyId + " could not be stored!" );
            }else{
                throw new Exception("Care profile: "+ careProfileId +"  already contains the property with id " + propertyId +"!");
            }
        }
    }

    /**
     * Check if profile already contains the property
     * When it does not contain the property return false else true
     * @param careProfileId
     * @param carePropertyId
     * @return  when it does not contain the property return false else true
     * @throws Exception
     */
    private boolean hasProperty(int careProfileId, int carePropertyId) throws Exception{
        String query = "SELECT * FROM care_profile_properties " +
                "WHERE care_profile_id = " + careProfileId + " " +
                "AND care_property_id = " + carePropertyId;

        JSONArray results = super.executeQuery(query, "");

        if(results.isEmpty()){
            return false;
        }
        return true;
    }
}

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

    /**
     * Create a new profile
     * @param careProfile
     * @return int created row id
     * @throws Exception
     */
    public int create(JSONObject careProfile) throws Exception{
        final String[] values = {careProfile.get("name").toString()};
        final String query = addValues("INSERT INTO care_profiles VALUES(0,", values);
        return executeInsertReturnId(query, "Failed to save CareProfile!");
    }

    @Override
    public void delete(int id) throws Exception{
        super.executeUpdate("DELETE FROM care_profiles where id = " + id, "Failed to delete profile! It could still be in use.");
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
     * method for fetching all profiles with their properties from the db
     * @return JSONArray profiles with properties
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public JSONArray getAllProfilesWithProperties() throws Exception {
        String query = "SELECT id FROM care_profiles";

        final JSONArray returnValue = new JSONArray();

        JSONArray result = super.executeQuery(query, "Something went wrong!");

        for(Object o : result){
            JSONObject pr = (JSONObject) o;
            returnValue.add(this.getProfileWithProperties(Integer.parseInt(pr.get("id").toString())));
        }

        return returnValue;

    }

    /**
     * Get Care profile by id with all it's care properties
     * @param profileId
     * @return JSONObject CareProfile
     * @throws Exception
     */
    public JSONObject getProfileWithProperties(int profileId) throws Exception{
        String query = "SELECT cp.id as cp_id, cp.name as cp_name, prop.id as prop_id, prop.name as prop_name, prop.description as prop_des, cpp.applies  " +
                "FROM care_profiles cp, care_profile_properties cpp, care_properties prop  " +
                "WHERE cp.id = "+ profileId + " " +
                "AND cpp.care_profile_id = cp.id " +
                "AND cpp.care_property_id = prop.id";

        JSONArray result = super.executeQuery(query, "The given CareProfile with id " + profileId + " could not be found!");

        if(result.isEmpty()){
            throw new Exception("CareProfile with id: " + profileId + " does not exist!");
        }else{
            return this.buildProfileWithPropertiesObject(result);
        }
    }

    /**
     * Convert json from results from query in method getProfileWithProperties to a more
     * usable and readable format
     * @param array
     * @return JSONObject profile
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONObject buildProfileWithPropertiesObject(JSONArray array) throws Exception{
        final JSONObject result = new JSONObject();
        final JSONArray properties = new JSONArray();

        for(Object o : array){
            final JSONObject prop = (JSONObject) o;
            final JSONObject property = new JSONObject();

            if(result.containsKey("profileId")){
                if(!result.get("profileId").toString().equals(prop.get("cp_id").toString())){
                    throw new Exception("Invalid json while converting!");
                }
            }else{
                result.put("profileId", prop.get("cp_id").toString());
                result.put("profileName", prop.get("cp_name").toString());
            }

            property.put("id", prop.get("prop_id").toString());
            property.put("name", prop.get("prop_name").toString());
            property.put("description", prop.get("prop_des").toString());
            property.put("applies", prop.get("applies").toString());

            properties.add(property);
        }

        result.put("properties", properties);

        return result;
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

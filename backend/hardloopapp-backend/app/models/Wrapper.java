package models;

import org.json.simple.JSONObject;

/**
 * Created by Henderikus on 7-11-2016.
 */
public interface Wrapper {

    int create(JSONObject o) throws Exception;
}

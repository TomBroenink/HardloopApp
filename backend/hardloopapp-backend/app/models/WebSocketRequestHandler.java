package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import play.db.Database;

public class WebSocketRequestHandler {
	
	private static JSONParser parser = new JSONParser();
	
	public static String handleRequest(String message, Database db){
		JSONObject response = null;
    	String requestAction = null;
    	try{
			JSONObject args = (JSONObject) parser.parse(message);
			requestAction = (String) args.get("requestAction");
			if(requestAction == null){
				throw new Exception("Invalid RequestAction.");
			}
			response = (JSONObject) parser.parse("{\"responseAction\": \"" + requestAction + "\",\"responseStatusCode\":1,\"responseStatusDescription\":\"Succes\"}");
			DatabaseWrapper dbWrapper = new DatabaseWrapper(db);
			if(requestAction.equals("getAllMonitors")){
				JSONArray monitors = dbWrapper.getAllMonitors();
				response.put("monitors", monitors);
			}
			else{
				throw new Exception("Invalid RequestAction.");
			}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return "{\"responseAction\": \"" + requestAction + "\",\"responseStatusCode\":0,\"responseStatusDescription\":\"Failure\",\"errorDescription\":\"" + e.getMessage() + "\"}";
    	}
        return response.toJSONString();
	}
}

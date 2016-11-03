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
			response = (JSONObject) parser.parse("{\"responseAction\": \"" + requestAction + "\",\"responseStatusCode\":\"1\",\"responseStatusDescription\":\"Succes\"}");
			DatabaseWrapper dbWrapper = new DatabaseWrapper(db);
<<<<<<< HEAD
			if(requestAction.equals("getAllMonitors")){
				JSONArray monitors = dbWrapper.getAllMonitors();
				response.put("monitors", monitors);
			}else if(requestAction.equals("createMonitor")){
				MonitorDataseWrapper wrapper = new MonitorDataseWrapper(db);
				wrapper.insertMonitor((JSONObject)args.get("person"));
				response.put("status", 200);
				response.put("message", "Monitor has been stored without problems");
			}
			else{
				throw new Exception("Invalid RequestAction.");
=======
			switch(requestAction){
				case "getAllMonitors":
					JSONArray monitors = dbWrapper.getAllMonitors();
					response.put("monitors", monitors);
					break;
				case "getAllClients":
					JSONArray clients = dbWrapper.getAllClients();
					response.put("clients", clients);
					break;
				case "registerClient":
					dbWrapper.registerClient((String) args.get("firstName"), (String) args.get("lastName"), (String) args.get("phoneNumber"), (String) args.get("username"), (String) args.get("password"));
					break;
				case "removeUser":
					dbWrapper.removeUser((String) args.get("personalDataId"));
					break;
				case "assignClientToMonitor":
					dbWrapper.assignClientToMonitor((String) args.get("monitorId"), (String) args.get("clientId"), (String) args.get("monitorNumber"));
					break;
				case "createRunSchema":
					dbWrapper.createRunSchema((String) args.get("name"), (String) args.get("description"));
					break;
				case "assignRunSchemaToClient":
					dbWrapper.assignRunSchemaToClient((String) args.get("clientId"), (String) args.get("runSchemaId"));
					break;
				default:
					throw new Exception("Invalid RequestAction.");
>>>>>>> 0a4c4be3a436be3118f98bf7533b512e6dda1c12
			}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return "{\"responseAction\": \"" + requestAction + "\",\"responseStatusCode\":\"0\",\"responseStatusDescription\":\"Failure\",\"errorDescription\":\"" + e.getMessage() + "\"}";
    	}
        return response.toJSONString();
	}
}

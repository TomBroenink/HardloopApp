package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import play.db.Database;

public class WebSocketRequestHandler {
	
	private static JSONParser parser = new JSONParser();
	
	@SuppressWarnings("unchecked")
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
			switch(requestAction){
				case "getAllMonitors":
					response.put("monitors", new MonitorDatabaseWrapper(db).getAllMonitors());
					break;
				case "getAllClients":
					response.put("clients", new ClientDatabaseWrapper(db).getAllClients());
					break;
				case "registerClient":
					response.put("clientId", new ClientDatabaseWrapper(db).registerClient((String) args.get("firstName"), (String) args.get("lastName"), (String) args.get("phoneNumber"), (String) args.get("username"), (String) args.get("password")));
					break;
				case "deleteUser":
					new PersonDatabaseWrapper(db).deleteUser((String) args.get("personalDataId"));
					break;
				case "assignClientToMonitor":
					new MonitorDatabaseWrapper(db).assignClientToMonitor((String) args.get("monitorId"), (String) args.get("clientId"), (String) args.get("monitorNumber"));
					break;
				case "createRunSchema":
					response.put("runSchemaId", new RunSchemaDatabaseWrapper(db).createRunSchema((String) args.get("name"), (String) args.get("description")));
					break;
				case "assignRunSchemaToClient":
					new ClientDatabaseWrapper(db).assignRunSchemaToClient((String) args.get("clientId"), (String) args.get("runSchemaId"));
					break;
				case "registerMonitor":
					MonitorDatabaseWrapper wrapper = new MonitorDatabaseWrapper(db);
					wrapper.insertMonitor((JSONObject)args.get("person"));
					response.put("status", 200);
					response.put("message", "Monitor has been stored without problems");
					break;
				case "assignRunToRunSchema":
					new RunSchemaDatabaseWrapper(db).assignRunToRunSchema((String) args.get("runSchemaId"), (String) args.get("runId"), (String) args.get("day"), (String) args.get("time"));
					break;
				case "createRun":
					response.put("runId", new RunDatabaseWrapper(db).createRun((String) args.get("name"), (String) args.get("description"), (String) args.get("distance"), (JSONArray) args.get("route")));
					break;
				case "deleteRunFromRunSchema":
					new RunSchemaDatabaseWrapper(db).deleteRunFromRunSchema((String) args.get("runSchemaId"), (String) args.get("runId"));
					break;
				case "deleteRunSchemaFromClient":
					new ClientDatabaseWrapper(db).deleteRunSchemaFromClient((String) args.get("clientId"), (String) args.get("runSchemaId"));
					break;
				default:
					throw new Exception("Invalid RequestAction.");
			}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return "{\"responseAction\": \"" + requestAction + "\",\"responseStatusCode\":\"0\",\"responseStatusDescription\":\"Failure\",\"errorDescription\":\"" + e.getMessage() + "\"}";
    	}
        return response.toJSONString();
	}
}

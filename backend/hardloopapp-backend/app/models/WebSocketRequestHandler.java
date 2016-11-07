package models;

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
			DatabaseWrapperFactory factory = new DatabaseWrapperFactory(db);
			DatabaseWrapper dbWrapper = new DatabaseWrapper(db);

			switch(requestAction){
				case "getAllMonitors":
					response.put("monitors", dbWrapper.getAllMonitors());
					break;
				case "getAllClients":
					response.put("clients", dbWrapper.getAllClients());
					break;
				case "registerClient":
					response.put("clientId", dbWrapper.registerClient((String) args.get("firstName"), (String) args.get("lastName"), (String) args.get("phoneNumber"), (String) args.get("username"), (String) args.get("password")));
					break;
				case "removeUser":
					dbWrapper.removeUser((String) args.get("personalDataId"));
					break;
				case "assignClientToMonitor":
					dbWrapper.assignClientToMonitor((String) args.get("monitorId"), (String) args.get("clientId"), (String) args.get("monitorNumber"));
					break;
				case "createRunSchema":
					response.put("runSchemaId", dbWrapper.createRunSchema((String) args.get("name"), (String) args.get("description")));
					break;
				case "assignRunSchemaToClient":
					dbWrapper.assignRunSchemaToClient((String) args.get("clientId"), (String) args.get("runSchemaId"));
					break;
				case "registerMonitor":
					MonitorDataseWrapper wrapper = new MonitorDataseWrapper(db);
					wrapper.insertMonitor((JSONObject)args.get("person"));
					response.put("status", 200);
					response.put("message", "Monitor has been stored without problems");
					break;
				case "assignRunToRunSchema":
					dbWrapper.assignRunToRunSchema((String) args.get("runSchemaId"), (String) args.get("runId"), (String) args.get("day"), (String) args.get("time"));
					break;
				case "createRun":
					response.put("runId", dbWrapper.createRun((String) args.get("name"), (String) args.get("description"), (String) args.get("routeId")));
					break;
				case "createEmpyProfile":
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

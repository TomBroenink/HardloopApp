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
			DatabaseWrapperFactory factory = new DatabaseWrapperFactory(db);
			switch(requestAction){
				case "getAllMonitors":
					response.put("monitors", factory.getWrapper("monitor").getAll());
					break;
				case "getAllClients":
					response.put("clients", factory.getWrapper("client").getAll());
					break;
				case "registerClient":
					response.put("clientId", factory.getWrapper("client").create(args));
					break;
				case "deleteUser":
					factory.getWrapper("monitor").delete(Integer.parseInt((String) args.get("personalDataId")));
					break;
				case "assignClientToMonitor":
					((MonitorDatabaseWrapper) factory.getWrapper("monitor")).assignClientToMonitor((String) args.get("monitorId"), (String) args.get("clientId"), (String) args.get("monitorNumber"));
					break;
				case "createRunSchema":
					response.put("runSchemaId", factory.getWrapper("runSchema").create(args));
					break;
				case "assignRunSchemaToClient":
					((ClientDatabaseWrapper) factory.getWrapper("client")).assignRunSchemaToClient((String) args.get("clientId"), (String) args.get("runSchemaId"));
					break;
				case "registerMonitor":
					response.put("monitorId", factory.getWrapper("monitor").create((JSONObject)args.get("person")));
					break;
				case "assignRunToRunSchema":
					((RunSchemaDatabaseWrapper) factory.getWrapper("runSchema")).assignRunToRunSchema((String) args.get("runSchemaId"), (String) args.get("runId"), (String) args.get("day"), (String) args.get("time"));
					break;
				case "createRun":
					response.put("runId", factory.getWrapper("run").create(args));
					break;
				case "createEmptyCareProfile":
					response.put("profileId", factory.getWrapper("careProfile").create((JSONObject) args.get("careProfile")));
					break;
				case "createCareProperty":
					response.put("careProperty", factory.getWrapper("careProperty").create((JSONObject) args.get("careProperty")));
					break;
				case "setCareProfileProperties":
					((CareProfileWrapper)factory.getWrapper("careProfile")).addPropertiesToProfile(Integer.parseInt(args.get("careProfileId").toString()), (JSONArray) args.get("careProperties"));
                    break;
				case "getCareProfileWithProperties":
					response.put("careProfile", factory.getWrapper("careProfile").getById(args.get("careProfileId").toString()));
					break;
				case "deleteRunFromRunSchema":
					((RunSchemaDatabaseWrapper) factory.getWrapper("runSchema")).deleteRunFromRunSchema((String) args.get("runSchemaId"), (String) args.get("runId"));
					break;
				case "deleteRunSchemaFromClient":
					((ClientDatabaseWrapper) factory.getWrapper("client")).deleteRunSchemaFromClient((String) args.get("clientId"), (String) args.get("runSchemaId"));
					break;
				case "getClientsForMonitor":
					response.put("clients", ((MonitorDatabaseWrapper)factory.getWrapper("monitor")).getClientsForMonitor((String) args.get("monitorId")));
					break;
				case "getAchievementsForClient":
					response.put("achievements", ((ClientDatabaseWrapper) factory.getWrapper("client")).getAchievementsForClient((String) args.get("clientId")));
					break;
				case "getRunSchemasForClient":
					response.put("runSchemas", ((ClientDatabaseWrapper) factory.getWrapper("client")).getRunSchemasForClient((String) args.get("clientId")));
					break;
				case "getAllProfilesWithProperties":
					response.put("careProfiles", factory.getWrapper("careProfile").getAll());
					break;
				case "getAllCareProperties":
					response.put("careProperties", factory.getWrapper("careProperty").getAll());
					break;
				case "getClientById":
					response.put("client", factory.getWrapper("client").getById((String) args.get("clientId")));
					break;
				case "getMonitorById":
					response.put("client", factory.getWrapper("monitor").getById((String) args.get("monitorId")));
					break;
				case "getAllRuns":
					response.put("runs", factory.getWrapper("run").getAll());
					break;
				case "deleteClientFromMonitor":
					((MonitorDatabaseWrapper) factory.getWrapper("monitor")).deleteClientFromMonitor((String) args.get("monitorId"), (String) args.get("clientId"));
					break;
				case "getRouteForRun":
					response.put("route", ((RunDatabaseWrapper) factory.getWrapper("run")).getRouteForRun((String) args.get("routeId")));
					break;
				case "getRunsForRunSchema":
					response.put("runs", ((RunSchemaDatabaseWrapper) factory.getWrapper("runSchema")).getRunsForRunSchema((String) args.get("runSchemaId")));
					break;
				case "getAllRunSchemas":
					response.put("runSchemas", factory.getWrapper("runSchema").getAll());
					break;
				case "getRunSchemaById":
					response.put("runSchema", factory.getWrapper("runSchema").getById((String) args.get("runSchemaId")));
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

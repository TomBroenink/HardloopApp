package controllers;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import akka.stream.javadsl.Flow;
import models.WebSocketRequestHandler;
import play.db.Database;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	
	private Database db;

    @Inject
    public HomeController(Database db) {
        this.db = db;
    }

    public Result index() {
        return ok(index.render());
    }

    public Result mockUser(){
        return ok(mockuser.render());
    }
    
    @SuppressWarnings("deprecation")
	public LegacyWebSocket<String> ws() {
        return WebSocket.whenReady((in, out) -> {
            in.onMessage(message -> {
            	System.out.println(message);
            	String response = WebSocketRequestHandler.handleRequest(message, db);
            	System.out.println(response);
            	out.write(response);
            });
        });
    }

}

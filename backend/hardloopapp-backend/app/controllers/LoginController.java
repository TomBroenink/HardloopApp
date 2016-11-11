package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.MonitorDatabaseWrapper;
import org.json.simple.JSONObject;
import play.db.Database;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by hbh13 on 3-11-2016.
 */
public class LoginController extends Controller {

    private Database db;

    @Inject
    public LoginController(Database db) {
        this.db = db;
    }

    @BodyParser.Of(Json.class)
    public Result login(){
        final JsonNode json = request().body().asJson();
        Result result;

        final MonitorDatabaseWrapper wrapper = new MonitorDatabaseWrapper(db);

        try{
            JSONObject user = wrapper.validateLogin(json.get("username").toString(), json.get("password").asText());
            result = ok(user.toJSONString());
        }catch(Exception e){
            result = badRequest(e.getMessage());
        }

        return result;
    }
}

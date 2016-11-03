package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by hbh13 on 3-11-2016.
 */
public class LoginController extends Controller {

    @BodyParser.Of(Json.class)
    public Result login(){
        final JsonNode json = request().body().asJson();
        Result result = badRequest("Invalid username or password!");



        return result;
    }
}

package controllers;

import play.*;
import play.data.validation.Constraints;
import play.mvc.*;
import models.User;
import views.html.*;
import play.mvc.Controller;

public class Application extends Controller {

    public static Result index() {
    	//Logger.debug(sesstion("username"));
        return ok(index.render(User.findByUsername(request().username()), "Your new application is ready."));
    }
    
    public static Result listuser(){
//    	List<User> users = User.findAll();
    	return ok(userlist.render(User.findAll()));
    }
}

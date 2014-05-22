package controllers;

import play.*;
import play.mvc.*;
import models.User;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result listuser(){
//    	List<User> users = User.findAll();
    	return ok(userlist.render(User.findAll()));
    }
}

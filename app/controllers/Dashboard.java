package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Result;
import views.html.dashbord.index;

@Security.Authenticated(Secured.class)
public class Dashboard extends Controller {
	public static Result index(){
		
		return ok(index.render(User.findByUsername(request().username())));
	}
}

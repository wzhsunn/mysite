package controllers;

import java.util.Date;

import models.User;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Result;
import views.html.dashbord.index;

@Security.Authenticated(Secured.class)
public class Dashboard extends Controller {
	public static Result index(){
		User user = User.findByUsername(request().username());
		user.loginTime = new Date();
		user.loginIp = request().remoteAddress();
		user.save();
		return ok(index.render(user));
	}
}

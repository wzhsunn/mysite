package controllers;

import static play.data.Form.form;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signin.*;

public class SignIn extends Controller {
	
	public static Result blank() {
        return ok(
            index.render(form(User.class))
        );
    }
	
	 
    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<User> loginForm = form(User.class).bindFromRequest();       
        if(loginForm.hasErrors()){
        	String err = loginForm.errorsAsJson().toString();
        	Logger.debug(err);
        	 return badRequest(index.render(loginForm));
        }
        else {
            session("username", loginForm.get().username);
            User findUser = User.findByUsername(loginForm.get().username);
            return ok(summary.render(findUser));
        }
    }
}

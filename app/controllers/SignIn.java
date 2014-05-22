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
        User user = loginForm.get();
        
        Logger.debug("user " + user.toString());
        
        if(User.authenticate(user.username, user.password) == null){
        	Logger.debug("run here");
        	 return badRequest(index.render(loginForm));
        }else if(loginForm.hasErrors()) {
            return badRequest(index.render(loginForm));
        } else {
            session("username", loginForm.get().username);
            return ok(summary.render(loginForm.get()));
//            return redirect(
//                routes.SignUp.index()
//            );
        }
    }
}

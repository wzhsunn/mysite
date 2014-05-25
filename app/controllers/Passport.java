package controllers;

import static play.data.Form.form;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Email;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.passport.register;
import views.html.passport.registerOk;
import views.html.passport.login;

public class Passport extends Controller {
	
	 public static Result GO_HOME = redirect(
	            routes.Application.index()
	    );

	    public static Result GO_DASHBOARD = redirect(
	            routes.Dashboard.index()
	    );
	
	public static Result register() {
        return ok(
            register.render(form(Register.class, User.All.class))
        );
    }
	
    /**
     * Handle login form submission.
     */
    public static Result registerSubmit() {
    	 Form<Register> registerForm = form(Register.class, User.All.class).bindFromRequest();
    	 
    	 if(registerForm.hasErrors() == false){
    		 String username = registerForm.get().username;
    		 if(username.trim() == ""){
    			registerForm.reject("username", Messages.get("passport.username.empty")); 
    		 }
    		 else if(User.findByUsername(registerForm.get().username) != null){
         		 registerForm.reject("username", Messages.get("passport.username.exists"));
         	 }
         }
    	 
    	 if(!registerForm.field("password").valueOr("").isEmpty()) {
             if(!registerForm.field("password").valueOr("").equals(registerForm.field("repeatPassword").value())) {
            	 registerForm.reject("repeatPassword", Messages.get("passport.password.not.match"));
             }
         }
       
         if (registerForm.hasErrors()) {
             return badRequest(register.render(registerForm));
         }
         

         Register registerObj = registerForm.get();
        

         try {
             User user = new User();
             user.email = registerObj.email;
             user.phone = registerObj.phone;
             user.username = registerObj.username;
             user.password = registerObj.password;//TODO //Hash.createPassword(register.password);
             user.save();

             return ok(registerOk.render());
         } catch (Exception e) {
             Logger.error("Signup.save error", e);
             flash("error", Messages.get("error.technical"));
         }
         return badRequest(register.render(registerForm));
    }
    
    //TODO validation
    public static class Register {

    	@Email
        public String email;
        
        public String phone;
        
        @Constraints.Required
        public String username;

        @Constraints.Required
        public String password;

        
        /**
         * Validate the authentication.
         *
         * @return null if validation ok, string with details otherwise
         */
        public String validate() {
            if (isBlank(username)) {
                return "Full name is required";
            }

            if (isBlank(password)) {
                return "Password is required";
            }

            return null;
        }

        private boolean isBlank(String input) {
            return input == null || input.isEmpty() || input.trim().isEmpty();
        }
    }
    
    //login
    
    public static Result login(){
//    	return TODO;
    	return ok(
    			login.render(form(Login.class)));
    }
    
    public static Result loginSubmit(){
    	Form<Login> loginForm = form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session("username", loginForm.get().username);
            return GO_DASHBOARD;
        }
    }
    
    public static Result logout(){
    	 session().clear();
         flash("success", Messages.get("youve.been.logged.out"));
         return GO_HOME;
    }
    
    /**
     * Login class used by Login Form.
     */
    public static class Login {

        @Constraints.Required
        public String username;
        
        @Constraints.Required
        public String password;

        /**
         * Validate the authentication.
         *
         * @return null if validation ok, string with details otherwise
         */
        public String validate() {

            User user = null;
            user = User.authenticate(username, password);
      
            if (user == null) {
                return Messages.get("invalid.user.or.password");
            }
            return null;
        }

    }
}

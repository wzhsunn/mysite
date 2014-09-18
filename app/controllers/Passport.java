package controllers;

import static play.data.Form.form;

import javax.validation.Constraint;
import java.util.Date;
import models.User;
import models.utils.Hash;
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
import views.html.passport.update;

public class Passport extends Controller {
	
	 public static Result GO_HOME = redirect(
	            routes.Application.index()
	    );

	    public static Result GO_DASHBOARD = redirect(
	            routes.Dashboard.index()
	    );
	
	public static Result register() {
        return ok(
            register.render(form(Register.class))
        );
    }
	
	public static Result update(String username){
		User user = User.findByUsername(username);
		Form<Register> userForm = form(Register.class).fill(new Register(user));
		return ok(
				update.render(user, userForm)
				);
	}
	
	public static Result updateSubmit(){
		Form<Register> registerForm = form(Register.class).bindFromRequest();
		String errors = registerForm.errors().toString();
		Logger.debug(errors);

//		if (registerForm.hasErrors() == false) {
//			if (User.findByUsername(registerForm.get().username) != null) {
//				registerForm.reject("username",
//						Messages.get("passport.username.exists"));
//			}
//		}
		if (!registerForm.field("password").valueOr("").isEmpty()) {
			if (!registerForm.field("password").valueOr("")
					.equals(registerForm.field("repeatPassword").value())) {
				registerForm.reject("repeatPassword",
						Messages.get("passport.password.not.match"));
			}
		}

		if (registerForm.hasErrors()) {
			return badRequest(register.render(registerForm));
		}

		Register registerObj = registerForm.get();

		try {
			
			User user = User.findByUsername(registerObj.username);
			user.email = registerObj.email;
			user.phone = registerObj.phone;
			user.username = registerObj.username;
			user.password = Hash.createPassword(registerObj.password);
			user.regTime = new java.util.Date();
			user.regIp = request().remoteAddress();
			user.save();

			session("username", user.username);
            return GO_DASHBOARD;
		} catch (Exception e) {
			Logger.error("Signup.save error", e);
			flash("error", Messages.get("error.technical"));
		}
		return badRequest(register.render(registerForm));
	}
	
    /**
     * Handle login form submission.
     */
    public static Result registerSubmit() {
    	 Form<Register> registerForm = form(Register.class).bindFromRequest();
    	 String errors = registerForm.errors().toString();
    	 Logger.debug(errors);
    	 
    	 if(registerForm.hasErrors() == false){
    		 if(User.findByUsername(registerForm.get().username) != null){
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
             user.password =  Hash.createPassword(registerObj.password);
             user.regTime = new java.util.Date();
             user.regIp = request().remoteAddress();
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

    	
    	@Email(message="email格式不对")
        public String email;
        //TODO:phone valid
    	@Constraints.Pattern(value="^1[3|4|5|8][0-9]\\d{4,8}$", message="手机格式必须正确" )
        public String phone;
        
        @Constraints.Required(message="不能为空")
        @Constraints.MinLength(value=4,  message="不得少于4个字符")
        @Constraints.MaxLength(value=18, message="不得超过18个字符")
        public String username;

        @Constraints.Required(message="不能为空")
        @Constraints.MinLength(value=6,  message="不得少于4个字符")
        @Constraints.MaxLength(value=18, message="不得超过18个字符")
        public String password;

        public Register(){}
        public Register(User user){
        	email = user.email;
        	phone = user.phone;
        	username = user.username;
        	password = user.password;
        }
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

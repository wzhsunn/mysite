package controllers;

import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import views.html.passport.register;

import static play.data.Form.form;


import static play.data.Form.form;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;


public class Passport extends Controller {
	public static Result register(){
//		Form<Register> registerForm = form(Register.class);
		return ok(register.render(form(Register.class)));
	}
	
	public static Result registerSubmit(){
		return TODO;
	}
	
}

package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

public class Secured extends Authenticator {
	@Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.Application.index());
    }
}

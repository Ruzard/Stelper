package controllers;

import models.User;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.util.Date;

@With(Security.class)

public class Application extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byUsername", Security.connected()).first();
			renderArgs.put("user", user.username);
		}
	}

	public static void index() {
		render();
	}
}
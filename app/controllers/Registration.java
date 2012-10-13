package controllers;

import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;
import services.UserService;

/**
 * Created with IntelliJ IDEA.
 * User: viru
 * Date: 13.10.12
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class Registration extends Controller {

	public static void index() {
		render();
	}

	public static void handleSubmit(@Valid User user) {
		if (validation.hasErrors()) {
			render("@index");
		}
		if (UserService.register(user)) {
			render(user);
		} else {
			render("errors/500.html");
		}
	}

}

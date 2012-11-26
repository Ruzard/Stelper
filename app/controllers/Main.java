package controllers;

import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Created with IntelliJ IDEA. User: viru Date: 13.10.12 Time: 20:37 To change
 * this template use File | Settings | File Templates.
 */
@With(Secure.class)
public class Main extends Controller {

	private static User user;

	@Before
	static void setConnectedUser() {

		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void main() {
		Posts.frontPosts();
	}
}

package controllers;

import java.util.List;

import models.PrivateConversation;
import models.UniversalPost;
import models.User;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import services.MessageService;
import services.UserService;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 26.11.12 Time: 18:56 To change
 * this template use File | Settings | File Templates.
 */
public class Users extends Controller {
	private static User user;

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void profile() {
		List<UniversalPost> createdPosts = UniversalPost.find("byAuthor", user).fetch();
		List<PrivateConversation> conversations = MessageService.getPrivateConversations(user);

		renderArgs.put("user", user);
		renderArgs.put("conversations", conversations);
		renderArgs.put("createdPosts", createdPosts);
		render();
	}

	public static void registration() {
		render();
	}

	public static void registrationSubmit(@Valid User user) {
		if (validation.hasErrors()) {
			render("@registration");
		}
		if (UserService.register(user)) {
			render(user);
		} else {
			render("errors/500.html");
		}
	}
}

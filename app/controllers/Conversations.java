package controllers;

import models.User;
import models.exceptions.PrivateMessageException;
import play.mvc.Before;
import play.mvc.Controller;
import services.MessageService;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 29.11.12 Time: 21:52 To change
 * this template use File | Settings | File Templates.
 */
public class Conversations extends Controller {
	private static User user;

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void sendMessage(String receiver, String message) {
		User receiverUser = User.find("byUsername", receiver).first();
		try {
			if (MessageService.sendMessage(user, receiverUser, message)) {
				session.put("notification", "Message sent successfully");
			} else {
				session.put("notification", "Message sent failed");
			}
			// Posts.frontPosts();
		} catch (PrivateMessageException e) {
			e.printStackTrace();

		}
	}
}

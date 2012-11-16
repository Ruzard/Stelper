package controllers;

import models.DealAgent;
import models.UniversalPost;
import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;
import services.UserService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: viru
 * Date: 13.11.12
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class Profile extends Controller {

	public static void index() {
		User user = User.find("byUsername", session.get("username")).first();
		List<UniversalPost> createdPosts = UniversalPost.find("byAuthor", user).fetch();

		renderArgs.put("user", user);
		renderArgs.put("createdPosts", createdPosts);
		render();
	}

}

package controllers;

import models.UniversalPost;
import models.User;
import models.enums.RatingType;
import models.enums.ResponseStatus;
import play.mvc.Before;
import play.mvc.Controller;
import services.PostService;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 16.11.12 Time: 13:02 To change
 * this template use File | Settings | File Templates.
 */
public class Rating extends Controller {
	@Before
	static void setConnectedUser() {

		if (Security.isConnected()) {
			User user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void ratePost(RatingType ratingType) {
		Long postId = Long.parseLong(session.get("postId"));
		UniversalPost universalPost = UniversalPost.findById(postId);
		User user = (User) renderArgs.get("user");
		ResponseStatus responseStatus = PostService.changeRating(universalPost, user, ratingType);
		switch (responseStatus) {
		case OK:
			Stelper.detailedPost(postId);
			break;
		default:
			error(responseStatus.getMessage());
		}
	}
}

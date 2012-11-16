package controllers;

import models.LangPost;
import models.UniversalPost;
import models.User;
import models.enums.PostType;
import models.exceptions.AccessViolationException;
import models.exceptions.DataValidationException;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import services.PostService;

/**
 * Created with IntelliJ IDEA. User: Mire1lle Date: 13.10.12 Time: 19:31 To
 * change this template use File | Settings | File Templates.
 */
@With(Secure.class)
public class AddPost extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void addSearchPost() {
		render();
	}

	public static void addOfferPost() {
		render();
	}

	public static void addUploadPost() {
		render();
	}

	public static void handleSubmit(@Valid LangPost langPost, PostType postType) {
		if (validation.hasErrors()) {
			switch (postType) {
			case OFFER:
				addOfferPost();
				break;
			case SEARCH:
				addSearchPost();
				break;
			case UPLOAD:
				addUploadPost();
				break;
			}
		}
		UniversalPost unPost = new UniversalPost();
		langPost.parentPost = unPost;
		unPost.addLangPost(langPost);
		unPost.type = postType;
		User author = renderArgs.get("user", User.class);
		try {
			if (PostService.createPost(unPost, author)) {
				System.out.println("success");
			} else {
				System.out.println("fail");
			}
		} catch (AccessViolationException e) {

		} catch (DataValidationException e) {

		}

		Stelper.stelper();
	}
}

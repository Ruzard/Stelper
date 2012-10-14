package controllers;

import models.Comment;
import models.CommentTree;
import models.UniversalPost;
import models.User;
import play.data.validation.Valid;
import play.mvc.*;
import services.PostService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: viru
 * Date: 13.10.12
 * Time: 20:37
 * To change this template use File | Settings | File Templates.
 */
@With(Secure.class)
public class Stelper extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void stelper() {
		List<UniversalPost> posts = UniversalPost.findAll();
		renderArgs.put("posts", posts);
		render("Stelper/posts.html");
	}

	public static void detailedPost(Long id) {
		UniversalPost post = UniversalPost.findById(id);
		if (post != null) {
			renderArgs.put("post", post);
			session.put("postId", id);
		}
		render();
	}

	public static void saveComment(@Valid Comment comment) {
		Long postId = Long.parseLong(session.get("postId"));
		if (validation.hasErrors()) {
			detailedPost(postId);
		}
		UniversalPost universalPost = UniversalPost.findById(postId);
		universalPost.posts.get(0).addComment(comment);
		detailedPost(postId);
	}
}

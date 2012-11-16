package controllers;

import java.util.List;

import models.Comment;
import models.CommentTree;
import models.LangPost;
import models.UniversalPost;
import models.User;
import models.exceptions.AccessViolationException;
import models.exceptions.PostException;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import services.PostService;

/**
 * Created with IntelliJ IDEA. User: viru Date: 13.10.12 Time: 20:37 To change
 * this template use File | Settings | File Templates.
 */
@With(Secure.class)
public class Stelper extends Controller {

	private static User user;

	@Before
	static void setConnectedUser() {

		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
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

	public static void saveCommentTree(@Valid Comment comment) {
		Long postId = Long.parseLong(session.get("postId"));
		if (validation.hasErrors()) {
			detailedPost(postId);
		}
		UniversalPost universalPost = UniversalPost.findById(postId);
		LangPost langPost = universalPost.posts.get(0);

		try {
			PostService.addPostComment(user, langPost, comment);
		} catch (AccessViolationException e) {
			error(e.getMessage());
		} catch (PostException e) {
			error(e.getMessage());
		}

		detailedPost(postId);
	}

	public static void saveSubComment(Long commentTreeId, Comment comment) {
		// session.remove("exception");
		Long postId = Long.parseLong(session.get("postId"));
		CommentTree commentTree = CommentTree.findById(commentTreeId);
		try {
			PostService.addSubComment(commentTree, comment, user);
		} catch (AccessViolationException e) {
			error(e.getMessage());
		} catch (PostException e) {
			error(e.getMessage());
		}
		detailedPost(postId);
	}
}

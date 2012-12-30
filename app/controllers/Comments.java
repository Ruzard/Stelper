package controllers;

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
import services.PostService;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 26.11.12 Time: 18:52 To change
 * this template use File | Settings | File Templates.
 */
public class Comments extends Controller {
	private static User user;

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void saveCommentTree(@Valid Comment comment) {
		Long postId = Long.parseLong(session.get("postId"));
		if (validation.hasErrors()) {
			Posts.detailedPost(postId);
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

		Posts.detailedPost(postId);
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
		Posts.detailedPost(postId);
	}
}

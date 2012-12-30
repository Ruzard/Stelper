package controllers;

import java.util.List;

import models.LangPost;
import models.UniversalPost;
import models.User;
import models.enums.PostType;
import models.exceptions.AccessViolationException;
import models.exceptions.DataValidationException;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import services.PostService;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 26.11.12 Time: 18:49 To change
 * this template use File | Settings | File Templates.
 */
public class Posts extends Controller {
	private static User user;

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}

	public static void frontPosts() {
		String notification = session.get("notification");
		if (notification != null) {
			session.remove("notification");
		}

		List<UniversalPost> posts = PostService.getVisiblePosts();
		renderArgs.put("posts", posts);
		renderArgs.put("message", notification);
		render(posts);
	}

	public static void detailedPost(Long id) {
		UniversalPost post = UniversalPost.findById(id);
		if (post != null) {
			renderArgs.put("post", post);
			session.put("postId", id);
		}
		render();
	}

	public static void createPost(PostType postType) {
		render(postType);
	}

	public static void createPostSubmit(@Valid LangPost langPost, PostType postType) {
		if (validation.hasErrors()) {
			createPost(postType);
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

		Main.main();
	}
}

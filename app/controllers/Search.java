package controllers;

import models.UniversalPost;
import models.User;
import models.enums.PostType;
import play.mvc.Before;
import play.mvc.Controller;
import services.SearchService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Search extends Controller {
	public static final String LANGUAGES = "languages";
	public static final String DATE = "date";
	public static final String TYPE = "type";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	private static User user;

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			user = User.find("byUsername", Security.connected()).first();
			if (user != null)
				renderArgs.put("user", user);
		}
	}


	public static void search() {
		render("search.html");
	}

	public static void searchSubmit(String date, PostType postType, String languages, String keywords) {
		Map<String, String> params = new HashMap();
		if (postType != null)
			params.put(TYPE, postType.name());
		if (date != null)
			params.put(DATE, date);
		if (languages != null)
			params.put(LANGUAGES, languages);
		Set<UniversalPost> posts = SearchService.findPost(params);
		renderArgs.put("posts", posts);
		render("search.html");
	}
}

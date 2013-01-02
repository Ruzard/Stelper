package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.UniversalPost;
import models.enums.PostStatus;
import models.enums.PostType;
import utils.PostFilterUtils;

public class SearchService {

	public static final String LANGUAGES = "languages";
	public static final String DATE = "date";
	public static final String TYPE = "type";
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	public static Set<UniversalPost> findPost(Map<String, String> params) {
		if (params == null) {
			throw new IllegalArgumentException("Parameter shoudn't be null");
		} else if (!params.containsKey(TYPE)) {
			throw new IllegalArgumentException("Post type should be specified");
		}

		List<UniversalPost> allPosts = null;
		PostType postType = PostType.valueOf(params.get(TYPE));
		if (params.containsKey(DATE)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			try {
				Date date = dateFormat.parse(params.get("date"));
				allPosts = UniversalPost.find("postedAt >= ? AND type = ? AND status = ?", date, postType,
						PostStatus.OPEN).fetch();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			allPosts = UniversalPost.find("type = ? AND status = ?", postType, PostStatus.OPEN).fetch();
		}

		if (allPosts == null) {
			return null;
		}

		//checking languages
		if (params.containsKey(LANGUAGES)) {
			String[] languages = params.get(LANGUAGES).split(",");
			PostFilterUtils.filterLanguages(allPosts, languages);
		}


		return new HashSet<UniversalPost>(allPosts);
	}

}

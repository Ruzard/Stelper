package services;

import models.UniversalPost;
import models.enums.PostStatus;
import models.enums.PostType;
import models.util.Pair;
import utils.PostFilterUtils;
import utils.SearchUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchService {

	public static final String LANGUAGES = "languages";
	public static final String DATE = "date";
	public static final String TYPE = "type";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String SEARCH_QUERY = "searchQuery";

	public static List<UniversalPost> findPosts(Map<String, String> params) {
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
				Date date = dateFormat.parse(params.get(DATE));
				allPosts = UniversalPost.find("postedAt >= ? AND type = ? AND status = ?", date, postType,
						PostStatus.OPEN).fetch();
			} catch (ParseException e) {
				throw new IllegalArgumentException("Wrong date format");
			}
		} else {
			allPosts = UniversalPost.find("type = ? AND status = ?", postType, PostStatus.OPEN).fetch();
		}

		//checking languages
		if (params.containsKey(LANGUAGES)) {
			String[] languages = params.get(LANGUAGES).split(", ");
			allPosts = PostFilterUtils.filterLanguages(allPosts, languages);
		}

		if (!params.containsKey(SEARCH_QUERY)) {
			return new ArrayList<UniversalPost>(allPosts);
		}

		String searchQuery = params.get(SEARCH_QUERY);

		List<Pair<Integer, UniversalPost>> postsWithRatings = new ArrayList<Pair<Integer,
				UniversalPost>>(allPosts.size());
		for (UniversalPost universalPost : allPosts) {
			int postValue = SearchUtils.calculatePostValue(universalPost, searchQuery);
			if (postValue != 0) {
				postsWithRatings.add(new Pair<Integer, UniversalPost>(postValue, universalPost));
			}
		}
		Collections.sort(postsWithRatings, new Comparator<Pair<Integer, UniversalPost>>() {
			@Override
			public int compare(Pair<Integer, UniversalPost> first, Pair<Integer,
					UniversalPost> second) {
				return first.fst.compareTo(second.fst);
			}
		});

		List<UniversalPost> posts = new LinkedList<UniversalPost>();
		for (Pair<Integer, UniversalPost> postsWithRating : postsWithRatings) {
			posts.add(postsWithRating.snd);
		}
		return posts;
	}

}

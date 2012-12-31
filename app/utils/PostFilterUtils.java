package utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import models.LangPost;
import models.UniversalPost;

public class PostFilterUtils {
	/**
	 * @param posts     posts to filter.
	 * @param languages accepted languages. Posts with other languages will be deleted.
	 * @return List of filtered posts.
	 */
	public static List<UniversalPost> filterLanguages(List<UniversalPost> posts, String[] languages) {
		if (languages.length == 0) {
			return posts;
		}

		List<UniversalPost> filteredPosts = new LinkedList<UniversalPost>(posts);
		Iterator<UniversalPost> postIterator = filteredPosts.iterator();
		while (postIterator.hasNext()) {
			if (PostFilterUtils.filterLanguages(postIterator.next(), languages) == null) {
				postIterator.remove();
			}
		}

		return filteredPosts;
	}


	/**
	 * @param universalPost universal post which will be filtered.
	 * @param languages     accepted languages
	 * @return null if post is not eligible, otherwise universal post with eligible languages.
	 */
	public static UniversalPost filterLanguages(UniversalPost universalPost, String[] languages) {
		Iterator<LangPost> langPostIterator = universalPost.posts.iterator();
		while (langPostIterator.hasNext()) {
			LangPost langPost = langPostIterator.next();
			if (!Arrays.asList(languages).contains(langPost.language.toString())) {
				langPostIterator.remove();
			}
		}

		return universalPost.posts.size() == 0 ? null : universalPost;
	}
}

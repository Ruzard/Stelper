package utils;

import java.util.Arrays;
import java.util.Iterator;

import models.LangPost;
import models.UniversalPost;

public class PostFilterUtils {
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

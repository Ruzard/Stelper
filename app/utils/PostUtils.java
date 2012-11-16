package utils;

import models.LangPost;
import models.UniversalPost;
import models.exceptions.DataValidationException;

public class PostUtils {

	public static void checkTags(UniversalPost post) throws DataValidationException {
		boolean tagsValid = true;
		for (LangPost langPost : post.posts) {
			if (langPost.tags == null || langPost.tags.size() == 0) {
				tagsValid = false;
				break;
			}

			for (String tag : langPost.tags) {
				if (tag.isEmpty()) {
					tagsValid = false;
					break;
				}
			}
		}
		if (!tagsValid)
			throw new DataValidationException("Tags are empty");
	}
}

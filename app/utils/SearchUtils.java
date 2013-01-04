package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.LangPost;
import models.UniversalPost;

public class SearchUtils {

	private static final String SPLIT_REGEX = "[,\\s\\.]+";
	private static final int TITLE_MULTIPLIER = 100;
	private static final int BODY_MULTIPLIER = 10;
	private static final int SHORT_WORD_LENGTH = 2;
	private static final int TAG_MULTIPLIER = 50;

	public static int calculatePostValue(UniversalPost universalPost, String query) {
		List<Integer> langPostValueList = new ArrayList<Integer>(universalPost.posts.size());
		for (LangPost langPost : universalPost.posts) {
			int value = 0;
			value += calculateValue(langPost.title, query) * TITLE_MULTIPLIER;
			value += calculateValue(langPost.body, query) * BODY_MULTIPLIER;
			value += calculateTagsValue(langPost.tags, query) * TAG_MULTIPLIER;
			langPostValueList.add(value);
		}
		return Collections.max(langPostValueList);
	}

	private static int calculateTagsValue(List<String> tags, String query) {
		return countSimilarities(tags.toArray(new String[tags.size()]), removeShortWords(query));
	}

	private static int calculateValue(String text, String query) {
		String[] sourceTextWords = removeShortWords(text);
		String[] queryWords = removeShortWords(query);
		return countSimilarities(sourceTextWords, queryWords);
	}

	private static int countSimilarities(String[] sourceWords, String[] queryWords) {
		int count = 0;
		for (String sourceTextWord : sourceWords) {
			for (String queryWord : queryWords) {
				if (sourceTextWord.equalsIgnoreCase(queryWord)) {
					count++;
				}
			}
		}
		return count;
	}

	private static String[] removeShortWords(String string) {
		String[] strings = string.split(SPLIT_REGEX);

		List<String> filteredString = new ArrayList<String>(strings.length);
		for (String stringToFilter : strings) {
			if (stringToFilter.length() > SHORT_WORD_LENGTH) {
				filteredString.add(stringToFilter);
			}
		}
		return filteredString.toArray(new String[filteredString.size()]);
	}
}

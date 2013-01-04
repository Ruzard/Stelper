package utils;

import java.util.Arrays;

import models.LangPost;
import models.UniversalPost;
import models.enums.Language;
import org.junit.Before;
import org.junit.Test;
import play.test.UnitTest;

public class SearchUtilsTest extends UnitTest {
	private UniversalPost universalPost;
	private LangPost eligibleLangPost;
	private LangPost ineligibleLangPost;

	@Before
	public void prepare() {
		universalPost = new UniversalPost();

		String[] tags = {"post", "first"};
		eligibleLangPost = new LangPost("First post", "body first blabla", Language.RU, Arrays.asList(tags));

		tags = new String[]{"no_match"};
		ineligibleLangPost = new LangPost("Another language", "only one match - post", Language.EN,
				Arrays.asList(tags));
	}

	@Test
	public void testWork() {
		universalPost.addLangPost(eligibleLangPost);
		int postValue = SearchUtils.calculatePostValue(universalPost, "first post");
		assertFalse(postValue == 0);
	}

	@Test
	public void onlyMaximumValueCounted() {
		universalPost.addLangPost(eligibleLangPost);
		int firstValue = SearchUtils.calculatePostValue(universalPost, "first post");

		universalPost.addLangPost(ineligibleLangPost);
		int secondValue = SearchUtils.calculatePostValue(universalPost, "first post");

		assertTrue("Post value should be taken from 1 language", firstValue == secondValue);
	}

	@Test
	public void tagsCounted() {
		universalPost.addLangPost(eligibleLangPost);
		int valueWithTags = SearchUtils.calculatePostValue(universalPost, "first post");

		eligibleLangPost.tags = Arrays.asList(new String[]{"no_tags"});
		int noTagsValue = SearchUtils.calculatePostValue(universalPost, "first post");

		assertTrue("Tags should be counted", valueWithTags > noTagsValue);
	}

	@Test
	public void emptyStringTest() {
		universalPost.addLangPost(eligibleLangPost);
		int postValue = SearchUtils.calculatePostValue(universalPost, "");
		assertEquals("Empty query should return 0", postValue, 0);
	}


}

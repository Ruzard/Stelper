package services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.javaws.exceptions.InvalidArgumentException;
import models.LangPost;
import models.UniversalPost;
import models.enums.Language;
import models.enums.PostType;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

import static models.enums.PostStatus.OPEN;
import static services.SearchService.DATE;
import static services.SearchService.DATE_FORMAT;

public class SearchServiceTests extends UnitTest {

	private Map<String, String> requestMap;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");
		requestMap = new HashMap<String, String>();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullPointerTest() throws InvalidArgumentException {
		Set<UniversalPost> post = SearchService.findPost(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void postTypeIsNeeded() throws InvalidArgumentException {
		SearchService.findPost(requestMap);
	}

	@Test
	public void typeFilterWorks() {
		requestMap.put(SearchService.TYPE, PostType.OFFER.toString());
		Set<UniversalPost> posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			assertEquals(universalPost.status, OPEN);
			assertEquals(universalPost.type, PostType.OFFER);
		}

		requestMap.put(SearchService.TYPE, PostType.SEARCH.toString());
		posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			assertEquals(universalPost.status, OPEN);
			assertEquals(universalPost.type, PostType.SEARCH);
		}

		requestMap.put(SearchService.TYPE, PostType.UPLOAD.toString());
		posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			assertEquals(universalPost.status, OPEN);
			assertEquals(universalPost.type, PostType.UPLOAD);
		}
	}

	@Test
	public void dateFilterWorks() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, Calendar.OCTOBER, 9); //09.10.2012

		Date date = calendar.getTime();
		requestMap.put(DATE, new SimpleDateFormat(DATE_FORMAT).format(date));

		requestMap.put(SearchService.TYPE, PostType.OFFER.toString());
		Set<UniversalPost> posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			assertTrue("Filter not working", date.before(universalPost.postedAt));
		}

		requestMap.put(SearchService.TYPE, PostType.SEARCH.toString());
		posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			assertTrue("Filter not working", date.before(universalPost.postedAt));
		}

		requestMap.put(SearchService.TYPE, PostType.UPLOAD.toString());
		posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			assertTrue("Filter not working", date.before(universalPost.postedAt));
		}
	}

	@Test
	public void languageFilterWorks() {
		requestMap.put(SearchService.LANGUAGES, Language.ET.toString() + "," + Language.RU);

		requestMap.put(SearchService.TYPE, PostType.OFFER.toString());
		Set<UniversalPost> posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			for (LangPost post : universalPost.posts) {
				assertTrue(post.language == Language.ET || post.language == Language.RU);
			}
		}

		requestMap.put(SearchService.TYPE, PostType.SEARCH.toString());
		posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			for (LangPost post : universalPost.posts) {
				assertTrue(post.language == Language.ET || post.language == Language.RU);
			}
		}

		requestMap.put(SearchService.TYPE, PostType.UPLOAD.toString());
		posts = SearchService.findPost(requestMap);
		for (UniversalPost universalPost : posts) {
			for (LangPost post : universalPost.posts) {
				assertTrue(post.language == Language.ET || post.language == Language.RU);
			}
		}
	}
}

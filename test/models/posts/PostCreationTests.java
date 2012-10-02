package models.posts;

import java.util.*;

import models.*;
import models.enums.*;
import models.exceptions.*;
import org.junit.*;
import play.test.*;
import services.PostService;

public class PostCreationTests extends UnitTest {

	private UniversalPost universalPost;

	private List<String> tags;
	private User user;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		user = User.find("byUsername", "Bob").first();
		tags = new ArrayList<String>() {{
			add("first tag");
			add("second tag");
		}};

		//not stored post
		universalPost = new UniversalPost();
		universalPost.addLangPost(new LangPost("Title", "Body", Language.EN, tags));
	}

	@Test
	public void postSuccessCreation() throws AccessViolationException, DataValidationException {
		UniversalPost universalPost = new UniversalPost();
		universalPost.type = PostType.SEARCH;
		universalPost.addLangPost(new LangPost("Title", "Body", Language.EN, tags));

		long initialPostCount = UniversalPost.count();
		assertTrue(PostService.createPost(universalPost, user));

		long expectedPostCount = initialPostCount + 1;
		assertTrue(expectedPostCount == UniversalPost.count());
		assertNotSame(0, user.posts.size());
	}

	@Test(expected = DataValidationException.class)
	public void postCreationTagsMissing() throws AccessViolationException, DataValidationException {
		UniversalPost universalPost = new UniversalPost();
		universalPost.addLangPost(new LangPost("Title", "Body", Language.EN, null));

		long initialPostCount = UniversalPost.count();
		PostService.createPost(universalPost, user);

		assertTrue(initialPostCount == UniversalPost.count());
	}

	@Test(expected = DataValidationException.class)
	public void creationWithoutContent() throws AccessViolationException, DataValidationException {
		UniversalPost universalPost = new UniversalPost();

		long initialPostCount = UniversalPost.count();

		PostService.createPost(universalPost, user);
		assertEquals(initialPostCount, UniversalPost.count());
	}

	@Test(expected = AccessViolationException.class)
	public void frozenUserPostCreation() throws AccessViolationException, DataValidationException {
		long initialPostCount = UniversalPost.count();

		User bannedUser = User.find("byStatus", UserStatus.FROZEN).first();
		createSamplePost(bannedUser);

		assertTrue(initialPostCount == UniversalPost.count());
	}

	@Test(expected = AccessViolationException.class)
	public void bannedUserPostCreation() throws AccessViolationException, DataValidationException {
		long initialPostCount = UniversalPost.count();

		User bannedUser = User.find("byStatus", UserStatus.BANNED).first();
		createSamplePost(bannedUser);

		assertTrue(initialPostCount == UniversalPost.count());
	}


	private void createSamplePost(User author) throws AccessViolationException, DataValidationException {
		UniversalPost postFirst = new UniversalPost();
		postFirst.addLangPost(new LangPost("Title", "Body", Language.EN, tags));

		PostService.createPost(postFirst, author);
	}
}

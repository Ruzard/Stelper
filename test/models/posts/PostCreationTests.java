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

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

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
		User user = User.find("byUsername", "Bob").first();
		UniversalPost universalPost = new UniversalPost();
		universalPost.addLangPost(new LangPost("Title", "Body", Language.EN, tags));

		UniversalPost createdPost = null;
		createdPost = PostService.createPost(universalPost, user);

		assertNotSame(0, user.posts.size());
		assertNotNull(createdPost);
		assertNotNull(createdPost.postedAt);
		assertNotSame(0, createdPost.posts.size());
		assertNotNull(createdPost.author);
		assertNotNull(createdPost.rating);
		assertEquals(PostStatus.OPEN, createdPost.status);
	}

	@Test(expected = DataValidationException.class)
	public void creationWithoutContent() throws AccessViolationException, DataValidationException {
		User user = User.find("byUsername", "Bob").first();
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

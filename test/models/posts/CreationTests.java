package models.posts;

import models.*;
import models.enums.*;
import org.junit.*;
import play.test.*;
import services.PostService;

public class CreationTests extends UnitTest {

	private UniversalPost universalPost;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		//not stored post
		universalPost = new UniversalPost();
		universalPost.addLangPost(new LangPost("Title", "Body", Language.EN));
	}

	@Test
	public void bannedUserPostCreation() {
		User user = User.find("byUsername", "BannedBob").first();

		ResponseStatus response = PostService.createPost(universalPost, user);
		assertEquals(ResponseStatus.ACCESS_VIOLATION, response);
	}

	@Test
	public void postCreation() {
		User user = User.find("byUsername", "Bob").first();
		PostService.createPost(universalPost, user);

		UniversalPost post = UniversalPost.find("byAuthor", user).first();
		assertNotNull(post);
		assertNotNull(post.postedAt);
		assertEquals(PostStatus.OPEN, post.status);
	}

	@Test
	public void restrictedUserPostCreation() {
		UniversalPost postFirst = new UniversalPost();
		postFirst.addLangPost(new LangPost("Title", "Body", Language.EN));

		long initialPostCount = UniversalPost.count();

		User bannedUser = User.find("byStatus", UserStatus.BANNED).first();
		ResponseStatus firstStatus = PostService.createPost(postFirst, bannedUser);
		assertEquals(ResponseStatus.ACCESS_VIOLATION, firstStatus);

		UniversalPost postSecond = new UniversalPost();
		postSecond.addLangPost(new LangPost("Title", "Body", Language.ET));

		User frozenUser = User.find("byStatus", UserStatus.FROZEN).first();
		ResponseStatus secondStatus = PostService.createPost(postSecond, frozenUser);
		assertEquals(ResponseStatus.ACCESS_VIOLATION, secondStatus);

		assertTrue(initialPostCount == UniversalPost.count());
	}
}

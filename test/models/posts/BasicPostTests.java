package models.posts;

import models.*;
import models.enums.*;
import org.junit.*;
import play.test.*;
import services.PostService;

import static models.enums.RatingType.NEUTRAL;
import static models.enums.RatingType.POSITIVE;
import static models.enums.ResponseStatus.OK;
import static models.enums.ResponseStatus.RATING_ALREADY_CHANGED;

public class BasicPostTests extends UnitTest {
	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");
	}

	@Test()
	public void bannedUserPostCreation() {
		UniversalPost universalPost = UniversalPost.all().first();
		User user = User.find("byUsername", "BannedBob").first();
		ResponseStatus response = PostService.createPost(universalPost, user);
		assertEquals(ResponseStatus.ACCESS_VIOLATION, response);
	}

	@Test
	public void postCreation() {
		UniversalPost universalPost = UniversalPost.all().first();
		User user = User.find("byUsername", "Bob").first();
		PostService.createPost(universalPost, user);

		UniversalPost post = UniversalPost.find("byAuthor", user).first();
		assertNotNull(post);
		assertNotNull(post.postedAt);
		assertEquals(PostStatus.OPEN, post.status);
	}

	@Test
	public void changePostRating() {
		UniversalPost post = UniversalPost.all().first();
		User initiator = User.find("byUsername", "Commenter").first();

		int positiveRating = post.rating.positive;
		int neutralRating = post.rating.neutral;
		int negativeRating = post.rating.negative;

		PostService.changeRating(post, initiator, POSITIVE);

		UniversalPost changedPost = UniversalPost.all().first();

		assertNotSame(positiveRating, changedPost.rating.positive);
		assertSame(neutralRating, changedPost.rating.neutral);
		assertSame(negativeRating, changedPost.rating.negative);
	}

	@Test
	public void failedChangePostRating() {
		UniversalPost post = UniversalPost.all().first();
		User initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus status = PostService.changeRating(post, initiator, NEUTRAL);
		assertEquals(ResponseStatus.RATING_NEUTRAL_EXCEPTION, status);
	}

	@Test
	public void failedOverusingPostRating() {
		UniversalPost post = UniversalPost.all().first();
		assertEquals(0, post.rating.negative);
		assertEquals(0, post.rating.neutral);
		assertEquals(0, post.rating.negative);


		User initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusFirst = PostService.changeRating(post, initiator, POSITIVE);
		assertEquals(OK, statusFirst);
		UniversalPost firstChange = UniversalPost.findById(post.id);
		assertEquals(1, firstChange.rating.positive);
		assertEquals(1, firstChange.rating.positive);
		assertEquals(0, firstChange.rating.neutral);

		initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusSecond = PostService.changeRating(firstChange, initiator, POSITIVE);
		assertEquals(RATING_ALREADY_CHANGED, statusSecond);

		UniversalPost secondChange = UniversalPost.findById(firstChange.id);
		assertEquals(1, secondChange.rating.positive);
		assertEquals(0, secondChange.rating.neutral);
		assertEquals(0, secondChange.rating.negative);

	}

}

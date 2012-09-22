package models.posts;

import models.*;
import models.enums.*;
import org.junit.*;
import play.test.*;
import services.PostService;

import static models.enums.RatingType.*;
import static models.enums.ResponseStatus.OK;
import static models.enums.ResponseStatus.RATING_ALREADY_CHANGED;

public class PostRatingTests extends UnitTest {

	private UniversalPost universalPost;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		//stored post
		universalPost = UniversalPost.find("byType", PostType.UPLOAD).first();
	}

	@Test
	public void changePostRating() {
		assertTrue(assertRatingEquals(0, 0, 0, universalPost.rating));

		User initiator = User.find("byUsername", "Commenter").first();
		PostService.changeRating(universalPost, initiator, NEGATIVE);

		UniversalPost changedPost = UniversalPost.all().first();
		assertTrue(assertRatingEquals(0, 0, 1, changedPost.rating));
	}

	@Test
	public void failedChangePostRatingNeutral() {
		User initiator = User.find("byUsername", "Commenter").first();

		assertTrue(assertRatingEquals(0, 0, 0, universalPost.rating));

		ResponseStatus status = PostService.changeRating(universalPost, initiator, NEUTRAL);
		assertEquals(ResponseStatus.RATING_NEUTRAL_EXCEPTION, status);

		assertTrue(assertRatingEquals(0, 0, 0, universalPost.rating));
	}

	@Test
	public void failedOverusingPostRating() {
		assertTrue(assertRatingEquals(0, 0, 0, universalPost.rating));

		User initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusFirst = PostService.changeRating(universalPost, initiator, POSITIVE);
		assertEquals("Status should be OK", OK, statusFirst);

		UniversalPost firstChange = UniversalPost.findById(universalPost.id);
		assertTrue("Rating should have changed", assertRatingEquals(1, 0, 0, firstChange.rating));

		initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusSecond = PostService.changeRating(firstChange, initiator, POSITIVE);
		assertEquals("Status should be RATING_ALREADY_CHANGED", RATING_ALREADY_CHANGED, statusSecond);

		UniversalPost secondChange = UniversalPost.findById(firstChange.id);
		assertTrue("Rating should not have changed", assertRatingEquals(1, 0, 0, secondChange.rating));
	}

	@Test
	public void rateOfferPost() {
		universalPost.type = PostType.OFFER;
		assertTrue("Post should have been created", universalPost.validateAndSave());

		User user = User.find("byStatus", UserStatus.ACTIVE).first();

		assertTrue("Rating should be null initially", assertRatingEquals(0, 0, 0, universalPost.rating));
		PostService.changeRating(universalPost, user, RatingType.POSITIVE);
		assertTrue("Rating should not be changed", assertRatingEquals(0, 0, 0, universalPost.rating));
	}

	private boolean assertRatingEquals(int positive, int neutral, int negative, Rating rating) {
		boolean positiveEquals = positive == rating.positive;
		boolean neutralEquals = neutral == rating.neutral;
		boolean negativeEquals = negative == rating.negative;

		return positiveEquals && neutralEquals && negativeEquals;
	}


}

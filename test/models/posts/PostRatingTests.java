package models.posts;

import models.Rating;
import models.UniversalPost;
import models.User;
import models.enums.PostType;
import models.enums.RatingType;
import models.enums.ResponseStatus;
import models.enums.UserStatus;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;
import services.PostService;

import static models.enums.RatingType.NEGATIVE;
import static models.enums.RatingType.NEUTRAL;
import static models.enums.RatingType.POSITIVE;
import static models.enums.ResponseStatus.OK;
import static models.enums.ResponseStatus.RATING_ALREADY_CHANGED;

public class PostRatingTests extends UnitTest {

	private UniversalPost universalPost;
	private User changeInitiator;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		changeInitiator = User.find("byUsername", "Commenter").first();

		//stored post
		universalPost = UniversalPost.find("byType", PostType.UPLOAD).first();
	}

	@Test
	public void changePostRating() {
		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));
		PostService.changeRating(universalPost, changeInitiator, NEGATIVE);
		assertTrue("Post rating should be changed", isRatingEqual(0, 0, 1, universalPost.rating));
	}

	@Test
	public void changePostRatingNullPointer() {
		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));
		PostService.changeRating(universalPost, changeInitiator, null);
		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));

		PostService.changeRating(universalPost, null, POSITIVE);
		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));

		assertEquals(ResponseStatus.DATA_VALIDATION_EXCEPTION, PostService.changeRating(null, null, null));
	}

	@Test
	public void changePostRatingNeutral() {
		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));

		ResponseStatus status = PostService.changeRating(universalPost, changeInitiator, NEUTRAL);
		assertEquals(ResponseStatus.RATING_NEUTRAL_EXCEPTION, status);

		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));
	}

	@Test
	public void failedOverusingPostRating() {
		assertTrue(isRatingEqual(0, 0, 0, universalPost.rating));

		ResponseStatus statusFirst = PostService.changeRating(universalPost, changeInitiator, POSITIVE);
		assertEquals("Status should be OK", OK, statusFirst);

		UniversalPost firstChange = UniversalPost.findById(universalPost.id);
		assertTrue("Rating should have changed", isRatingEqual(1, 0, 0, firstChange.rating));

		ResponseStatus statusSecond = PostService.changeRating(firstChange, changeInitiator, POSITIVE);
		assertEquals("Status should be RATING_ALREADY_CHANGED", RATING_ALREADY_CHANGED, statusSecond);

		UniversalPost secondChange = UniversalPost.findById(firstChange.id);
		assertTrue("Rating should not have changed", isRatingEqual(1, 0, 0, secondChange.rating));
	}

	@Test
	public void rateOfferPost() {
		universalPost = UniversalPost.find("byType", PostType.OFFER).first();
		User user = User.find("byStatus", UserStatus.ACTIVE).first();

		assertTrue("Rating should be null initially", isRatingEqual(0, 0, 0, universalPost.rating));
		PostService.changeRating(universalPost, user, RatingType.POSITIVE);
		assertTrue("Rating should be changed", isRatingEqual(1, 0, 0, universalPost.rating));
	}

	private boolean isRatingEqual(int positive, int neutral, int negative, Rating rating) {
		boolean positiveEquals = positive == rating.positive;
		boolean neutralEquals = neutral == rating.neutral;
		boolean negativeEquals = negative == rating.negative;

		return positiveEquals && neutralEquals && negativeEquals;
	}


}

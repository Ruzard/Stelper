package models.posts;

import models.*;
import models.enums.ResponseStatus;
import org.junit.*;
import play.test.*;
import services.PostService;

import static models.enums.RatingType.NEUTRAL;
import static models.enums.RatingType.POSITIVE;
import static models.enums.ResponseStatus.OK;
import static models.enums.ResponseStatus.RATING_ALREADY_CHANGED;

public class PostRatingTests extends UnitTest {

	private UniversalPost universalPost;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		//stored post
		universalPost = UniversalPost.all().first();
	}

	@Test
	public void changePostRating() {
		assertTrue(assertRatingEquals(0, 0, 0, universalPost.rating));

		User initiator = User.find("byUsername", "Commenter").first();
		PostService.changeRating(universalPost, initiator, POSITIVE);

		UniversalPost changedPost = UniversalPost.all().first();
		assertTrue(assertRatingEquals(1, 0, 0, changedPost.rating));
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
		assertEquals(OK, statusFirst);

		UniversalPost firstChange = UniversalPost.findById(universalPost.id);
		assertTrue(assertRatingEquals(1, 0, 0, firstChange.rating));

		initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusSecond = PostService.changeRating(firstChange, initiator, POSITIVE);
		assertEquals(RATING_ALREADY_CHANGED, statusSecond);

		UniversalPost secondChange = UniversalPost.findById(firstChange.id);
		assertTrue(assertRatingEquals(1, 0, 0, secondChange.rating));

	}

	private boolean assertRatingEquals(int positive, int neutral, int negative, Rating rating) {
		boolean positiveEquals = positive == rating.positive;
		boolean neutralEquals = neutral == rating.neutral;
		boolean negativeEquals = negative == rating.negative;

		return positiveEquals && neutralEquals && negativeEquals;
	}


}

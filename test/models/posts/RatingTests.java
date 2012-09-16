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

public class RatingTests extends UnitTest {

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
		User initiator = User.find("byUsername", "Commenter").first();

		int positiveRating = universalPost.rating.positive;
		int neutralRating = universalPost.rating.neutral;
		int negativeRating = universalPost.rating.negative;

		PostService.changeRating(universalPost, initiator, POSITIVE);

		UniversalPost changedPost = UniversalPost.all().first();

		assertNotSame(positiveRating, changedPost.rating.positive);
		assertSame(neutralRating, changedPost.rating.neutral);
		assertSame(negativeRating, changedPost.rating.negative);
	}

	@Test
	public void failedChangePostRatingNeutral() {
		User initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus status = PostService.changeRating(universalPost, initiator, NEUTRAL);
		assertEquals(ResponseStatus.RATING_NEUTRAL_EXCEPTION, status);
	}

	@Test
	public void failedOverusingPostRating() {
		assertEquals(0, universalPost.rating.negative);
		assertEquals(0, universalPost.rating.neutral);
		assertEquals(0, universalPost.rating.negative);


		User initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusFirst = PostService.changeRating(universalPost, initiator, POSITIVE);
		assertEquals(OK, statusFirst);
		UniversalPost firstChange = UniversalPost.findById(universalPost.id);
		assertEquals(1, firstChange.rating.positive);
		assertEquals(0, firstChange.rating.neutral);
		assertEquals(0, firstChange.rating.negative);

		initiator = User.find("byUsername", "Commenter").first();
		ResponseStatus statusSecond = PostService.changeRating(firstChange, initiator, POSITIVE);
		assertEquals(RATING_ALREADY_CHANGED, statusSecond);

		UniversalPost secondChange = UniversalPost.findById(firstChange.id);
		assertEquals(1, secondChange.rating.positive);
		assertEquals(0, secondChange.rating.neutral);
		assertEquals(0, secondChange.rating.negative);

	}

}

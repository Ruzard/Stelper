package models.userTests;

import models.UniversalPost;
import models.User;
import models.enums.PostType;
import models.enums.RatingType;
import models.enums.UserStatus;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;
import services.PostService;

public class UserRatingTests extends UnitTest {
	private UniversalPost universalPost;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		universalPost = UniversalPost.find("byType", PostType.UPLOAD).first();
	}

	@Test
	public void checkUserRatingPositiveChanges() {
		universalPost = UniversalPost.find("byType", PostType.OFFER).first();
		User user = User.find("byStatus", UserStatus.ACTIVE).first();

		int baseValue = universalPost.author.postRating.positive;
		PostService.changeRating(universalPost, user, RatingType.POSITIVE);
		assertEquals("User post rating should be changed", baseValue + 1, universalPost.author.postRating.positive);
	}

}

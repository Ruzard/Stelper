package services;

import models.UniversalPost;
import models.enums.PostStatus;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

public class PostServiceTests extends UnitTest {

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");
	}

	@Test
	public void visiblePostsLoadingTest() {
		int countBeforeClosingPost = PostService.getVisiblePosts().size();
		UniversalPost firstPost = UniversalPost.find("byStatus", PostStatus.OPEN).first();
		firstPost.status = PostStatus.FROZEN;

		int countAfterClosingPost = PostService.getVisiblePosts().size();
		assertFalse(countAfterClosingPost == countBeforeClosingPost);
	}
}

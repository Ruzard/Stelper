package models.comments;

import models.*;
import models.enums.UserStatus;
import models.exceptions.*;
import org.junit.*;
import play.test.*;
import services.PostService;

public class CommentCreationTests extends UnitTest {
	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		langPost = (LangPost) LangPost.findAll().get(0);

		comment = new Comment();
		comment.body = "body";
	}

	private LangPost langPost;
	private Comment comment;

	@Test
	public void createParentComment() throws AccessViolationException {
		User author = User.find("byStatus", UserStatus.ACTIVE).first();

		long initialCommentsCount = Comment.count();
		PostService.addPostComment(author, langPost, comment);

		assertEquals("Comment has not been created", initialCommentsCount + 1, Comment.count());
		assertNotSame("Comment has not been assigned to the post", 1, langPost.comments.size());
	}

	@Test(expected = AccessViolationException.class)
	public void frozenUserCommentCreation() throws AccessViolationException, DataValidationException {
		User frozenUser = User.find("byStatus", UserStatus.FROZEN).first();
		PostService.addPostComment(frozenUser, langPost, comment);
	}

	@Test(expected = AccessViolationException.class)
	public void bannedUserCommentCreation() throws AccessViolationException, DataValidationException {
		User bannedUser = User.find("byStatus", UserStatus.BANNED).first();
		PostService.addPostComment(bannedUser, langPost, comment);
	}
}

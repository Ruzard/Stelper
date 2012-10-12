package models.comments;

import models.*;
import models.enums.UserStatus;
import models.exceptions.*;
import org.junit.*;
import play.test.*;
import services.PostService;

public class CommentCreationTests extends UnitTest {

	private User activeUser;
	private LangPost langPost;
	private Comment comment;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		activeUser = User.find("byStatus", UserStatus.ACTIVE).first();
		langPost = LangPost.all().first();

		comment = new Comment();
		comment.body = "body";
		comment.author = activeUser;
	}


	@Test
	public void addCommentBasic() throws AccessViolationException {
		long initialCommentCount = Comment.count();
		long initialPostComments = langPost.comments.size();
		langPost.addComment(comment);
		assertNotSame("comment hasn't been added to the database", initialCommentCount,
				Comment.count());
		assertFalse("comment hasn't been added to the post", initialPostComments == langPost.comments.size());
	}

	@Test
	public void createParentComment() throws AccessViolationException, PostException {

		long initialCommentsCount = Comment.count();

		long initialPostCommentsCount = langPost.comments.size();
		PostService.addPostComment(activeUser, langPost, comment);

		assertEquals("Comment has not been created", initialCommentsCount + 1, Comment.count());
		assertTrue("Comment has not been assigned to the post", initialPostCommentsCount + 1 == langPost.comments
				.size());
	}

	@Test(expected = AccessViolationException.class)
	public void frozenUserCommentCreation() throws AccessViolationException, DataValidationException, PostException {
		User frozenUser = User.find("byStatus", UserStatus.FROZEN).first();
		PostService.addPostComment(frozenUser, langPost, comment);
	}

	@Test(expected = AccessViolationException.class)
	public void bannedUserCommentCreation() throws AccessViolationException, DataValidationException, PostException {
		User bannedUser = User.find("byStatus", UserStatus.BANNED).first();
		PostService.addPostComment(bannedUser, langPost, comment);
	}

	@Test(expected = PostException.class)
	public void closedPostCommentCreation() throws AccessViolationException, PostException {
		LangPost languagePost = LangPost.find("byTitle", "ClosedPost").first();
		User user = User.find("byUsername", "Commenter").first();

		PostService.addPostComment(user, languagePost, comment);
	}
}

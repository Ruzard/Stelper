package models.comments;

import models.*;
import models.enums.*;
import models.exceptions.*;
import org.junit.*;
import play.test.*;
import services.PostService;

public class AdvancedCommentTests extends UnitTest {

	private LangPost langPost;
	private Comment comment;
	private CommentTree commentTree;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		comment = new Comment();
		comment.body = "New comment";

		langPost = LangPost.find("byTitle", "PostWithComments").first();
		commentTree = CommentTree.find("byParentPost", langPost).first();
	}

	@Test
	public void basicAddSubComment() throws AccessViolationException, PostException {
		long initialCommentCount = Comment.count();
		PostService.addSubComment(commentTree, comment, langPost.parentPost.author);
		long count = Comment.count();
		assertTrue(initialCommentCount != count);
	}

	@Test(expected = PostException.class)
	public void addSubCommentWrongUser() throws AccessViolationException, PostException {
		User absoluteGuest = User.find("byUsername", "AbsoluteGuest").first();
		PostService.addSubComment(commentTree, comment, absoluteGuest);
		//User should not be able to add subcomment to somebody else's comment trees
	}

	@Test(expected = AccessViolationException.class)
	public void addSubCommentBannedUser() throws AccessViolationException, PostException {
		User bannedUser = User.find("byStatus", UserStatus.BANNED).first();
		PostService.addSubComment(commentTree, comment, bannedUser);
	}

	@Test(expected = AccessViolationException.class)
	public void addSubCommentFrozenUser() throws AccessViolationException, PostException {
		User bannedUser = User.find("byStatus", UserStatus.FROZEN).first();
		PostService.addSubComment(commentTree, comment, bannedUser);
	}

	@Test(expected = PostException.class)
	public void addSubCommentWrongPostType() throws AccessViolationException, PostException {
		UniversalPost post = UniversalPost.find("byType", PostType.UPLOAD).first();
		langPost = post.posts.get(0);
		PostService.addSubComment(langPost.comments.get(0), comment, post.author);
	}
}

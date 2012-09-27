package models.comments;

import models.*;
import org.junit.*;
import play.test.*;
import services.PostService;

public class AdvancedCommentTests extends UnitTest {

	private LangPost post;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		post = LangPost.find("byTitle", "PostWithComments").first();
	}

	@Test
	public void basicAddSubComment() {
		User postAuthor = post.parentPost.author;
		CommentTree commentTree = CommentTree.find("byParentPost", post).first();

		Comment comment = new Comment();
		comment.author = postAuthor;
		comment.body = "New comment";

		long initialCommentCount = Comment.count();
		PostService.addSubComment(commentTree, comment, postAuthor);
		long count = Comment.count();
		assertTrue(initialCommentCount != count);
//		assertTrue(true);
	}
}

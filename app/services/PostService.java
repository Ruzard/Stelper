package services;

import models.*;
import models.enums.*;
import models.exceptions.*;
import utils.AccessValidation;

import static models.enums.ResponseStatus.*;

public class PostService {
	public static UniversalPost createPost(UniversalPost post, User author) throws AccessViolationException,
			DataValidationException {
		checkAccess(author, "You are not allowed to create posts");

		if (post.posts.isEmpty()) {
			throw new DataValidationException(post + " missing translated posts");
		}
		post.author = author;
		return post.save();
	}

	public static ResponseStatus changeRating(UniversalPost post, User initiator, RatingType changeType) {
		ActivityHistory history = initiator.history;
		if (history.ratedPosts.contains(post)) {
			return RATING_ALREADY_CHANGED;
		}

		if (post.type != PostType.UPLOAD) {
			return RATING_EXCEPTION;
		}

		switch (changeType) {
			case POSITIVE:
				post.rating.positive++;
				break;
			case NEGATIVE:
				post.rating.negative++;
				break;
			case NEUTRAL:
				return RATING_NEUTRAL_EXCEPTION;
		}

		post.save();

		initiator.history.ratedPosts.add(post);
		initiator.save();

		return OK;
	}

	public static boolean addPostComment(User author, LangPost post, Comment comment) throws AccessViolationException {
		checkAccess(author, "You are not allowed to comment");
		comment.author = author;
		return post.addComment(comment);
	}


	public static boolean addSubComment(CommentTree commentTree, Comment comment,
	                                    User author) throws AccessViolationException, PostException {
		checkAccess(author, "You are not allowed to add subComment");
		UniversalPost parentPost = commentTree.parentPost.parentPost;
		if (parentPost.type != PostType.SEARCH) {
			throw new PostException("No subcomments are allowed in posts different from SEARCH");
		}

		User postAuthor = parentPost.author;
		User commentTreeAuthor = commentTree.comments.get(0).author;
		if (!(commentTreeAuthor == author ^ postAuthor == author)) {
			throw new PostException("Only post author and comment creator are able to discuss in subcomments");
		}

		comment.author = author;
		return commentTree.addComment(comment);
	}


	private static void checkAccess(User author, String message) throws AccessViolationException {
		if (!AccessValidation.commentCreationAllowed(author)) {
			throw new AccessViolationException(message + "\nAuthor status:" + author.status);
		}
	}
}

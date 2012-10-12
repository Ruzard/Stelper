package services;

import models.*;
import models.enums.*;
import models.exceptions.*;
import utils.AccessValidation;

import static models.enums.ResponseStatus.*;

public class PostService {
	public static boolean createPost(UniversalPost post, User author) throws AccessViolationException,
			DataValidationException {
		checkAccess(author, "You are not allowed to create posts");

		if (post.posts.isEmpty()) {
			throw new DataValidationException(post + " missing translated posts");
		}
		checkTags(post);


		post.author = author;
		return post.validateAndSave();
	}

	private static void checkTags(UniversalPost post) throws DataValidationException {
		boolean tagsValid = true;
		for (LangPost langPost : post.posts) {
			if (langPost.tags == null) {
				tagsValid = false;
				break;
			}
			if (langPost.tags.size() == 0) {
				tagsValid = false;
				break;
			}

			for (String tag : langPost.tags) {
				if (tag.isEmpty()) {
					tagsValid = false;
					break;
				}
			}
		}
		if (!tagsValid)
			throw new DataValidationException("Tags are empty");
	}

	public static ResponseStatus changeRating(UniversalPost post, User initiator, RatingType changeType) {
		if (changeType == null || initiator == null || post == null) {
			return ResponseStatus.DATA_VALIDATION_EXCEPTION;
		}
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

	public static boolean addPostComment(User author, LangPost post, Comment comment) throws AccessViolationException,
			PostException {
		if (author == null || post == null || comment == null) {
			return false;
		}
		checkAccess(author, "You are not allowed to comment");
		checkStatus(post.parentPost);
		comment.author = author;
		return post.addComment(comment);
	}

	private static void checkStatus(UniversalPost parentPost) throws PostException {
		if (parentPost.status != PostStatus.OPEN) {
			throw new PostException("Only open posts can be commented");
		}
	}


	public static boolean addSubComment(CommentTree commentTree, Comment comment,
	                                    User author) throws AccessViolationException, PostException {
		checkAccess(author, "You are not allowed to add sub comment");
		UniversalPost parentPost = commentTree.parentPost.parentPost;
		if (parentPost.type != PostType.SEARCH) {
			throw new PostException("No sub comments are allowed in posts different from SEARCH");
		}

		User postAuthor = parentPost.author;
		User commentTreeAuthor = commentTree.comments.get(0).author;
		if (!(commentTreeAuthor == author ^ postAuthor == author)) {
			throw new PostException("Only post author and comment creator are able to discuss in sub comments");
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

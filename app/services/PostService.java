package services;

import models.*;
import models.enums.*;
import models.exceptions.*;
import utils.AccessValidation;

import static models.enums.ResponseStatus.*;

public class PostService {
	public static UniversalPost createPost(UniversalPost post, User author) throws AccessViolationException,
			DataValidationException {

		if (!AccessValidation.postCreationAllowed(author)) {
			throw new AccessViolationException("You are not allowed to post with status " + author.status);
//			return ACCESS_VIOLATION.specifyMessage();
		}

		if (post.posts.isEmpty()) {
			throw new DataValidationException(post + " missing translated posts");
//			return DATA_VALIDATION_EXCEPTION.specifyMessage(post + " missing translated posts");
		}
		post.author = author;
		return post.save();
	}

	public static ResponseStatus changeRating(UniversalPost post, User initiator, RatingType changeType) {
		ActivityHistory history = initiator.history;
		if (history.ratedPosts.contains(post)) {
			return RATING_ALREADY_CHANGED;
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
}

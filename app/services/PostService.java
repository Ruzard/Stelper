package services;

import models.*;
import models.enums.*;
import play.Logger;

import static models.enums.ResponseStatus.*;

public class PostService {
	public static ResponseStatus createPost(UniversalPost post, User author) {
		switch (author.status) {
			case FROZEN:
			case BANNED:
				Logger.debug(author.username + " has tried to create a post being" + author.status);
				return ACCESS_VIOLATION;
			case ACTIVE:
				post.author = author;
				post.save();
				return OK;
		}
		Logger.error("Rollback for " + post);
		post.delete();
		return ROLLBACK_OCCURED;
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

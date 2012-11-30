package services;

import java.util.List;

import models.PrivateConversation;
import models.PrivateMessage;
import models.User;
import models.enums.ExceptionType;
import models.exceptions.PrivateMessageException;

public class MessageService {
	public static boolean sendMessage(User author, User receiver, String message) throws PrivateMessageException {
		if (message == null || message.equals("")) {
			return false;
		}

		if (receiver == null || author == null) {
			return false;
		}

		if (User.findById(receiver.id) == null) {
			String errorMessage = "User " + receiver.username + " was not found";
			throw new PrivateMessageException(ExceptionType.USER_NOT_FOUND, errorMessage);
		}

		PrivateConversation conversation = PrivateConversation.find("byFirstUserAndSecondUser", author, receiver)
				.first();
		if (conversation == null) {
			conversation = PrivateConversation.find("byFirstUserAndSecondUser", receiver, author).first();
			if (conversation == null) {
				conversation = new PrivateConversation(author, receiver);
			}
		}
		return conversation.addMessage(new PrivateMessage(author, message));
	}

	public static List<PrivateConversation> getPrivateConversations(User user) {
		List<PrivateConversation> firstPart = PrivateConversation.find("byFirstUser", user).fetch();
		List<PrivateConversation> secondPart = PrivateConversation.find("bySecondUser", user).fetch();
		firstPart.addAll(secondPart);
		return firstPart;
	}
}

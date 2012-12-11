package services;

import java.util.List;

import models.PrivateConversation;
import models.PrivateMessage;
import models.User;
import models.enums.ExceptionType;
import models.exceptions.PrivateMessageException;

public class MessageService {
	public static PrivateMessage sendMessage(User author, User receiver, String message) throws PrivateMessageException {
		if (message == null || message.equals("")) {
			return null;
		}

		if (receiver == null || author == null) {
			return null;
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
		PrivateMessage privateMessage = new PrivateMessage(author, message);
		return conversation.addMessage(privateMessage) ? privateMessage : null;

	}

	public static List<PrivateConversation> getPrivateConversations(User user) {
		List<PrivateConversation> firstPart = PrivateConversation.find("byFirstUser", user).fetch();
		List<PrivateConversation> secondPart = PrivateConversation.find("bySecondUser", user).fetch();
		firstPart.addAll(secondPart);
		return firstPart;
	}
}

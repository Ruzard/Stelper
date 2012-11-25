package services;

import java.util.List;

import models.PrivateDialog;
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

		PrivateDialog dialog = PrivateDialog.find("byFirstUserAndSecondUser", author, receiver).first();
		if (dialog == null) {
			dialog = PrivateDialog.find("byFirstUserAndSecondUser", receiver, author).first();
			if (dialog == null) {
				dialog = new PrivateDialog(author, receiver);
			}
		}
		return dialog.addMessage(new PrivateMessage(author, message));
	}

	public static List<PrivateDialog> getDialogList(User user) {
		List<PrivateDialog> firstPart = PrivateDialog.find("byFirstUser", user).fetch();
		List<PrivateDialog> secondPart = PrivateDialog.find("bySecondUser", user).fetch();
		firstPart.addAll(secondPart);
		return firstPart;
	}
}

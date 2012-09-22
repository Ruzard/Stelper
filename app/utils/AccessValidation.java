package utils;

import models.User;
import models.enums.UserStatus;
import play.Logger;

public class AccessValidation {
	public static boolean postCreationAllowed(User user) {
		if (user.status != UserStatus.ACTIVE) {
			Logger.debug(user.username + " has tried to create a post being" + user.status);
			return false;
		}
		return true;
	}

	public static boolean commentCreationAllowed(User author) {
		return postCreationAllowed(author); //logic is the same atm
	}
}

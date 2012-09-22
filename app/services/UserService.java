package services;

import models.User;

public class UserService {

	public static boolean register(User user) {
		return user.validateAndSave();
	}

	public static User connect(String username, String password) {
		return User.find("byUsernameAndPassword", username, password).first();
	}
}

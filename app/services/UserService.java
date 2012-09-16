package services;

import models.User;

public class UserService {

	public static User register(User user) {
		return user.save();
	}

	public static User connect(String username, String password) {
		return User.find("byUsernameAndPassword", username, password).first();
	}
}

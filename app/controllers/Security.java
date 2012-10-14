package controllers;

import services.UserService;


public class Security extends Secure.Security {
	static boolean authenticate(String username, String password) {
		return UserService.connect(username, password) != null;
	}

	static void onAuthenticated() {
		Stelper.stelper();
	}

}

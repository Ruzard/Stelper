package controllers;

import play.mvc.Controller;

public class GuestPage extends Controller {

	public static void guestPage() {
		render("guestPage.html");
	}
}

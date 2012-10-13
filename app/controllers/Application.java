package controllers;

import models.User;
import play.mvc.Controller;

import java.util.Date;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void help() {
		render();
	}
    public static void register() {
        render();
    }
    public static void handleSubmit(
            String username,
            String firstname,
            String lastname,
            Date birthdate,
            String password,
            String email,
            String about,
            String country,
            String city) {
         User user= new User();
        user.username=username;
        user.firstName=firstname;
        user.lastName=lastname;
        user.birthdate=birthdate;
        user.password=password;
        user.email=email;
        user.about=about;
        user.city=city;


        // Ok, display the created user
        render(username, firstname, lastname, birthdate, password, email, about, country, city);
    }

}
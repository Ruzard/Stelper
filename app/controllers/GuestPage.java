package controllers;

import models.User;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.util.Date;


public class GuestPage extends Controller {


	public static void index() {
		render();
	}
}
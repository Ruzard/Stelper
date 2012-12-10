package controllers;

import play.mvc.Controller;
import play.test.Fixtures;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 10.12.12 Time: 20:18 To change
 * this template use File | Settings | File Templates.
 */
public class HELPER extends Controller {

	public static void loadModels() {
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data.yml");
	}
}

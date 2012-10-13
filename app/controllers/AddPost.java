package controllers;

import models.LangPost;
import models.UniversalPost;
import models.User;
import models.exceptions.AccessViolationException;
import models.exceptions.DataValidationException;
import play.mvc.Controller;
import services.PostService;

/**
 * Created with IntelliJ IDEA.
 * User: Mire1lle
 * Date: 13.10.12
 * Time: 19:31
 * To change this template use File | Settings | File Templates.
 */
public class AddPost extends Controller {
    public static void addPost() {
        render();
    }

    public static void handleSubmit(LangPost langPost) throws AccessViolationException, DataValidationException {
        UniversalPost unPost=new UniversalPost();
        unPost.addLangPost(langPost);
        User newUser=new User();
        PostService.createPost(unPost, newUser);
        render(langPost);
    }
}

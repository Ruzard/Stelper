package controllers;

import models.Comment;
import models.LangPost;
import models.User;
import models.enums.Language;
import play.mvc.Controller;
import services.PostService;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Mire1lle
 * Date: 13.10.12
 * Time: 23:42
 * To change this template use File | Settings | File Templates.
 */
public class AddComment extends Controller{
    public static void addComment() {
        render();
    }

    public static void handleSubmit(Comment comment){
        render(comment);
    }
}

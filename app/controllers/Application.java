package controllers;

import com.google.appengine.api.users.User;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    /**
     *
     */
    @Before
    static void checkConnected() {
        if (GAE.isLoggedIn()) {
            User user = GAE.getUser();
            Issues.storeUser(user);
        }
    }

    public static void index() {
        render();
    }

    public static void about() {
        render();
    }

    public static void login() {
        String url = "Issues.index";
        // if a url exists in the session, then forward to the redirect page
        if (session.contains("forwardURL")) {
            url = "Application.redirect";
        }
        GAE.login(url);
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

    /**
     * This method is used to complete the loop for
     * URL forwarding.  The GAE.login(url) method
     * would not take a plain url or an action with parameters.
     */
    public static void redirect() {
        String url = null;
        if (session.contains("forwardURL")) {
            url = session.get("forwardURL");
            // clear the variable from the session
            session.remove("forwardURL");
        } else {
            url = "/";
        }
        redirect(url);
    }
}

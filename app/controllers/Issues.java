/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.appengine.api.users.User;
import java.util.List;
import models.Issue;
import play.data.validation.Required;
import play.modules.gae.GAE;
import play.mvc.Before;

/**
 *
 * 
 */
public class Issues extends Application {

    /**
     * This method checks if a user is logged in.
     * It also stores a url to be used later for
     * url forwarding.
     */
    @Before
    static void checkConnected() {
        if (!GAE.isLoggedIn()) {
            // it is stored in the session because otherwise it gets lost
            // during GAE.login()
            session.put("forwardURL", request.url);
            Application.login();
        }
    }

    public static void index() {
        List<Issue> issues = Issue.allByUser(GAE.getUser().getEmail());
        render(issues);
    }

    public static void blank(Long id) {
        Issue issue = null;
        if (id != null) {
            issue = Issue.findById(id);
            notFoundIfNull(issue);
            checkOwner(issue);
        }
        render(issue);
    }

    public static void show(Long id) {
        Issue issue = Issue.findById(id);
        notFoundIfNull(issue);
        checkOwner(issue);
        render(issue);
    }

    public static void save(Long id, @Required String title, @Required String error, String solution) {
        if (id != null) {
            // this is an update
            Issue theOne = Issue.findById(id);
            theOne.title = title;
            theOne.error = error;
            theOne.solution = solution;
            theOne.update();
            show(id);
        } else {
            // this is a new creation
            Issue theOne = new Issue(title, error, solution, GAE.getUser().getEmail());
            theOne.insert();
            show(theOne.id);
        }
    }

    public static void search(String criteria) {
        List<Issue> issues = Issue.find(GAE.getUser().getEmail(), criteria );
        render(issues);
    }

    public static int count() {
        return Issue.allByUser(GAE.getUser().getEmail()).size();
    }

    public static void delete(Long id) {
        Issue issue = Issue.findById(id);
        checkOwner(issue);
        notFoundIfNull(issue);
        issue.delete();
        index();
    }

    // Utility methods below
    static void storeUser(User user) {
        renderArgs.put("user", user.getEmail());
        renderArgs.put("userName", user.getNickname());
    }

    static String getUser() {
        return (String) renderArgs.get("user");
    }

    static void checkOwner(Issue issue) {
        String currentUser = getUser();
        // if no user exists, everyone has access
        if (issue.user != null && !currentUser.equals(issue.user)) {
            forbidden();
        }
    }
}

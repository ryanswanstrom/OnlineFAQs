/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import siena.Generator;
import siena.Id;
import siena.Model;

/**
 * This class represents a problem and a solution.
 * 
 */
public class Issue extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;
    public String title;
    public String error;
    public String solution;
    public Date entry = new Date();
    public Date lastUpdated = new Date();
    public String user;
    public String status;

    public static String ACTIVE_STATUS = "A";
    public static String INACTIVE_STATUS = "I";

    public Issue(String title, String error, String solution, String user) {
        this.error = error;
        this.solution = solution;
        this.title = title;
        this.user = user;
        this.status = Issue.ACTIVE_STATUS;
    }

    public static List<Issue> allByUser(String user) {
        return Model.all(Issue.class).filter("user", user)
                .filter("status", Issue.ACTIVE_STATUS).fetch();
    }

    public static Issue findById(Long id) {
        return Model.all(Issue.class).filter("id", id)
                .filter("status", Issue.ACTIVE_STATUS).get();
    }

    /**
     * Siena does not currently support search in GAE, so this method
     * is an attempt to search.
     * 
     * @param user
     * @param search
     * @return
     */
    public static List<Issue> find(String user, String search) {
        List<Issue> issues = Model.all(Issue.class).filter("user", user)
                .filter("status", Issue.ACTIVE_STATUS).fetch();
        List<Issue> results = new ArrayList<Issue>();
        if (search != null && !search.isEmpty()) {
            // convert any non-word char to .*
            Pattern p = Pattern.compile("\\W");
            Matcher m = p.matcher(search);
            final String regex = "(?si).*" + m.replaceAll(".*") + ".*";
            // currently just searches for the exact characters
            // the Pattern.compile gives better performance
            // than the String matches()
            // the (?s) mean "." will also include line terminators
            // the (?i) is for case insensitive
            p = Pattern.compile(regex);
            for (Issue iss : issues) {
                if (p.matcher(iss.title).matches() || p.matcher(iss.error).matches() || p.matcher(iss.solution).matches()) {
                    results.add(iss);
                }
            }
        }
        return results;
    }

    @Override
    public void update() {
        this.lastUpdated = new Date();
        super.update();
    }

    /**
     * An issue is not actually deleted.  It is just flaged as inactive.
     * It will not show up anymore.
     */
    @Override
    public void delete() {
        this.status = Issue.INACTIVE_STATUS;
        this.update();
    }

    @Override
    public String toString() {
        return (title != null) ? title : "Unknown Issue";
    }
}

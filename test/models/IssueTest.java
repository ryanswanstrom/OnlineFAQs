/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.UnitTest;

/**
 *
 */
public class IssueTest extends UnitTest {

    private String anEmail = "user@home.com";
    private String[] emails = {"somebody@gmail.com", "anotherbody@gmail.com",
        anEmail};

    @Before
    public void initialize() {
        this.deleteAll();
        // add a few users
        for (String email : emails) {
            Issue newIss = new Issue("title of issue", "issue problem",
                    "the solution to the problem", email);
            newIss.insert();
        }

    }

    @After
    public void cleanUp() {
        this.deleteAll();
    }

    private void deleteAll() {
        //delete all existing entries.
        List<Issue> existing = Issue.all(Issue.class).fetch();
        for (Issue i : existing) {
            i.delete();
        }
    }

    private int countAll() {
        return Issue.all(Issue.class).fetch().size();
    }

    @Test
    public void fullTest() {
        int numBefore = this.countAll();
        Issue issue = new Issue("first test issue", "this is a bad error",
                "what a magnificent solution", "guy@home.com");
        issue.insert();
        Assert.assertEquals(numBefore + 1, this.countAll());
        issue.delete();

        // delete does not remove Issue just flags to inactive
        Assert.assertEquals(numBefore + 1, this.countAll());
        Assert.assertEquals("issue should be flaged inactive",
                Issue.INACTIVE_STATUS, issue.status);

        Issue deletedIssue = Issue.findById(issue.id);
        Assert.assertNull("issue should not be found, it is inactive",
                deletedIssue);
    }

    @Test
    public void updateTest() {
        Issue issue = new Issue("second test issue", "this is a bad error",
                "what a magnificent solution", "guy@home.com");
        issue.insert();
        final String newSolution = "a great new solution";

        int numBefore = this.countAll();
        Issue updatedIssue = Issue.findById(issue.id);
        updatedIssue.solution = newSolution;
        updatedIssue.update();
        Assert.assertEquals("no issues should have been added or removed",
                numBefore, this.countAll());
        Assert.assertEquals(updatedIssue.id, issue.id);
                
        // verify the issue was updated        
        Assert.assertEquals(newSolution, Issue.findById(updatedIssue.id).solution);

        // test the toString() method
        Assert.assertNotNull(updatedIssue.toString());
    }

    /**
     * This method also tests the find() method.
     */
    @Test
    public void allByUserTest() {
        Assert.assertEquals(0, Issue.allByUser("notReallyUser").size());
        Assert.assertEquals(1, Issue.allByUser(anEmail).size());
        for (int i = 0; i < 10; i++ ) {
            Issue iss = new Issue("title", "problem " + i, "solution", anEmail);
            iss.insert();
        }
        Assert.assertEquals(11, Issue.allByUser(anEmail).size());

        // test the find()

        Assert.assertEquals(11, Issue.find(anEmail, "problem").size());
        // search the error field
        Assert.assertEquals(1, Issue.find(anEmail, "7").size());
        // search the title field
        Assert.assertEquals(1, Issue.find(anEmail, "of").size());
        // search the solution field
        Assert.assertEquals(1, Issue.find(anEmail, "to").size());
        Assert.assertEquals(0, Issue.find(anEmail, "hot dog").size());

        Assert.assertEquals(0, Issue.find("madeUp", "problem").size());

        Assert.assertEquals(0, Issue.find(null, "problem").size());
        Assert.assertEquals(0, Issue.find(anEmail, null).size());
    }

    @Test
    public void longEntriesTest() {
        StringBuilder prob = new StringBuilder(1000000);
        StringBuilder sol = new StringBuilder(1000000);
        for (int i = 0; i < 10000; i++) {
            prob.append("This is going to be a very long string. ");
            sol.append("What a long solution. ");
        }
        Issue iss = new Issue("some title", prob.toString(), sol.toString(),
                "me@me.com");
        int numBefore = this.countAll();
        iss.insert();
        Assert.assertEquals(numBefore + 1, this.countAll());
    }

    @Test
    public void testNullDates() {
        Issue iss = new Issue("what a title", "what an error","what a solution", anEmail);
        iss.insert();
        Assert.assertNotNull(iss.lastUpdated);
        Assert.assertNotNull(iss.entry);
    }
}

import org.junit.Assert;
import org.junit.Test;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

/**
 * This class is best used for testing non-HTML
 * responses.  For more HTML tests see the Selenium tests.
 *
 * @author RMS1
 */
public class ApplicationTest extends FunctionalTest {

    @Test
    public void testIndex() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testAbout() {
        Response response = GET("/about");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testLogin() {
        Response response = GET("/login");
        // redirect to Google login page
        FunctionalTest.assertStatus(302, response);
    }

    @Test
    public void testSearchToLogin() {
        Response response = GET("/search");
        Assert.assertNotNull(response);
        FunctionalTest.assertStatus(302, response);
        FunctionalTest.assertHeaderEquals("Location", "http://localhost/login", response);
    }

    @Test
    public void testRedirectToLogin() {
        Response response = GET("/faqs");
        Assert.assertNotNull(response);
        FunctionalTest.assertStatus(302, response);
        FunctionalTest.assertHeaderEquals("Location", "http://localhost/login", response);
    }
}
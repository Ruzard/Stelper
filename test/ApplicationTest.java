import org.junit.Test;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

public class ApplicationTest extends FunctionalTest {

	@Test
	public void testThatIndexPageWorks() {
		Response response = GET("/");
		assertIsOk(response);
		assertContentType("text/html", response);
		assertCharset(play.Play.defaultWebEncoding, response);
	}

	@Test
	public void testRegistrationPageWorks() {
		Response response = GET("/register");
		assertIsOk(response);
		assertContentMatch("First name", response);
	}

	//	@Test
	public void testLoginPageWorks() {
		Response response = GET("/main");
		assertIsOk(response);


	}

}
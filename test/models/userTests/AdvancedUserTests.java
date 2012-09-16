package models.userTests;

import org.junit.*;
import play.test.*;

public class AdvancedUserTests extends UnitTest {

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");
	}

	@Test
	public void testPrepare() {
		assertTrue(true);
	}
}

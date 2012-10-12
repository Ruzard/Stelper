package models.userTests;

import models.User;
import models.enums.EducationLevel;
import org.junit.*;
import play.test.*;
import services.UserService;

public class AdvancedUserTests extends UnitTest {

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");
	}

	@Test
	public void connectNullPointerTest() {
		User connectedUser = UserService.connect("Bob", null);
		assertNull(connectedUser);

		connectedUser = UserService.connect(null, "something");
		assertNull(connectedUser);

		connectedUser = UserService.connect(null, null);
		assertNull(connectedUser);
	}

	@Test
	public void updateUserInfo() {
		User user = User.all().first();
		EducationLevel educationLevel = user.educationLevel;
		user.educationLevel = EducationLevel.UNIVERSITY;
		assertTrue(UserService.updateInfo(user));
		assertNotSame("Education level should have been changed", educationLevel, user.educationLevel);
	}
}

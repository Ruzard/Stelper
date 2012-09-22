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
	public void updateUserInfo() {
		User user = User.all().first();
		EducationLevel educationLevel = user.educationLevel;
		user.educationLevel = EducationLevel.UNIVERSITY;
		assertTrue(UserService.updateInfo(user));
		assertNotSame("Education level should have been changed", educationLevel, user.educationLevel);
	}
}

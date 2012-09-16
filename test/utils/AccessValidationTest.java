package utils;

import models.User;
import models.enums.UserStatus;
import org.junit.Test;
import play.test.UnitTest;

public class AccessValidationTest extends UnitTest {

	@Test
	public void postCreationTest() {
		User user = new User();

		user.status = UserStatus.ACTIVE;
		assertTrue("Active user should get access", AccessValidation.postCreationAllowed(user));

		user.status = UserStatus.BANNED;
		assertFalse("Banned user should not get access", AccessValidation.postCreationAllowed(user));

		user.status = UserStatus.FROZEN;
		assertFalse("Frozen user should not get access", AccessValidation.postCreationAllowed(user));
	}


}

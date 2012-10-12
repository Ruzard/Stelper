package models.userTests;

import models.User;
import models.enums.Country;
import org.junit.*;
import play.test.*;
import services.UserService;

public class BasicUserTests extends UnitTest {

	@Before
	public void preparation() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void userRegisterNotFullInfo() {
		long initialNumberOfUsers = User.count();

		User user = new User();
		user.username = "Ruzard";
		user.password = "test";
		assertFalse(user.validateAndSave());
		assertEquals("User should not have been created", initialNumberOfUsers, User.count());
	}

	@Test
	public void userRegister() {
		User user = createTestUser();
		assertTrue(UserService.register(user));
		assertEquals(1, User.count());

		user.delete();
		assertEquals(User.count(), 0);
	}

	@Test
	public void userRegisterNullPointer() {
		User user = createTestUser();
		user.username = null;

		assertFalse(UserService.register(user));
		assertFalse(UserService.register(null));

	}

	@Test
	public void multipleUserRegistered() {
		for (int i = 0; i < 5; i++) {
			User user = createTestUser();
			UserService.register(user);
		}
		assertEquals(1, User.count());
	}

	@Test
	public void connect() {
		User user = createTestUser();
		user.validateAndSave();

		User userUnderTest = UserService.connect("Username", "PassworT");
		assertNull(userUnderTest);

		userUnderTest = UserService.connect("Username", "Password");
		assertNotNull(userUnderTest);
	}

	private User createTestUser() {
		User user = new User();
		user.username = "Username";
		user.password = "Password";
		user.country = Country.RU;
		user.email = "email@email.com";
		return user;
	}
}

package models.messages;

import java.util.List;

import models.PrivateDialog;
import models.PrivateMessage;
import models.User;
import models.exceptions.PrivateMessageException;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;
import services.MessageService;

public class PrivateMessageSimpleTest extends UnitTest {

	private User first;
	private User second;

	@Before
	public void prepare() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("data.yml");

		first = User.find("byUsername", "Bob").first();
		second = User.find("byUsername", "Ted").first();
	}

	@Test
	public void sendMessage() throws PrivateMessageException {
		long initialMessageCount = PrivateMessage.count();
		long initialDialogCount = PrivateDialog.count();

		MessageService.sendMessage(first, second, "Test message");

		assertFalse("Dialog should have been created", initialDialogCount == PrivateDialog.count());
		assertFalse("Message should have been created", initialMessageCount == PrivateMessage.count());
	}

	@Test
	public void singletonDialogOneUser() throws PrivateMessageException {
		MessageService.sendMessage(first, second, "Test message");

		long initialDialogCount = PrivateDialog.count();
		MessageService.sendMessage(first, second, "Test message 2");
		assertTrue("Dialog should have been created", initialDialogCount == PrivateDialog.count());
	}

	@Test
	public void singletonDialogBothUsers() throws PrivateMessageException {
		MessageService.sendMessage(first, second, "Test message");

		long initialDialogCount = PrivateDialog.count();
		MessageService.sendMessage(second, first, "Test message 2");
		assertTrue("Dialog should not have been created", initialDialogCount == PrivateDialog.count());
	}

	@Test
	public void nullPointerMessageServiceTest() throws PrivateMessageException {
		assertFalse(MessageService.sendMessage(null, second, "Test message"));
		assertFalse(MessageService.sendMessage(first, null, "Test message"));
		assertFalse(MessageService.sendMessage(first, second, null));
		assertFalse(MessageService.sendMessage(null, null, null));
	}

	@Test
	public void multipleDialogTest() throws PrivateMessageException {
		long initialMessageCount = PrivateMessage.count();
		long initialDialogCount = PrivateDialog.count();

		User third = User.find("byUsername", "John").first();

		MessageService.sendMessage(first, second, "Test message");
		MessageService.sendMessage(first, third, "Test message");
		MessageService.sendMessage(third, second, "Test message");

		assertTrue("Dialogs should have been created", (initialDialogCount + 3) == PrivateDialog.count());
		assertTrue("Messages should have been created", (initialMessageCount + 3) == PrivateMessage.count());
	}

	@Test(expected = PrivateMessageException.class)
	public void userHasBeenDeletedInProcess() throws PrivateMessageException {
		second.delete();
		MessageService.sendMessage(first, second, "Test message");
	}

	public void fetchDialogs() throws PrivateMessageException {
		User third = User.find("byUsername", "John").first();
		long initialDialogCount = PrivateDialog.count();

		MessageService.sendMessage(first, second, "Test message");
		MessageService.sendMessage(second, first, "Test message");
		MessageService.sendMessage(first, third, "Test message");

		List<PrivateDialog> dialogList = MessageService.getDialogList(first);
		assertTrue("Dialogs should have been created", dialogList.size() == (initialDialogCount + 2));
	}

}

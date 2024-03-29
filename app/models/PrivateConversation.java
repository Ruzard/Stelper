package models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class PrivateConversation extends Model {
	@ManyToOne
	public User firstUser;

	@ManyToOne
	public User secondUser;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "conversation")
	public List<PrivateMessage> messages;

	@OneToOne(cascade = CascadeType.ALL)
	public PrivateMessage lastMessage;

	public PrivateConversation(User firstUser, User secondUser) {
		this.firstUser = firstUser;
		this.secondUser = secondUser;
		messages = new LinkedList<PrivateMessage>();
	}

	public boolean addMessage(PrivateMessage message) {
		messages.add(message);
		message.conversation = this;
		lastMessage = message;
		return validateAndSave();
	}

	public User getCompanion(String currentUser) {
		return currentUser.equals(firstUser.username) ? secondUser : firstUser;
	}
}

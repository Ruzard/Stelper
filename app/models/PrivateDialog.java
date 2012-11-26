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
public class PrivateDialog extends Model {
	@ManyToOne
	public User firstUser;

	@ManyToOne
	public User secondUser;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dialog")
	public List<PrivateMessage> messages;

	@OneToOne (cascade = CascadeType.ALL)
	public PrivateMessage lastMessage;

	public PrivateDialog(User firstUser, User secondUser) {
		this.firstUser = firstUser;
		this.secondUser = secondUser;
		messages = new LinkedList<PrivateMessage>();
	}

	public boolean addMessage(PrivateMessage message) {
		messages.add(message);
		lastMessage = message;
		return validateAndSave();
	}

	public User getCompanion(User currentUser) {
		return currentUser.equals(firstUser) ? secondUser : firstUser;
	}
}

package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class PrivateMessage extends Model {
	public String message;
	public Date sent;

	@ManyToOne
	public PrivateDialog dialog;

	@ManyToOne
	public User author;

	public PrivateMessage(User author, String message) {
		sent = new Date();
		this.author = author;
		this.message = message;
	}
}

package models;

import java.util.Date;

import javax.persistence.*;
import play.db.jpa.Model;

@Entity
public class Feedback extends Model {
	@ManyToOne
	public User receiver;

	public User author;

	public String body;
	public Date postedAt;
}

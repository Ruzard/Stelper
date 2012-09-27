package models;

import java.util.Date;

import javax.persistence.*;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Comment extends Model {
	@ManyToOne
	public User author;
	@Required
	public String body;
	@Required
	public Date postedAt;
	@ManyToOne
	public CommentTree parentTree;

	public Comment() {
		postedAt = new Date();
	}
}

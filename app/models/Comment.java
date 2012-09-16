package models;

import java.util.Date;

import javax.persistence.*;
import play.db.jpa.Model;

@Entity
public class Comment extends Model {
	public User author;
	public String body;
	public Date postedAt;

	@ManyToOne
	public LangPost parentPost;
}

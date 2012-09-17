package models;

import java.util.*;

import javax.persistence.*;
import models.enums.*;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class UniversalPost extends Model {
	public Date postedAt;
	@Required
	@Enumerated
	public PostType type;
	@Required
	@Enumerated
	public PostStatus status;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentPost")
	@Required
	public List<LangPost> posts;

	@OneToOne(cascade = CascadeType.ALL)
	public Rating rating;
	public int suspiciousLevel;

	@ManyToOne
	@Required
	public User author;

	public UniversalPost() {
		postedAt = new Date();
		status = PostStatus.OPEN;
		posts = new ArrayList<LangPost>();
		rating = new Rating();
	}

	public void addLangPost(LangPost post) {
		posts.add(post);
	}

}

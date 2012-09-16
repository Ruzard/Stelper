package models;

import java.util.*;

import javax.persistence.*;
import models.enums.*;
import play.db.jpa.Model;

@Entity
public class UniversalPost extends Model {
	public Date postedAt;
	public PostType type;
	public PostStatus status;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentPost")
	public List<LangPost> posts;

	public Rating rating;
	public int suspiciousLevel;

	@ManyToOne
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

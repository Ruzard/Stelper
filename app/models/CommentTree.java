package models;

import java.util.*;

import javax.persistence.*;
import play.db.jpa.Model;

@Entity
public class CommentTree extends Model {
	@OneToMany(mappedBy = "parentTree", cascade = CascadeType.ALL)
	@ElementCollection
	public List<Comment> comments;
	@ManyToOne
	public LangPost parentPost;

	public CommentTree(Comment mainComment) {
		this();
		comments.add(mainComment);
	}

	public CommentTree() {
		comments = new ArrayList<Comment>();
	}

	public boolean addComment(Comment comment) {
		comments.add(comment);
		return validateAndSave();
	}
}

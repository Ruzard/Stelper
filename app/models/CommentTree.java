package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class CommentTree extends Model {
	@OneToMany(mappedBy = "parentTree", cascade = CascadeType.ALL)
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

	public void addComment(Comment comment) {
		comment.parentTree = this;
		comments.add(comment);
		validateAndSave();
	}
}

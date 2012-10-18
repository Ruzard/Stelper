package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import models.enums.Language;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class LangPost extends Model {
	@Required
	public String title;
	@Required
	public String body;

	@OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL)
	public List<CommentTree> commentTrees;

	@ElementCollection
	public List<String> tags;

	@ElementCollection
	public List<Blob> fileList;

	@Required
	public Language language;

	@ManyToOne
	public UniversalPost parentPost;

	public LangPost() {
		this("Untitled", "No body", Language.EN, new ArrayList<String>());
	}

	public LangPost(String title, String body, Language language, List<String> tags) {
		this.title = title;
		this.body = body;
		this.language = language;
		commentTrees = new ArrayList<CommentTree>();
		this.tags = tags;
	}

	public boolean addCommentTree(Comment comment) {
		CommentTree commentTree = new CommentTree();
		commentTree.parentPost = this;
		commentTrees.add(commentTree);
		commentTree.addComment(comment);
		return validateAndSave();
	}
}

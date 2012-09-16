package models;

import java.util.*;

import javax.persistence.*;
import models.enums.Language;
import play.data.validation.Required;
import play.db.jpa.*;

@Entity
public class LangPost extends Model {
	@Required
	public String title;
	@Required
	public String body;

	@OneToMany(mappedBy = "parentPost")
	public List<Comment> comments;

	@ElementCollection
	public List<Blob> fileList;

	@Required
	public Language language;

	@ManyToOne
	public UniversalPost parentPost;

	public LangPost() {
		comments = new ArrayList<Comment>();
	}
}

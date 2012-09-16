package models;

import java.util.*;

import javax.persistence.*;
import play.db.jpa.*;

@Entity
public class PortfolioObject extends Model {
	public String title;
	public String body;
	public Date postedAt;

	@ElementCollection
	public List<Blob> files;

	@ManyToOne
	public User author;
}

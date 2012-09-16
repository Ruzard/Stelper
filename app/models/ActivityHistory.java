package models;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import org.hibernate.annotations.*;
import play.db.jpa.Model;

@Entity
public class ActivityHistory extends Model {

	@NotFound(action = NotFoundAction.IGNORE)
	@ElementCollection
	@CollectionTable(name = "activityhistory_ratedPosts")
	public List<UniversalPost> ratedPosts;

	@ElementCollection
	@CollectionTable(name = "activityhistory_reportedPosts")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<UniversalPost> reportedPosts;

	@ElementCollection(fetch = FetchType.LAZY)
	public List<User> reportedUsers;

	public Date lastUserReport;

	public Date lastPostReport;
}

package models;

import org.hibernate.annotations.*;
import play.db.jpa.Model;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.Date;
import java.util.List;

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

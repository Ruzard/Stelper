package models;

import java.util.*;

import javax.persistence.*;
import play.db.jpa.Model;

@Entity
public class ActivityHistory extends Model {

	@ElementCollection(fetch = FetchType.LAZY)
	public List<UniversalPost> ratedPosts;

//	@ElementCollection(fetch = FetchType.LAZY)
//	public List<UniversalPost> reportedPosts;

	@ElementCollection(fetch = FetchType.LAZY)
	public List<User> reportedUsers;

	public Date lastUserReport;

	public Date lastPostReport;
}

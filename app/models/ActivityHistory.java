package models;

import java.util.*;

import javax.persistence.*;
import play.db.jpa.Model;

@Entity
public class ActivityHistory extends Model {

	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name = "ratedPosts")
	public List<UniversalPost> ratedPosts;

//	@ElementCollection(fetch = FetchType.LAZY)
//	@Column(name = "reportedPosts")
//	public List<UniversalPost> reportedPosts;

	@ElementCollection(fetch = FetchType.LAZY)
	public List<User> reportedUsers;

	public Date lastUserReport;

	public Date lastPostReport;

	public ActivityHistory() {
//		ratedPosts = new ArrayList<UniversalPost>();
//		reportedPosts = new ArrayList<UniversalPost>();
//		reportedUsers = new ArrayList<User>();

	}
}

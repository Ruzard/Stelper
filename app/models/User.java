package models;

import java.util.*;

import javax.persistence.*;
import models.enums.*;
import play.data.validation.*;
import play.db.jpa.Model;

@Entity
@Table(name = "StUser")
public class User extends Model {

	@Required
	@Unique
	public String username;

	public String firstName;

	public String lastName;

	public Date birthdate;

	@Required
	public String password;

	@Email
	@Required
	public String email;

	public String about;

	@Required
	public Country country;

	public String city;

	@ElementCollection
	public List<Language> languages;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	public List<UniversalPost> posts;

	@OneToOne(cascade = CascadeType.ALL)
	public ActivityHistory history;

	public Role role;

	public UserStatus status;

	public EducationLevel educationLevel;

	public Rating userRating;

	public Rating postRating;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	public List<PortfolioObject> portfolio;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
	public List<Feedback> feedbacks;

	public ReportState reportState;

	public User() {
		history = new ActivityHistory();

		postRating = new Rating();
		userRating = new Rating();

		portfolio = new ArrayList<PortfolioObject>();
		feedbacks = new ArrayList<Feedback>();

		reportState = new ReportState();

		status = UserStatus.ACTIVE;
		role = Role.USER;
	}
}

package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import models.enums.Country;
import models.enums.EducationLevel;
import models.enums.Language;
import models.enums.Role;
import models.enums.UserStatus;
import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
@Table(name = "StUser")
public class User extends Model {

	@Required
	@Unique
	@Column(unique = true)
	public String username;

	public String firstName;

	public String lastName;
	@As("dd.MM.yyyy")
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
	@Enumerated
	public List<Language> languages;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	public List<UniversalPost> posts;

	@OneToOne(cascade = CascadeType.ALL)
	public ActivityHistory history;

	@Enumerated(EnumType.STRING)
	public Role role;

	@Enumerated(EnumType.STRING)
	public UserStatus status;

	@Enumerated(value = EnumType.STRING)
	public EducationLevel educationLevel;

	@OneToOne(cascade = CascadeType.ALL)
	public Rating postRating;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	public List<PortfolioObject> portfolio;

	@OneToOne(cascade = CascadeType.ALL)
	public ReportState reportState;

	public User() {
		history = new ActivityHistory();

		postRating = new Rating();

		portfolio = new ArrayList<PortfolioObject>();

		reportState = new ReportState();

		status = UserStatus.ACTIVE;
		role = Role.USER;
	}
}

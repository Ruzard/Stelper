package models;

import java.util.Date;

import javax.persistence.Entity;
import models.enums.DealStatus;
import play.db.jpa.Model;

@Entity
public class DealAgent extends Model {
	public User searcher;
	public User helper;
	public UniversalPost relatedPost;

	public Date closedAt;
	public DealStatus status;
	public Feedback feedback;
	public Boolean feedbackLeft;
}

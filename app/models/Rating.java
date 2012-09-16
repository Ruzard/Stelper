package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Rating extends Model {
	public int positive;
	public int neutral;
	public int negative;
}

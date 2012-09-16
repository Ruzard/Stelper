package models;

import java.util.Date;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class ReportState extends Model {
	public int suspiciousLevel;
	public Date lastReported;
}

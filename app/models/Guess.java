package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.avaje.ebean.annotation.ConcurrencyMode;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

/**
 * A Guess represents one round of guessing for a player.
 * 
 * {@link ConcurrencyMode#NONE} disables optimistic locking.
 * 
 * @author Christopher Olaussen
 *
 */
@Entity
@Table(name = "tbl_guess")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Guess extends Model {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;

	@Id
	@Constraints.Min(10)
	private Long id;
	
	@Constraints.Required
	@Column(name="points")
	private Integer points;
	
	@Constraints.Required
	@Column(name="bonusPoints")
	private Integer bonusPoints;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Game game;
	
	@Formats.DateTime(pattern = DATE_PATTERN)
	@Column(name="created")
	private Date created = new Date();
	
	public static Finder<Long, Guess> find = new Finder<Long, Guess>(Long.class,
			Guess.class);
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(Integer bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
	
	public int getSumPoints() {
		return bonusPoints+points;
	}
	
	public static Guess findEarlierGuess(Guess thanGuess, Game belongingTo) {
		List<Guess> findList = find.where().lt("created", thanGuess.getCreated()).eq("game", belongingTo).orderBy("created desc").setMaxRows(1).findList();
		if(findList!=null && findList.size() > 0) {
			return findList.get(0);
		}
		return null;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
}

package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.avaje.ebean.annotation.ConcurrencyMode;


import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import utils.Toolbox;

/**
 * Game representing a game with its rounds.
 * 
 * {@link ConcurrencyMode#NONE} disables optimistic locking.
 * 
 * @author Christopher Olaussen
 *
 */
@Entity
@Table(name = "tbl_game")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Game extends Model {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;

	@Id
	@Constraints.Min(10)
	private Long id;

	
	@Constraints.Required
	@Column(name="points")
	private Integer points;
	
	@Constraints.Required
	@Column(name="wins_in_a_row")
	private Integer winsInARow;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private GamePlayer gamePlayer;
	
	@OneToMany(mappedBy="game")
	@OrderBy("created desc")  
	private List<Guess> guesses;
	
	@Formats.DateTime(pattern = DATE_PATTERN)
	@Column(name="created")
	private Date created = new Date();
	
	public static Finder<Long, Game> find = new Finder<Long, Game>(Long.class,
			Game.class);
	
		
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

	public List<Guess> getGuesses() {
		return guesses;
	}

	public void setGuesses(List<Guess> guesses) {
		this.guesses = guesses;
	}

	public Integer getWinsInARow() {
		return winsInARow;
	}

	public void setWinsInARow(Integer winsInARow) {
		this.winsInARow = winsInARow;
	}

	public Integer increaseWinsInArow() {
		if(winsInARow != null && winsInARow >= 0) {
			winsInARow = winsInARow+1;
		}
		if(winsInARow == null) {
			winsInARow = 0;
		}
		return Toolbox.bonusPoints(winsInARow);
	}
	
	public static Integer rank(Integer score) {		
		Integer ranking = com.avaje.ebean.Ebean.createSqlQuery("SELECT count(id) as counted FROM tbl_game where points > :pts order by points desc;").setParameter("pts", score).findUnique().getInteger("counted");		
		return ranking+1;		
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}
	
}

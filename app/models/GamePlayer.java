package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.avaje.ebean.annotation.ConcurrencyMode;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import utils.Toolbox;

@Entity
@Table(name = "tbl_gameplayer")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class GamePlayer extends Model {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;

	@Id
	@Constraints.Min(10)
	private Long id;

	@Constraints.Required
	@Column(name="name")
	private String name;
	
	@Constraints.Required
	@Column(name="points")
	private Integer points;
	
	@Constraints.Required
	@Column(name="wins_in_a_row")
	private Integer winsInARow;
	
	@Constraints.Required
	@Column(name="uuid")
	private String uuid;
	
	@OneToMany(mappedBy="gameplayer")
	@OrderBy("created desc")  
	private List<Guess> guesses;
	
	@Formats.DateTime(pattern = DATE_PATTERN)
	@Column(name="created")
	private Date created = new Date();
	
	public static Finder<Long, GamePlayer> find = new Finder<Long, GamePlayer>(Long.class,
			GamePlayer.class);
	
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	
}

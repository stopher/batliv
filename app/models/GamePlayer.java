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
 * GamePlayer representing a Player and his aggregated results.
 * 
 * {@link ConcurrencyMode#NONE} disables optimistic locking.
 * 
 * @author Christopher Olaussen
 *
 */
@Entity
@Table(name = "tbl_gameplayer", uniqueConstraints={@UniqueConstraint(columnNames={"uuid"})})
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
	@Column(name="uuid")
	private String uuid;
	
	@Formats.DateTime(pattern = DATE_PATTERN)
	@Column(name="created")
	private Date created = new Date();
	
	@OneToMany(mappedBy="gamePlayer")
	private List <Game> games;
	
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List <Game> getGames() {
		return games;
	}

	public void setGames(List <Game> games) {
		this.games = games;
	}
	
}

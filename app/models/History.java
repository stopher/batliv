package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;


@Entity
@Table(name = "tbl_history")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class History {
public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;
	
	public static Finder<Long, History> find = new Finder<Long, History>(Long.class,
			History.class);
	
	@Id
	@Constraints.Min(10)
	private Long id;
	
	@Constraints.Required
	@Column(name="latitude")
	private Double latitude;
	
	@Constraints.Required
	@Column(name="longitude")
	private Double longitude;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Boat boat;
	
	@Formats.DateTime(pattern = DATE_PATTERN)
	private Date created = new Date();
	
	
	
	
	/**
	 * Find history by boat
	 * @param boat
	 * @return
	 */
	public static List<History> findByBoat(Boat boat, int maxRowsReturned) {

		List<History> history = find.fetch("boat").where()
				.eq("boat", boat)
				.orderBy("created desc")
				.setMaxRows(maxRowsReturned).findList();
		return history;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}

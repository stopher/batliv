package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.avaje.ebean.annotation.ConcurrencyMode;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
@Table(name = "tbl_boat")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Boat extends Model {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;

	@Id
	@Constraints.Min(10)
	private Long id;

	@Constraints.Required
	private String name;
	
	@Constraints.Required
	@Column(name="description")	
	private String description;
	
	@Constraints.Required
	@Column(name="latitude")
	private Double latitude;
	
	@Constraints.Required
	@Column(name="longitude")
	private Double longitude;
	
	@Column(name="type")
	private String type;

	@Column(name="telephone")
	private String telephone;

	@Formats.DateTime(pattern = DATE_PATTERN)
	private Date created = new Date();
	
	@OneToMany(mappedBy="boat")
	private List<History> history;

	public static Finder<Long, Boat> find = new Finder<Long, Boat>(Long.class,
			Boat.class);
	
				
	/**
	 * 
	 * Eg. use with result from Google Maps.getBounds() to get positions in
	 * bounds of current map.
	 * 
	 * |(nwLat,nwLng) | | | | (seLat,seLng)|
	 * 
	 * @return list of positions
	 */
	public static List<Boat> findPositionsInBounds(Double northwestLat,
			Double northwestLng, Double southeastLat, Double southeastLng,
			int maxRowsReturned) {

		List<Boat> positions = find.where()
				.gt("latitude", southeastLat).lt("latitude", northwestLat)
				.lt("longitude", southeastLng).gt("longitude", northwestLng)
				.setMaxRows(maxRowsReturned).findList();

		return positions;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	

	public void setDescription(String description) {
		this.description = description;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String toString() {
		
		return description;
		
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}

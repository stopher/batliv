package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

@Entity
@Table(name = "tbl_chatmessage")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ChatMessage {
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;

	public static Finder<Long, ChatMessage> find = new Finder<Long, ChatMessage>(Long.class,
		ChatMessage.class);

	@Id
	@Constraints.Min(10)
	private Long id;
	
	@Constraints.Required
	@Column(name="user")
	private String user;
	
	@Constraints.Required
	@Column(name="message")	
	private String message;
	
	@Constraints.Required
	@Formats.DateTime(pattern = DATE_PATTERN)
	@Column(name="created")	
	private Date created = new Date();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	
}

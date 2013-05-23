package database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Log {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@Temporal (TemporalType.TIMESTAMP)
	private Date timestamp;

	@Column(columnDefinition = "MEDIUMTEXT")
	private String message;

	private String name;

	private Type type;
	
	@ManyToOne
	private WebService webservice;

	public enum Type {
		OPERATION, HARDWARE, SOFTWARE, AVAILABILITY
	}
	
	public Log() { }

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public WebService getWebservice() {
		return webservice;
	}

	public void setWebservice(WebService webservice) {
		this.webservice = webservice;
	}

}

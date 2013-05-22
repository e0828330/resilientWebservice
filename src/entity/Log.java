package entity;

import java.sql.Timestamp;

public class Log {

	private Long id;

	private Timestamp timestamp;

	private String message;

	private String name;

	private Type type;

	public enum Type {
		OPERATION, HARDWARE, SOFTWARE, AVAILABILITY
	}
	
	public Log() {
		
	}
	
	public Log (Long id, String message, String name, Type type) {
		this.id = id;
		this.message = message;
		this.name = name;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
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

}

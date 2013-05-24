package resilient.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Change {

	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlElement(name="date")
	private Date date;
	
	@XmlElement(name="name")
	private String name; 
	
	@XmlElement(name="type")
	private String type;
	
	@XmlElement(name="message")
	private String logMessage;

	public Change() {
		
	}
	
	public Change(Date date, String name, String type, String logMessage) {
		this.date = date;
		this.logMessage = logMessage;
		this.name = name;
		this.type = type;
	}
	
	public Date getDate() {
		return date;
	}

	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	} 
}

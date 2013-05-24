package resilient.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="changes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Identity {

	@XmlElement
	private String version;
	
	@XmlElement
	private String wsdlUrl;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlElement(name="date")
	private Date created;

	public Identity() {
		
	}
	
	public Identity(String version, String wsdlUrl, Date created) {
		this.version = version;
		this.wsdlUrl = wsdlUrl;
		this.created = created;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	
}

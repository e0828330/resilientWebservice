package resilient;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import resilient.xml.Changes;
import resilient.xml.DateAdapter;
import resilient.xml.Identity;

@WebService(targetNamespace=Resilient.NAMESPACE)
public interface Resilient {
	
	static final String NAMESPACE = "http://rws.tuwien.ac.at/DigitalPreservation";
	
	@WebMethod
	public Identity identifyYourSelf();
	@WebMethod
	public String identifySWEnvironment();
	@WebMethod
	public String identifyHWEnvironment();
	@WebMethod
	public Changes serviceChangesSince(@XmlElement(required=true) @WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date);
	@WebMethod
	public Changes swEnvironmentChangesSince(@XmlElement(required=true) @WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date);
	@WebMethod
	public Changes hwEnvironmentChangesSince(@XmlElement(required=true) @WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date);
	
}

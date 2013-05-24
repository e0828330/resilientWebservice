package resilient;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
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
	public Changes serviceChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date);
	@WebMethod
	public String swEnvironmentChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date);
	@WebMethod
	public String hwEnvironmentChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date);
	
}

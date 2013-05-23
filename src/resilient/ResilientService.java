package resilient;

import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import resilient.xml.Change;
import resilient.xml.Changes;
import resilient.xml.DateAdapter;

@WebService(endpointInterface="resilient.Resilient", targetNamespace=Resilient.NAMESPACE)
public class ResilientService implements Resilient {

	@Resource
	private WebServiceContext ctx;
	
	@Override
	@WebMethod
	public String identifyYourSelf() {
		return "Hello I am a Service:" + ctx.getMessageContext().get(MessageContext.QUERY_STRING);
	}

	@Override
	@WebMethod
	public String identifySWEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public Changes serviceChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		if (date != null) {
			System.out.println(date.toString());
		}
		Changes changes = new Changes();
		changes.addData(new Change(new Date(), "Test"));
		changes.addData(new Change(new Date(), "Test 2"));

		return changes;
	}

	@Override
	@WebMethod
	public String swEnvironmentChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public String hwEnvironmentChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		// TODO Auto-generated method stub
		return null;
	}

}

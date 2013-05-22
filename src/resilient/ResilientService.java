package resilient;

import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import resilient.xml.Change;
import resilient.xml.Changes;

@WebService(endpointInterface="resilient.Resilient")
public class ResilientService implements Resilient {

	@Resource
	private WebServiceContext ctx;
	
	@Override
	public String identifyYourSelf() {
		return "Hello I am a Service:" + ctx.getMessageContext().get(MessageContext.QUERY_STRING);
	}

	@Override
	public String identifySWEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Changes serviceChangesSince(Date date) {
		if (date != null) {
			System.out.println(date.toString());
		}
		Changes changes = new Changes();
		changes.addData(new Change(new Date(), "Test"));
		changes.addData(new Change(new Date(), "Test 2"));

		return changes;
	}

	@Override
	public String swEnvironmentChangesSince(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hwEnvironmentChangesSince(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

}

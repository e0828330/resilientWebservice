package resilient;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import database.dao.ResourceFactory;
import database.entity.Log;

import resilient.xml.Change;
import resilient.xml.Changes;
import resilient.xml.DateAdapter;
import resilient.xml.Identity;

@WebService(endpointInterface="resilient.Resilient", targetNamespace=Resilient.NAMESPACE)
public class ResilientService implements Resilient {

	@Resource
	private WebServiceContext ctx;
	
	private Long getId() {
		Long id = Long.parseLong((String) ctx.getMessageContext().get(MessageContext.QUERY_STRING));
		return id;
	}
	
	@Override
	@WebMethod
	public Identity identifyYourSelf() {
		database.entity.WebService service = ResourceFactory.getServiceDao().getService(getId());
		Identity identity = new Identity(service.getVersion(), service.getUrl(), service.getTimestamp()); 
		return identity;
	}

	@Override
	@WebMethod
	public String identifySWEnvironment() {
		database.entity.WebService service = ResourceFactory.getServiceDao().getService(getId());
		return service.getSWinfo();
	}

	@Override
	@WebMethod
	public String identifyHWEnvironment() {
		database.entity.WebService service = ResourceFactory.getServiceDao().getService(getId());
		return service.getHWinfo();
	}
	
	@Override
	@WebMethod
	public Changes serviceChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		Long id = getId();
		Changes changes = new Changes();
		List<Log> logs = ResourceFactory.getLogDao().getSinceDate(id, date);
		for (Log log : logs) {
			changes.addData(new Change(log.getTimestamp(), log.getName(), log.getType().toString(), log.getMessage()));
		}
		
		return changes;
	}

	@Override
	@WebMethod
	public Changes swEnvironmentChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		Long id = getId();
		Changes changes = new Changes();
		List<Log> logs = ResourceFactory.getLogDao().getSinceDate(id, date);
		for (Log log : logs) {
			if (log.getType().equals(Log.Type.SOFTWARE)) {
				changes.addData(new Change(log.getTimestamp(), log.getName(), log.getType().toString(), log.getMessage()));
			}
		}

		return changes;
	}

	@Override
	@WebMethod
	public Changes hwEnvironmentChangesSince(@WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		Long id = getId();
		Changes changes = new Changes();
		List<Log> logs = ResourceFactory.getLogDao().getSinceDate(id, date);
		for (Log log : logs) {
			if (log.getType().equals(Log.Type.HARDWARE)) {
				changes.addData(new Change(log.getTimestamp(), log.getName(), log.getType().toString(), log.getMessage()));
			}
		}

		return changes;
	}

}

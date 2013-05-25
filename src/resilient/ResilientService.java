package resilient;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import database.DBConnector;
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
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();				
		database.entity.WebService service = ResourceFactory.getServiceDao(em).getService(getId());
		tx.commit();
		em.close();
		Identity identity = new Identity(service.getVersion(), service.getUrl(), service.getTimestamp()); 
		return identity;
	}

	@Override
	@WebMethod
	public String identifySWEnvironment() {
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();				
		database.entity.WebService service = ResourceFactory.getServiceDao(em).getService(getId());
		tx.commit();
		em.close();
		return service.getSWinfo();
	}

	@Override
	@WebMethod
	public String identifyHWEnvironment() {
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();				
		database.entity.WebService service = ResourceFactory.getServiceDao(em).getService(getId());
		tx.commit();
		em.close();
		return service.getHWinfo();
	}
	
	@Override
	@WebMethod
	public Changes serviceChangesSince(@XmlElement(required=true) @WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		Long id = getId();
		Changes changes = new Changes();
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();					
		List<Log> logs = ResourceFactory.getLogDao(em).getSinceDate(id, date);
		tx.commit();
		em.close();
		for (Log log : logs) {
			changes.addData(new Change(log.getTimestamp(), log.getName(), log.getType().toString(), log.getMessage()));
		}
		
		return changes;
	}

	@Override
	@WebMethod
	public Changes swEnvironmentChangesSince(@XmlElement(required=true) @WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		Long id = getId();
		Changes changes = new Changes();
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();					
		List<Log> logs = ResourceFactory.getLogDao(em).getSinceDate(id, date);
		tx.commit();
		em.close();
		for (Log log : logs) {
			if (log.getType().equals(Log.Type.SOFTWARE)) {
				changes.addData(new Change(log.getTimestamp(), log.getName(), log.getType().toString(), log.getMessage()));
			}
		}

		return changes;
	}

	@Override
	@WebMethod
	public Changes hwEnvironmentChangesSince(@XmlElement(required=true) @WebParam(name="date") @XmlJavaTypeAdapter(DateAdapter.class) Date date) {
		Long id = getId();
		Changes changes = new Changes();
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();					
		List<Log> logs = ResourceFactory.getLogDao(em).getSinceDate(id, date);
		tx.commit();
		em.close();
		for (Log log : logs) {
			if (log.getType().equals(Log.Type.HARDWARE)) {
				changes.addData(new Change(log.getTimestamp(), log.getName(), log.getType().toString(), log.getMessage()));
			}
		}

		return changes;
	}

}

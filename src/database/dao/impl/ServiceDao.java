package database.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import database.DBConnector;
import database.dao.IServiceDao;
import database.entity.WebService;

public class ServiceDao implements IServiceDao {

	private EntityManagerFactory emf;

	private Logger log = Logger.getLogger(this.getClass());
	
	public ServiceDao() {
		this.emf = DBConnector.getInstance().getEMF();
	}
	
	@Override
	public void addService(WebService service) {
		this.log.debug("Try to store service " + service.getUrl() + " to database.");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(service);
		tx.commit();
		em.close();
		this.log.debug("Stored service " + service.getUrl() + " and ID=" + service.getId() + " to database.");
	}

	@Override
	public void updateService(WebService service) {
		this.log.debug("Try to update " + service.getUrl() + ".");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.merge(service);
		tx.commit();
		em.close();	
		this.log.debug("Updated service " + service.getUrl() + ".");
	}

	@Override
	public void deleteService(WebService service) {
		this.log.debug("Try to delete service " + service.getUrl() + ".");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.remove(service);
		tx.commit();
		em.close();
		this.log.debug("Deleted service " + service.getUrl() + ".");
	}

	@Override
	public WebService getService(Long id) {
		this.log.debug("Try to get service with ID=" + id + "from database.");
		EntityManager em = emf.createEntityManager();
		em = this.emf.createEntityManager();
		WebService ws = em.find(WebService.class, id);
		em.close();
		if (ws != null) {
			this.log.debug("Return service " + ws.getUrl() + ".");
		}
		return ws;
	}

	@Override
	public WebService getByURL(String url) {
		this.log.debug("Try to get service with URL=" + url + "from database.");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT w FROM WebService w WHERE url = :url ", WebService.class);
		query.setParameter("url", url);
		List<?> result = query.getResultList();
		em.close();
		if (result == null || result.isEmpty()) {
			return null;
		}
		this.log.debug("Found service with url " + url);
		return (WebService) result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WebService> getAll() {
		EntityManager em = emf.createEntityManager();
		List<WebService> result = em.createQuery("SELECT ws FROM WebService ws").getResultList();
		em.close();
		return result;
	}

}

package database.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import database.dao.ILogDao;
import database.entity.Data;
import database.entity.Log;
import database.entity.Log.Type;

public class LogDao implements ILogDao {

	private Logger log = Logger.getLogger(this.getClass());
	
	private EntityManager em;
	
	public LogDao(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void addLog(Log log) {
		this.log.debug("Try to store log to database.");
		em.persist(log);
		this.log.debug("Stored log for service " +
				log.getWebservice().getUrl() + " to database.");
	}

	@Override
	public Log getLastEntryOfType(Long serviceId, String name, Type type, Data data) {
		this.log.debug("Try to get entry of type=" + type + " name=" + name);
		Query query = null;
		if (name == null && data == null) {
			query = em.createQuery("SELECT l FROM Log l" +
					" WHERE l.name IS NULL AND l.type = :type AND l.webservice.id = :serviceId AND data IS NULL" +
					" ORDER by l.timestamp DESC", Log.class);
			query.setParameter("type", type);
			query.setParameter("serviceId", serviceId);
		} 
		else if (name == null && data != null) {
			query = em.createQuery("SELECT l FROM Log l" +
					" WHERE l.name IS NULL AND l.type = :type AND l.webservice.id = :serviceId AND data = :data" +
					" ORDER by l.timestamp DESC", Log.class);
			query.setParameter("type", type);
			query.setParameter("serviceId", serviceId);
			query.setParameter("data", data);
		}
		else if (name != null && data == null) {
			query = em.createQuery("SELECT l FROM Log l" +
					" WHERE l.name = :name AND l.type = :type AND l.webservice.id = :serviceId AND data IS NULL" +
					" ORDER by l.timestamp DESC", Log.class);
			query.setParameter("type", type);
			query.setParameter("name", name);
			query.setParameter("serviceId", serviceId);
		}
		else {
			query = em.createQuery("SELECT l FROM Log l" +
					" WHERE l.name = :name AND l.type = :type AND l.webservice.id = :serviceId AND data = :data" +
					" ORDER by l.timestamp DESC", Log.class);
			query.setParameter("type", type);
			query.setParameter("name", name);
			query.setParameter("serviceId", serviceId);
			query.setParameter("data", data);
		}
		
		List <?> result = query.getResultList();
		if (result == null || result.isEmpty()) {
			return null;
		}
		this.log.debug("Found entry of type=" + type + " name=" + name);
		return (Log) result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Log> getSinceDate(Long serviceId, Date since) {
		this.log.debug("Try to get entries of for " + serviceId + " since " + since);
		Query query = em.createQuery("SELECT l FROM Log l" +
									" WHERE l.webservice.id = :serviceId AND l.timestamp >= :date" +
									" ORDER by l.timestamp DESC", Log.class);
		query.setParameter("date", since);
		query.setParameter("serviceId", serviceId);
		List <Log> result = query.getResultList();
		return result;
	}

}

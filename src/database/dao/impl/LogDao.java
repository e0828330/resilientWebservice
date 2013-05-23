package database.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import database.DBConnector;
import database.dao.ILogDao;
import database.entity.Log;

public class LogDao implements ILogDao {
	
	private EntityManagerFactory emf;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public LogDao() {
		this.emf = DBConnector.getInstance().getEMF();
	}
	
	@Override
	public void addLog(Log log) {
		this.log.debug("Try to store log to database.");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(log);
		tx.commit();
		em.close();
		this.log.debug("Stored log for service " +
				log.getWebservice().getUrl() + " to database.");
	}

}

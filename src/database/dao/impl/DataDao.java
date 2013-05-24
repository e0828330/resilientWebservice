package database.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import monitor.Monitor;
import monitor.MonitorManager;

import org.apache.log4j.Logger;

import database.DBConnector;
import database.dao.IDataDao;
import database.entity.Data;
import database.entity.WebService;

public class DataDao implements IDataDao {

	private EntityManagerFactory emf;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public DataDao() {
		this.emf = DBConnector.getInstance().getEMF();
	}
	
	@Override
	public void addData(Data data) {
		this.log.debug("Try to store data for service.");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(data);
		tx.commit();
		em.close();
		this.log.debug("Stored data.");
	}

	@Override
	public List<Data> getData(Long id) {
		this.log.debug("Selecting all data for webservice with ID=" + id + ".");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		WebService ws = em.find(WebService.class, id);
		this.log.debug("select data now for webservice " + ws.getUrl());
		List<Data> result = ws.getData();
		this.log.debug("Received " + result.size() + " rows.");
		tx.commit();
		em.close();
		return result;
	}

	@Override
	public void deleteByWebService(WebService ws) {
		//this.log.debug("Try to remove all data for service with ID=" + ws.getId() + ".");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		WebService tmp = em.find(WebService.class, ws.getId());
		for (Data d : tmp.getData()) {
			em.merge(d);
			em.remove(d);
		}
		tx.commit();
		em.close();
		this.log.debug("Removed data.");
	}

}

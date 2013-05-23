package database.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import database.dao.IDataDao;
import entity.Data;
import entity.WebService;

public class DataDao implements IDataDao {

	private EntityManagerFactory emf;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public DataDao(EntityManagerFactory emf) {
		this.emf = emf;
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

	@SuppressWarnings("unchecked")
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
	protected void finalize() throws Throwable {
		super.finalize();
		emf.close();
	}	

}

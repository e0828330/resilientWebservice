package database.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import database.dao.IDataDao;
import database.entity.Data;
import database.entity.WebService;

public class DataDao implements IDataDao {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private EntityManager em;
	
	public DataDao(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void addData(Data data) {
		this.log.debug("Try to store data for service.");
		em.persist(data);
		this.log.debug("Stored data.");
	}

	@Override
	public List<Data> getData(Long id) {
		this.log.debug("Selecting all data for webservice with ID=" + id + ".");
		WebService ws = em.find(WebService.class, id);
		this.log.debug("select data now for webservice " + ws.getUrl());
		List<Data> result = ws.getData();
		this.log.debug("Received " + result.size() + " rows.");
		return result;
	}

	@Override
	public void deleteByWebService(WebService ws) {
		//this.log.debug("Try to remove all data for service with ID=" + ws.getId() + ".");
		WebService tmp = em.find(WebService.class, ws.getId());
		for (Data d : tmp.getData()) {
			d.setWebservice(null);
			em.remove(d);
		}
		this.log.debug("Removed data.");
	}

}

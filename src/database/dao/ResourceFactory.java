package database.dao;

import javax.persistence.EntityManager;

import database.dao.impl.DataDao;
import database.dao.impl.LogDao;
import database.dao.impl.ServiceDao;

public class ResourceFactory {
	
	private ResourceFactory() {	};
	
	public static IDataDao getDataDao(EntityManager em) {
		return new DataDao(em);
	}
	
	public static IServiceDao getServiceDao(EntityManager em) {
		return new ServiceDao(em);
	}
	
	public static ILogDao getLogDao(EntityManager em) {
		return new LogDao(em);
	}

	
}

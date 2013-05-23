package database.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import database.dao.impl.DataDao;
import database.dao.impl.LogDao;
import database.dao.impl.ServiceDao;

public class ResourceFactory {
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("serviceDB");
	
	private ResourceFactory() {	};
	
	public static IDataDao getDataDao() {
		return new DataDao(emf);
	}
	
	public static IServiceDao getServiceDao() {
		return new ServiceDao(emf);
	}
	
	public static ILogDao getLogDao() {
		return new LogDao(emf);
	}

	
}

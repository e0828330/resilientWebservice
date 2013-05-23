package database.dao;

import database.dao.impl.DataDao;
import database.dao.impl.LogDao;
import database.dao.impl.ServiceDao;

public class ResourceFactory {
	
	private ResourceFactory() {	};
	
	public static IDataDao getDataDao() {
		return new DataDao();
	}
	
	public static IServiceDao getServiceDao() {
		return new ServiceDao();
	}
	
	public static ILogDao getLogDao() {
		return new LogDao();
	}

	
}

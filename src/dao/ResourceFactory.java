package dao;

import dao.impl.LogDao;
import dao.impl.ResultDao;
import dao.impl.ServiceDao;
import database.DBConnector;

public class ResourceFactory {

	
	private ResourceFactory() { }
	
	public static IResultDao getResultDao() {
		return new ResultDao(DBConnector.getInstance().getConnect());
	}
	
	public static IServiceDao getServiceDao() {
		return new ServiceDao(DBConnector.getInstance().getConnect());
	}
	
	public static ILogDao getLogDao() {
		return new LogDao(DBConnector.getInstance().getConnect());
	}

	
}

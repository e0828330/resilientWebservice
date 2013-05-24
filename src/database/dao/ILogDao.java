package database.dao;

import database.entity.Log;


public interface ILogDao {
	
	public void addLog(Log log);
	
	/**
	 * Returns the most recent entry that
	 * has the given type and name
	 * 
	 * @param serviceId
	 * @param name
	 * @param type
	 * @return
	 */
	public Log getLastEntryOfType(Long serviceId, String name, Log.Type type);
	
}

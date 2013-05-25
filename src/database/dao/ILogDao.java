package database.dao;

import java.util.Date;
import java.util.List;

import database.entity.Log;


public interface ILogDao {
	
	public void addLog(Log log);
	
	/**
	 * Returns the most recent entry that
	 * has the given type and name and dataId
	 * 
	 * @param serviceId
	 * @param name
	 * @param type
	 * @param dataId
	 * @return
	 */
	public Log getLastEntryOfType(Long serviceId, String name, Log.Type type, Long dataId);
	
	/**
	 * Returns all message since the given date,
	 * when date is null all messages are returned
	 * 
	 * @param serviceId
	 * @param since
	 * @return
	 */
	public List<Log> getSinceDate(Long serviceId, Date since);
	
}

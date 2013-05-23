package database.dao;

import entity.WebService;

public interface IServiceDao {

	public void addService(WebService service);
	
	public void updateService(WebService service);
	
	public void deleteService(WebService service);
	
	public WebService getService(Long id);
	
}

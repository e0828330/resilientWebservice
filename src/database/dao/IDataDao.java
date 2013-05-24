package database.dao;

import java.util.List;

import database.entity.Data;
import database.entity.WebService;


public interface IDataDao {

	public void addData(Data data);
	
	public List<Data> getData(Long id);
	
	public void deleteByWebService(WebService service);
	
}

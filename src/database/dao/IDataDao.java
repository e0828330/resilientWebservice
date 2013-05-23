package database.dao;

import java.util.List;

import database.entity.Data;


public interface IDataDao {

	public void addData(Data data);
	
	public List<Data> getData(Long id);
	
}

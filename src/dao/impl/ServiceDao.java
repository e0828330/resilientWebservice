package dao.impl;

import java.sql.Connection;

import dao.IServiceDao;

public class ServiceDao implements IServiceDao {

	public ServiceDao(Connection connection) {
		
	}
	
	@Override
	public void addService() {
		System.out.println("ADDING SERVICE");
	}

	@Override
	public void updateService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteService() {
		// TODO Auto-generated method stub
		
	}

}

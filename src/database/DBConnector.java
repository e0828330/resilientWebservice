package database;

import javax.persistence.EntityManagerFactory;

public class DBConnector {
	private EntityManagerFactory emf = null;
	private static DBConnector instance = null;
	
	private DBConnector() {
		
	}

	public static DBConnector getInstance() {
		if (instance == null) {
			instance = new DBConnector();
		}
		return instance;
	}

	public EntityManagerFactory getEMF() {
		return emf;
	}
	
	public void close() {
		emf.close();
	}
	
}

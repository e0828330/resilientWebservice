package database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("serviceDB");
		}
		return emf;
	}
	
	public void close() {
		if (emf != null) {
			emf.close();
		}
	}
	
}

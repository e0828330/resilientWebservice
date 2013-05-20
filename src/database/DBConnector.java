package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	private Connection connect = null;
	private static DBConnector instance = null;
	
	private String host = "localhost";
	private String user = "root";
	private String password = "";
	private String database = "resilientWS";
	
	
	private DBConnector() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager .getConnection("jdbc:mysql://" + host + "/" + database + "?user=" + user + "&password=" + password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	static DBConnector getInstance() {
		if (instance == null) {
			instance = new DBConnector();
		}
		return instance;
	}

	public Connection getConnect() {
		return connect;
	}
	
}

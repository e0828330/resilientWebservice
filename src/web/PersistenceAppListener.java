package web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import database.DBConnector;

public class PersistenceAppListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DBConnector.getInstance().close();
	}

}

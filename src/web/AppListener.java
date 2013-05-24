package web;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import monitor.Monitor;
import monitor.MonitorManager;

import database.DBConnector;
import database.dao.ResourceFactory;
import database.entity.WebService;

public class AppListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		List<WebService> services = ResourceFactory.getServiceDao().getAll();
		MonitorManager monitorMg = MonitorManager.getInstance();
		for (WebService service : services) {
			monitorMg.addMonitor(new Monitor(service.getUrl()));
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		DBConnector.getInstance().close();
	}

}

package monitor;

import java.util.List;

import org.apache.log4j.Logger;

import database.dao.ResourceFactory;
import database.entity.Data;
import database.entity.WebService;

import utils.ServiceException;
import utils.Soap;

public class Monitor implements Runnable {

	final private long TIMEOUT = 15000;
	
	private String service;
	
	private Logger log = Logger.getLogger(Monitor.class); 
	
	public Monitor(String service) {
		this.service = service;
	}

	@Override
	public void run() {
		log.info("Starting monitoring of " + service);

		WebService webService = ResourceFactory.getServiceDao().getByURL(service);
		List<Data> dataList = webService.getData();
		
		while (true) {
			try {
				Thread.sleep(TIMEOUT);
				// Verify method responses
				for (Data data : dataList) {
					if (!Soap.isAvailable(service)) {
						throw new ServiceException("Server not reachable");
					}
					String msg = Soap.sendRequest(service, data.getMethod(), data.getRequest());
					if (!msg.equals(data.getResponse())) {
						// TODO: Log to DB
						log.warn("Message response changed for method " + data.getMethod() + "!");
					}
				}
				log.debug("Waiting between checks");
			} catch (ServiceException e) {
				// TODO: Log to DB
				log.warn(e.getMessage());
			}
			catch (InterruptedException e) {
				log.info("Monitor will quit!");
				return; // Exit
			} 
		}
	}

	public String getService() {
		return service;
	}

}

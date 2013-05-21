package monitor;

import org.apache.log4j.Logger;

import utils.Misc;
import utils.ServiceException;

public class Monitor implements Runnable {

	final private long TIMEOUT = 15000;
	
	private String service;
	
	private Logger log = Logger.getLogger(Monitor.class); 
	
	public Monitor(String service) {
		this.service = service;
	}

	@Override
	public void run() {
		// TODO: This is just for testing
		Communicator c = new Communicator();
		String expected = "";
		try {
			// TODO: The expected value should come from the database
			expected = c.sendRequest("", "");
	
		} catch (ServiceException e) {
			log.error(e.getMessage());
			return;
		}

		log.debug("expected message = " + expected);
		
		while (true) {
			try {
				Thread.sleep(TIMEOUT);
				if (!Misc.isAvailable("http://localhost:9999/WS/Test?wsdl")) {
					throw new ServiceException("Server not reachable");
				}
				// TODO: Fill in params (service, request from database)
				String msg = c.sendRequest("", "");
				if (!msg.equals(expected)) {
					// TODO: Log to DB
					log.warn("Message response changed!");
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

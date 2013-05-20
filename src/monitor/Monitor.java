package monitor;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import utils.ServiceException;

import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.support.SoapUIException;

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
		} catch (XmlException | IOException | SoapUIException | SubmitException e) {
			log.error(e.getMessage());
			return;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}

		while (true) {
			try {
				Thread.sleep(TIMEOUT);
				// TODO: Fill in params (service, request from database)
				String msg = c.sendRequest("", "");
				if (!msg.equals(expected)) {
					// TODO: Log to DB
					log.warn("Message response changed!");
				}
				log.debug("Waiting between checks");
			} catch (XmlException | IOException | SoapUIException | SubmitException e) {
				log.error(e.getMessage());
			} catch (ServiceException e) {
				// TODO: Log to DB
				log.warn("Service is not reachable!");
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

package monitor;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.support.SoapUIException;

public class Monitor implements Runnable {

	final private long TIMEOUT = 15000;
	
	private String service; 
	
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
			System.out.println(e.getMessage());
			return;
		}

		while (true) {
			try {
				Thread.sleep(TIMEOUT);
				// TODO: Fill in params (service, request from database)
				String msg = c.sendRequest("", "");
				if (!msg.equals(expected)) {
					System.out.println("Message response changed!");
				}
				System.out.println("SLEEP");
			} catch (XmlException | IOException | SoapUIException | SubmitException e) {
				System.out.println(e.getMessage());
			} catch (InterruptedException e) {
				System.out.println("ASKED TO QUIT!");
				return; // Exit
			}
		}
	}

	public String getService() {
		return service;
	}

}

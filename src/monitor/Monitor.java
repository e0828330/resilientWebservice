package monitor;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import database.dao.ILogDao;
import database.dao.ResourceFactory;
import database.entity.Data;
import database.entity.Log;
import database.entity.WebService;

import utils.ServiceException;
import utils.Soap;

public class Monitor implements Runnable {

	final private long TIMEOUT = 15000;
	
	private String service;
	
	private Logger log = Logger.getLogger(Monitor.class); 
	
	public Monitor(String service) {
		this.service = service;

		XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");

	}
	
	/**
	 * Returns the difference between two XML documents or NULL
	 * if they are none
	 * 
	 * @param origXML
	 * @param newML
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	private String getXMlDiff(String origXML, String newML) throws SAXException, IOException {
		DetailedDiff xmlDiff = new DetailedDiff(new Diff(origXML, newML));
		if (xmlDiff.identical()) {
			return null;
		}
	    return xmlDiff.toString().replaceAll("org.custommonkey.xmlunit.DetailedDiff", "");
	}

	private void logChange(WebService webService, String method, Log.Type type, String message) {
		Log entry = new Log();
		entry.setMessage(message);
		entry.setName(method);
		entry.setType(type);
		entry.setTimestamp(new Date());
		entry.setWebservice(webService);
		ResourceFactory.getLogDao().addLog(entry);
	}
	
	@Override
	public void run() {
		log.info("Starting monitoring of " + service);

		WebService webService = ResourceFactory.getServiceDao().getByURL(service);
		List<Data> dataList = webService.getData();
		ILogDao logDao = ResourceFactory.getLogDao();
		
		boolean reachable = true; // TODO: init from last log state
		while (true) {
			try {
				Thread.sleep(TIMEOUT);
				
				/* Check the WSDL contents */
				// TODO
				
				/* Loop through request / response pairs */
				for (Data data : dataList) {
					
					/* Check availability */
					if (!Soap.isAvailable(service)) {
						logChange(webService, "", Log.Type.AVAILABILITY, "Server not reachable");
						reachable = false;
						break; // Exit the loop and check again next round
					} else if (!reachable) {
						logChange(webService, "", Log.Type.AVAILABILITY, "Server is back online");
						reachable = true;
					}
					

					/* Check response */
					String response = null;
					try {
						response = Soap.sendRequest(service, data.getMethod(), data.getRequest());
					}
					catch (ServiceException e) {
						logChange(webService, "", Log.Type.AVAILABILITY, "Server not reachable");
					}

					if (response != null) {
						String xmlDiff = null;
						try {
							xmlDiff = getXMlDiff(data.getResponse(), response);
						} catch (SAXException | IOException e) {
							log.error("Monitor recived an error:" + e.getMessage());
						}

						Log lastLog = logDao.getLastEntryOfType(webService.getId(), data.getMethod(), Log.Type.OPERATION);

						if (xmlDiff != null && (lastLog == null || !xmlDiff.equals(lastLog.getMessage()))) {
							log.warn("Message response changed for method " + data.getMethod() + "!");
							logChange(webService, data.getMethod(), Log.Type.OPERATION, xmlDiff);
						}
						else if (lastLog != null && lastLog.getMessage() != null) {
							if (!lastLog.getMessage().equals("Method is returning the expected result again.")) {
								log.info("Message response for method " + data.getMethod() + " is back to expected.");
								logChange(webService, data.getMethod(), Log.Type.OPERATION, "Method is returning the expected result again.");
							}
						}
					}
					
				}

				log.debug("Waiting between checks");
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

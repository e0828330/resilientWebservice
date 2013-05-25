package monitor;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import utils.ServiceException;
import utils.Soap;
import database.DBConnector;
import database.dao.ILogDao;
import database.dao.IServiceDao;
import database.dao.ResourceFactory;
import database.entity.Data;
import database.entity.Log;
import database.entity.WebService;

public class Monitor implements Runnable {

	final private long TIMEOUT = 15000;

	private String service;

	private WebService webService;
	
	private Logger log = Logger.getLogger(Monitor.class);

	public Monitor(String service) {
		this.service = service;
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		IServiceDao dao = ResourceFactory.getServiceDao(em);
		this.webService = dao.getByURL(service);
		em.close();

		XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");

	}

	/**
	 * Returns the difference between two XML documents or NULL if they are none
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

	/**
	 * Logs message to database
	 * 
	 * @param webService
	 * @param method
	 * @param type
	 * @param message
	 * @param dataId
	 */
	private void logChange(WebService webService, String method, Log.Type type, String message, Long dataId) {
		Log entry = new Log();
		entry.setMessage(message);
		entry.setName(method);
		entry.setType(type);
		entry.setTimestamp(new Date());
		entry.setWebservice(webService);
		entry.setDataId(dataId);
		
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();		
		ResourceFactory.getLogDao(em).addLog(entry);
		tx.commit();
		em.close();
	}

	/**
	 * Checks whether the request returns the expected
	 * response and logs changed to the database
	 *  
	 * @param data
	 */
	private void checkRequest(Data data) {
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		ILogDao logDao = ResourceFactory.getLogDao(em);
		String response = null;
		try {
			response = Soap.sendRequest(service, data.getMethod(), data.getRequest());
		} catch (ServiceException e) {
			return;
		}

		if (response != null) {
			String xmlDiff = null;
			try {
				xmlDiff = getXMlDiff(data.getResponse(), response);
			} catch (SAXException | IOException e) {
				log.error("Monitor recived an error:" + e.getMessage());
			}

			Log lastLog = logDao.getLastEntryOfType(webService.getId(), data.getMethod(), Log.Type.OPERATION, data.getId());
			
			if (xmlDiff != null) {
				if (lastLog == null || !lastLog.getMessage().equals(xmlDiff)) {
					log.warn("Message response changed for method " + data.getMethod() + "!");
					logChange(webService, data.getMethod(), Log.Type.OPERATION, xmlDiff, data.getId());
				}
			} else if (lastLog != null && !lastLog.getMessage().equals("Method is returning the expected result again.")) {
				log.info("Message response for method " + data.getMethod() + " is back to expected.");
				logChange(webService, data.getMethod(), Log.Type.OPERATION, "Method is returning the expected result again.", data.getId());
			}
		}
		
		em.close();		
	}
	
	/**
	 * Checks whether the WSDL file has changed
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	private void checkWSDL() throws IOException, SAXException {
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		ILogDao logDao = ResourceFactory.getLogDao(em);
        String response = Soap.downloadWSDL(service);

        String xmlDiff = getXMlDiff(webService.getWsdl(), response);
        
        Log lastLog = logDao.getLastEntryOfType(webService.getId(), null, Log.Type.WSDL, null);
        
        if (xmlDiff != null) {
			if (lastLog == null || !lastLog.getMessage().equals(xmlDiff)) {
				log.warn("Original WSDL changed!");
				logChange(webService, null, Log.Type.WSDL, xmlDiff, null);
			}
		} else if (lastLog != null && !lastLog.getMessage().equals("WSDL is returning the expected result again.")) {
			logChange(webService, null, Log.Type.WSDL,  "WSDL is returning the expected result again.", null);
		}
		em.close();	        
	}

	@Override
	public void run() {
		log.info("Starting monitoring of " + service);

		List<Data> dataList = webService.getData();
		EntityManagerFactory emf = DBConnector.getInstance().getEMF();
		EntityManager em = emf.createEntityManager();
		ILogDao logDao = ResourceFactory.getLogDao(em);


		while (true) {
			try {
				Thread.sleep(TIMEOUT);

				/* Check the WSDL contents */
				try {
					checkWSDL();
				} catch (IOException | SAXException e) {
					log.warn("Error while checking WSDL contents");
				}

				/* Loop through request / response pairs */
				for (Data data : dataList) {

					/* Check availability */
					if (!Soap.isAvailable(service)) {						
						Log lastLog = logDao.getLastEntryOfType(webService.getId(), null, Log.Type.AVAILABILITY, null);
						if (lastLog == null || !lastLog.getMessage().equals("Server not reachable")) {
							logChange(webService, null, Log.Type.AVAILABILITY, "Server not reachable", null);
						}
						
						break; // Exit the loop and check again next round
					} else {
						Log lastLog = logDao.getLastEntryOfType(webService.getId(), null, Log.Type.AVAILABILITY, null);
						if (lastLog != null && !lastLog.getMessage().equals("Server is back online")) {
							logChange(webService, null, Log.Type.AVAILABILITY, "Server is back online", null);
						}
					}

					/* Check request */
					checkRequest(data);

				}

				log.debug("Waiting between checks");
			} catch (InterruptedException e) {
				log.info("Monitor will quit!");
				em.close();
				return; // Exit
			}
		}
	}

	public String getService() {
		return service;
	}

}

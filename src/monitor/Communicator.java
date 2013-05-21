package monitor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import utils.ServiceException;

public class Communicator {

	/**
	 * Sends a request to the webservice specified
	 * and returns the response as XML string
	 * 
	 * @param serviceUrl
	 * @param xmlRequest
	 * @return
	 * @throws ServiceException
	 */
	public String sendRequest(String serviceUrl, String xmlRequest) throws ServiceException {
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		try {
			SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
			MessageFactory msgFactory = MessageFactory.newInstance();
	
		    SOAPConnection connection = sfc.createConnection();
			
		    MimeHeaders mimeHeaders = new MimeHeaders();
	        mimeHeaders.addHeader("Content-Type","text/xml; charset=UTF-8");

	        // TODO: hardcoded
	        xmlRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ser='http://service/'>" + "<soapenv:Header/>" + "<soapenv:Body>"
					+ "<ser:doCalc>" + "<a>18</a>" + "<b>50</b>" + "</ser:doCalc>" + "</soapenv:Body>" + "</soapenv:Envelope>";
	        
	        // TODO: hardcoded
	        URL endpoint = new URL("http://localhost:9999/WS/Test");
	        SOAPMessage response = connection.call(msgFactory.createMessage(mimeHeaders, new ByteArrayInputStream(xmlRequest.getBytes())), endpoint);
	        
			response.writeTo(result);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return result.toString();
	}

}

package monitor;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import utils.Misc;
import utils.ServiceException;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;

public class Communicator {

	/**
	 * Sends a request to the webservice specified
	 * and returns the response as XML string
	 * 
	 * @param serviceUrl
	 * @param xmlRequest
	 * @return
	 * @throws XmlException
	 * @throws IOException
	 * @throws SoapUIException
	 * @throws SubmitException
	 * @throws ServiceException 
	 */
	public String sendRequest(String serviceUrl, String xmlRequest) throws XmlException, IOException, SoapUIException, SubmitException, ServiceException {
		// TODO: This is a random test ...
		SoapUI.setStandalone(true);
		WsdlProject project = new WsdlProject();
		project.setTimeout(1500);
		
		if (!Misc.isAvailable("http://localhost:9999/WS/Test?wsdl")) {
			throw new ServiceException("Server not reachable");
		}

		WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, "http://localhost:9999/WS/Test?wsdl", true)[0];
		WsdlOperation operation = (WsdlOperation) iface.getOperationByName("doCalc");
		WsdlRequest request = operation.addNewRequest("My request");

		String xml = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ser='http://service/'>" + "<soapenv:Header/>" + "<soapenv:Body>"
					+ "<ser:doCalc>" + "<a>18</a>" + "<b>50</b>" + "</ser:doCalc>" + "</soapenv:Body>" + "</soapenv:Envelope>";

		request.setRequestContent(xml);
		WsdlSubmit<?> submit = (WsdlSubmit<?>) request.submit( new WsdlSubmitContext(request), false );
		
		Response response = submit.getResponse();
		String result = response.getContentAsString();
		SoapUI.shutdown();

		return result;
	}

}

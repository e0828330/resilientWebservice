package utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;

public class Soap {

	/**
	 * Returns a Map containing methods names as key
	 * and request templates as values.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getRequests(String serviceUrl) throws Exception {
		HashMap<String, String> result = new HashMap<>();
		WsdlProject project = new WsdlProject();
		WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, serviceUrl);
		WsdlInterface wsdl = wsdls[0];
		for (Operation operation : wsdl.getOperationList()) {
			WsdlOperation op = (WsdlOperation) operation;
			result.put(op.getName(), op.createRequest(true));
		}
	
		SoapUI.shutdown();

		return result;
	}
	
	/**
	 * Returns a Map containing the method name as key, with
	 * the list of parameters as value.
	 * 
	 * @param serviceUrl
	 * @return
	 * @throws Exception
	 */
	public static Map<String, ArrayList<String>> getMethods(String serviceUrl) throws Exception {
		Map <String, String> requests = getRequests(serviceUrl);
		Map<String, ArrayList<String>> result = new HashMap<>();
		for (String method : requests.keySet()) {
			ArrayList<String> tmp = (ArrayList<String>) getParams(requests.get(method));
			result.put(method, tmp);
		}
		return result;
	}
	
	/**
	 * Returns a list of parameters for a particular method
	 * 
	 * @param methodRequest
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static List<String> getParams(String methodRequest) throws ParserConfigurationException, SAXException, IOException {
		ArrayList<String> result = new ArrayList<>(); 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder loader = factory.newDocumentBuilder();
		Document document = loader.parse(new InputSource(new StringReader(methodRequest)));

		DocumentTraversal traversal = (DocumentTraversal) document;

		NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

		for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
			Element element = ((Element) n);
			if (element.getTextContent().equals("?")) {
				result.add(element.getTagName());
			}
		}

		return result;
	}
	
}

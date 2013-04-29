package web;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import utils.Misc;
import biz.source_code.miniTemplator.MiniTemplator;

import com.sun.xml.messaging.saaj.util.ByteOutputStream;

/**
 * Servlet implementation class Index
 */
@WebServlet("/ServiceGenerator")
public class ServiceGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ServiceGenerator() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/resilientWebservice/");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// TODO: Add to database etc, this is just a quick test
			String wsdl = generateWSDL(request.getParameter("wsdl"), 1L);
			response.setContentType("text/xml");
			response.getWriter().print(wsdl);
		} catch (Exception e) {
			MiniTemplator.TemplateSpecification tplSpec = new MiniTemplator.TemplateSpecification();
			tplSpec.templateFileName = Misc.getTemplatePath(this, "info.html");
			
			MiniTemplator tpl = new MiniTemplator(tplSpec);
			tpl.setVariable("message", "Could not create WSDL for your service <b>" + e.getMessage() + "</b>");
			
			response.getWriter().print(tpl.generateOutput());
		}
		
	}
	
	/**
	 * Generates a WSDL document for the given service and appends the
	 * id to the resilent service for identification
	 * 
	 * @param url
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private String generateWSDL(String url, Long id) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		URL baseWSDL = new URL("http://localhost:8080/resilientWebservice/service?wsdl");
		Document baseDoc = builder.parse(new InputSource(baseWSDL.openStream()));

		URL sourceWSDL = new URL(url);
		Document targetDoc = builder.parse(new InputSource(sourceWSDL.openStream()));

		NamedNodeMap attrs = targetDoc.getDocumentElement().getAttributes();

		/* Find wsdl / soap prefix if any */
		String wsdl_ns = "http://schemas.xmlsoap.org/wsdl";
		String wsdl_prefix = "";

		for (int i = 0; i < attrs.getLength(); i++) {
			Node node = attrs.item(i);
			if (node.getNodeValue().startsWith(wsdl_ns) && node.getNodeName().startsWith("xmlns:")) {
				String[] tmp = node.getNodeName().split(":");
				wsdl_prefix = tmp[1] + ":";
			}
		}
		
		// Make sure we have the tns prefix for the target namespace
		if (attrs.getNamedItem("xmlns:tns") == null && attrs.getNamedItem("targetNamespace") != null) {
			targetDoc.getDocumentElement().setAttribute("xmlns:tns", attrs.getNamedItem("targetNamespace").getNodeValue());
		}

		// Add our namespace prefix
		targetDoc.getDocumentElement().setAttribute("xmlns:rws", "http://rws.tuwien.ac.at/DigitalPreservation");
		
		// Add new schema to types
		Node types = targetDoc.getElementsByTagName(wsdl_prefix.isEmpty() ? "types" : wsdl_prefix + "types").item(0);
		if (types == null) {
			types = targetDoc.getElementsByTagName("types").item(0);
		}
		Node newSchema =  baseDoc.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema").item(0);
		types.appendChild(targetDoc.adoptNode(newSchema.cloneNode(true)));
		
		// Copy messages
		Node firstMessage = targetDoc.getElementsByTagName(wsdl_prefix.isEmpty() ? "message" : wsdl_prefix + "message").item(0);
		if (firstMessage == null) {
			firstMessage = targetDoc.getElementsByTagName("message").item(0);
		}
		NodeList nl = baseDoc.getElementsByTagName("message");

		for(int i = 0; i < nl.getLength(); i++) {
			Node newNode = targetDoc.adoptNode(nl.item(i).cloneNode(true));
			NodeList sub = newNode.getChildNodes();
			// We need to replace the tns: prefix with our own
			for (int j = 0; j < sub.getLength(); j++) {
				if (sub.item(j).getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				attrs = sub.item(j).getAttributes();
				for (int k = 0; k < attrs.getLength(); k++) {
					if (attrs.item(k).getNodeValue().startsWith("tns:")) {
						attrs.item(k).setNodeValue(attrs.item(k).getNodeValue().replaceAll("tns:", "rws:"));
					}
				}
			}
			targetDoc.getDocumentElement().insertBefore(newNode, firstMessage);
		}
		
		// Copy portType
		Node oldPortType = targetDoc.getElementsByTagName(wsdl_prefix.isEmpty() ? "portType" : wsdl_prefix + "portType").item(0);
		if (oldPortType == null) {
			oldPortType = targetDoc.getElementsByTagName("portType").item(0);
		}
		Node newPortType = baseDoc.getElementsByTagName("portType").item(0);
		oldPortType.getParentNode().insertBefore(targetDoc.adoptNode(newPortType.cloneNode(true)), oldPortType);

		// Copy binding
		Node oldBinding = targetDoc.getElementsByTagName(wsdl_prefix.isEmpty() ? "binding" : wsdl_prefix + "binding").item(0);
		if (oldBinding == null) {
			oldBinding = targetDoc.getElementsByTagName("binding").item(0);
		}
		// We need the topmost binding element
		while (!oldBinding.getParentNode().isSameNode(targetDoc.getDocumentElement())) {
			oldBinding = oldBinding.getParentNode();
		}
		Node newBinding = baseDoc.getElementsByTagName("binding").item(0);
		oldBinding.getParentNode().insertBefore(targetDoc.adoptNode(newBinding.cloneNode(true)), oldBinding);
		
		// Add new schema to types
		Node service = targetDoc.getElementsByTagName(wsdl_prefix.isEmpty() ? "service" : wsdl_prefix + "service").item(0);
		if (service == null) {
			service = targetDoc.getElementsByTagName("service").item(0);
		}
		Node newPort =  baseDoc.getElementsByTagName("port").item(0);
		nl = newPort.getChildNodes();
		// Append ID to service URL
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals("soap:address")) {
				Element address = (Element) nl.item(i);
				address.setAttribute("location", address.getAttributes().getNamedItem("location").getNodeValue() + "?" + id);
			}
		}
		service.appendChild(targetDoc.adoptNode(newPort.cloneNode(true)));
				
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		ByteOutputStream output = new ByteOutputStream();
		
		DOMSource source = new DOMSource(targetDoc);
		StreamResult result = new StreamResult(output);
		transformer.transform(source, result);
		
		return output.toString();
	}

}

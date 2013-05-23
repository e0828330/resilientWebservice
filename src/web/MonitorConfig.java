package web;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import utils.Base64;
import utils.Misc;
import utils.RandomData;
import utils.Soap;
import biz.source_code.miniTemplator.MiniTemplator;
import database.dao.IServiceDao;
import database.dao.ResourceFactory;
import database.entity.Data;
import database.entity.WebService;

/**
 * Servlet implementation class MonitorConfig
 */
@WebServlet("/MonitorConfig")
public class MonitorConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public enum ValueType {
		EMPTY, NUMBER, WORD, OTHER
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MonitorConfig() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MiniTemplator.TemplateSpecification tplSpec = new MiniTemplator.TemplateSpecification();
		tplSpec.templateFileName = Misc.getTemplatePath(this, "config.html");

		Map<String, ArrayList<String>> methods = null;

		/*
		DebugCode ..
		try {
			MonitorManager mg = MonitorManager.getInstance();
			mg.addMonitor(new Monitor(""));
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		
		try {
			methods = Soap.getMethods(request.getParameter("wsdl"));
			MiniTemplator tpl = new MiniTemplator(tplSpec);
			tpl.setVariable("service", request.getParameter("wsdl"));

			for (String method : methods.keySet()) {
				tpl.setVariable("name", method);
				ArrayList<String> params = methods.get(method);
				for (String param : params) {
					tpl.setVariable("param", param);
					tpl.setVariable("empty", ValueType.EMPTY.toString());
					tpl.setVariable("number", ValueType.NUMBER.toString());
					tpl.setVariable("word", ValueType.WORD.toString());
					tpl.setVariable("other", ValueType.OTHER.toString());
					tpl.setVariable("idprefix", Base64.encodeBytes((method + " " + param).getBytes()).replaceAll("=", "X"));
					tpl.addBlock("param");
				}
				tpl.addBlock("method");
			}

			response.getWriter().print(tpl.generateOutput());
		} catch (Exception e) {
			tplSpec = new MiniTemplator.TemplateSpecification();
			tplSpec.templateFileName = Misc.getTemplatePath(this, "info.html");

			MiniTemplator tpl = new MiniTemplator(tplSpec);
			tpl.setVariable("message", "Could not list methods of your service <b>" + e.getMessage() + "</b>");

			response.getWriter().print(tpl.generateOutput());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			WebService service = new WebService();
			/* Web service static fields */
			service.setTimestamp(new Date());
			service.setVersion(request.getParameter("version"));
			service.setHWinfo(request.getParameter("hwconfig"));
			service.setSWinfo(request.getParameter("swconfig"));
			service.setUrl(request.getParameter("service"));
			
			
			
			Map<String, ArrayList<String>> methods = Soap.getMethods(request.getParameter("service"));
			Map<String, String> requestTemplates = Soap.getRequests(request.getParameter("service"));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder loader = factory.newDocumentBuilder();
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			RandomData randomData = new RandomData();
			
			response.getWriter().println("REQUESTS:");
			
			for (String method : requestTemplates.keySet()) {
				/* Generate 3 requests per method */
				for (int i = 0; i < 3; i++) {
					Document document = loader.parse(new InputSource(new StringReader(requestTemplates.get(method))));
					ArrayList<String> params = methods.get(method);
	
					for (String param : params) {
						ValueType type = ValueType.valueOf(request.getParameter(method + "_" + param));
						Element paramElement = (Element) document.getElementsByTagName(param).item(0);
						
						/* Generate param values */
						switch (type) {
							case EMPTY:
								paramElement.setTextContent("");
								break;
							case NUMBER:
								paramElement.setTextContent(randomData.getNumber());
								break;
							case WORD:
								paramElement.setTextContent(randomData.getWord());
								break;
							case OTHER:
								String[] list = request.getParameter(method + "_" + param + "_data").split("\n");
								paramElement.setTextContent(randomData.getFromList(list));
								break;
	
							default:
								break;
						}
					}
					
					
					StringWriter sw = new StringWriter();
					StreamResult result = new StreamResult(sw);
					DOMSource source = new DOMSource(document);
					trans.transform(source, result);
					String xmlString = sw.toString();
					
					response.getWriter().println(xmlString);
					
					/* Add new request -> response pair */
					Data tmp = new Data();
					tmp.setMethod(method);
					tmp.setRequest(xmlString);
					tmp.setResponse(Soap.sendRequest(service.getUrl(), method, xmlString));
					tmp.setWebservice(service);
					service.addData(tmp);
					
				}
			}
			
			IServiceDao dao = ResourceFactory.getServiceDao();
			dao.addService(service);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

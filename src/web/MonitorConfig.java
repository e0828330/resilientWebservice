package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.Monitor;
import utils.Misc;
import utils.Soap;
import biz.source_code.miniTemplator.MiniTemplator;

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

		// TODO: Remove
		boolean testMonitor = false;
		if (testMonitor) {
			Thread test = new Thread(new Monitor());
			test.start();
			
			try {
				Thread.sleep(60000);
				test.interrupt();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

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
		// TODO Auto-generated method stub
		try {
			Map<String, ArrayList<String>> methods = Soap.getMethods(request.getParameter("service"));

			for (String method : methods.keySet()) {
				response.getWriter().println("M - " + method);
				ArrayList<String> params = methods.get(method);
				for (String param : params) {
					response.getWriter().println("\t" + param + " : " + request.getParameter(method + "_" + param));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

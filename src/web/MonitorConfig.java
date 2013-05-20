package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Soap;
import utils.Misc;
import biz.source_code.miniTemplator.MiniTemplator;

/**
 * Servlet implementation class MonitorConfig
 */
@WebServlet("/MonitorConfig")
public class MonitorConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public enum ValueType {
		NUMBER,
		WORD,
		OTHER
	}
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonitorConfig() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MiniTemplator.TemplateSpecification tplSpec = new MiniTemplator.TemplateSpecification();
		tplSpec.templateFileName = Misc.getTemplatePath(this, "config.html");
		
		Map<String, ArrayList<String>> methods = null;
		
		try {
			// TODO: is hardcoded
			methods = Soap.getMethods("http://www.predic8.com:8080/shop/ShopService?wsdl");
		} catch (Exception e) {
			tplSpec = new MiniTemplator.TemplateSpecification();
			tplSpec.templateFileName = Misc.getTemplatePath(this, "info.html");
			
			MiniTemplator tpl = new MiniTemplator(tplSpec);
			tpl.setVariable("message", "Could not list methods of your service <b>" + e.getMessage() + "</b>");
			
			response.getWriter().print(tpl.generateOutput());
		} 
		
		MiniTemplator tpl = new MiniTemplator(tplSpec);
		
		for (String method : methods.keySet()) {
			tpl.setVariable("name", method);
			ArrayList<String> params = methods.get(method);
			for (String param : params) {
				tpl.setVariable("param", param);
				tpl.setVariable("number", ValueType.NUMBER.toString());
				tpl.setVariable("word", ValueType.WORD.toString());
				tpl.setVariable("other", ValueType.OTHER.toString());
				tpl.addBlock("param");
			}
			tpl.addBlock("method");
		}
		
		response.getWriter().print(tpl.generateOutput());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

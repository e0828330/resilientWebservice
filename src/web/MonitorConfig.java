package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MiniTemplator.TemplateSpecification tplSpec = new MiniTemplator.TemplateSpecification();
		tplSpec.templateFileName = Misc.getTemplatePath(this, "config.html");
		
		MiniTemplator tpl = new MiniTemplator(tplSpec);
		tpl.setVariable("param", "Test");
		tpl.setVariable("name", "Method");
		tpl.setVariable("number", ValueType.NUMBER.toString());
		tpl.setVariable("word", ValueType.WORD.toString());
		tpl.setVariable("other", ValueType.OTHER.toString());
		tpl.addBlock("param");
		
		tpl.setVariable("param", "Test2");
		tpl.setVariable("number", ValueType.NUMBER.toString());
		tpl.setVariable("word", ValueType.WORD.toString());
		tpl.setVariable("other", ValueType.OTHER.toString());
		tpl.addBlock("param");
		
		tpl.addBlock("method");

		
		tpl.setVariable("name", "Method2");
		tpl.setVariable("param", "date");
		tpl.setVariable("number", ValueType.NUMBER.toString());
		tpl.setVariable("word", ValueType.WORD.toString());
		tpl.setVariable("other", ValueType.OTHER.toString());
		tpl.addBlock("param");

		tpl.addBlock("method");
		
		
		
		response.getWriter().print(tpl.generateOutput());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

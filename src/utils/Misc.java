package utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class Misc {

	/**
	 * Returns the absolute path to a template for use by the template engine
	 * 
	 * @param servlet
	 * @param filename
	 * 
	 * @return
	 */
	public static String getTemplatePath(HttpServlet servlet, String filename) {
		ServletContext servletContext = servlet.getServletContext();
		return servletContext.getRealPath("/templates/" + filename);
	}

}

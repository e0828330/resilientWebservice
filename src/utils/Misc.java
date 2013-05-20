package utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

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

	/**
	 * Checks whether the given URL is available
	 * @param url
	 * @return
	 */
	public static boolean isAvailable(String url) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				return true;
			}
		} catch (ProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}

}

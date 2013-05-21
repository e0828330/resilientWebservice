package utils;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4180815663854161756L;

	public ServiceException(String msg) {
		super(msg);
	}
	
	public ServiceException(Throwable t) {
		super(t);
	}
	
}

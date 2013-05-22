package entity;

public class Data {

	private Long id;
	
	private String request;
	
	private String response;
	
	public Data() {
		
	}
	
	public Data(String request, String response) {
		this.request = request;
		this.response = response;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
}

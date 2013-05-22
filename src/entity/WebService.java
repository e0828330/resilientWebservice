package entity;

import java.sql.Timestamp;

public class WebService {

	private Long id;
	
	private String url;
	
	private Timestamp timestamp;
	
	private String wsdl;
	
	private String version;
	
	private String HWinfo;
	
	private String SWinfo;
	
	public WebService() {
		
	}
	
	public WebService (String url, String wsdl, String version, String HWinfo, String SWinfo) {
		this.url = url;
		this.wsdl = wsdl;
		this.version = version;
		this.HWinfo = HWinfo;
		this.SWinfo = SWinfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getWsdl() {
		return wsdl;
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHWinfo() {
		return HWinfo;
	}

	public void setHWinfo(String hWinfo) {
		HWinfo = hWinfo;
	}

	public String getSWinfo() {
		return SWinfo;
	}

	public void setSWinfo(String sWinfo) {
		SWinfo = sWinfo;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
	
}

package resilient.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="changes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Changes {

	@XmlElement(name="change", type=Change.class)
	private List<Change> data;

	public List<Change> getData() {
		return data;
	}
	
	public void addData(Change item) {
		if (this.data == null) {
			this.data = new ArrayList<Change>();
		}
		this.data.add(item);
	}
}

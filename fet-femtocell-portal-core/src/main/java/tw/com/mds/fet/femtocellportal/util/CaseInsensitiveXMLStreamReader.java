package tw.com.mds.fet.femtocellportal.util;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

public class CaseInsensitiveXMLStreamReader extends StreamReaderDelegate {

	public CaseInsensitiveXMLStreamReader(XMLStreamReader reader) {
		super(reader);
	}

	@Override
	public String getAttributeLocalName(int index) {
		return super.getAttributeLocalName(index).toLowerCase();
	}

	@Override
	public String getLocalName() {
		return super.getLocalName().toLowerCase();
	}
	
}

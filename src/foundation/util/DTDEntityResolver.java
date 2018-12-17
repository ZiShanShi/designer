package foundation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DTDEntityResolver implements EntityResolver{

	private InputSource inputSource;
	
	public DTDEntityResolver(String path, String systemId) throws FileNotFoundException {
		InputStream iStream = new FileInputStream(new File(path, systemId));
		if (iStream != null) {
			inputSource = new InputSource(iStream);
		}
	}
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		return inputSource;
	}

}

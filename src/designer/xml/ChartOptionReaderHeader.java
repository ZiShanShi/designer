package designer.xml;

import designer.options.ChartOption;
import foundation.util.Util;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kimi
 * @description chartOption 读取器
 * @date 2018-12-19 10:53
 */


public class ChartOptionReaderHeader extends DefaultHandler {

    private ChartOption chartOption;
    private String content = null;
    private Boolean isTakeDefaultPath = false;
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        chartOption = new ChartOption();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        switch (qName) {
            case "defaultPath":

                break;
        
            default:
                break;
        
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch (qName) {
            case "defaultPath":
                isTakeDefaultPath = Util.stringToBoolean(content);
                break;
        
            default:
                break;
        
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        content = new String(ch, start, length);
    }
}

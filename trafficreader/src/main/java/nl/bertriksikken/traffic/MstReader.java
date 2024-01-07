package nl.bertriksikken.traffic;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class MstReader {

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        MstReader mstReader = new MstReader();
        File file = new File("/home/bertrik/temp/rws/measurement_current.xml");
        mstReader.parseVersionInfo(file);
    }

    MstReader() throws SAXException, IOException, ParserConfigurationException {
    }

    void parseVersionInfo(File file) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        UserHandler userhandler = new UserHandler();
        saxParser.parse(file, userhandler);
    }

    private final class UserHandler extends DefaultHandler {

        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            System.out.println("qName = " + qName);
            if (qName.equals("publicationTime")) {
                System.out.println("Found publicationTime: ");
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("publicationTime")) {
                System.out.println("Found publicationTime: ");
            }
        }
    }

}

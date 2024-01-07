package nl.bertriksikken.traffic;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class MstVersionReader {
    
    private static final String FIELD_MEASUREMENT_SITE_TABLE = "measurementSiteTable";
    private static final String FIELD_PUBLICATION_TIME = "publicationTime";
    private static final String FIELD_VERSION_ATTR = "version";
    private static final String FIELD_ID_ATTR = "id";
    
    private String dateTime;
    private String id;
    private String version;
    
    public boolean parse(InputStream stream) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(stream);
        boolean haveTime = false;
        boolean haveVersion = false;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                case FIELD_PUBLICATION_TIME:
                    XMLEvent data = eventReader.peek();
                    if (data.isCharacters()) {
                        dateTime = data.asCharacters().getData();
                        haveTime = true;
                    }
                    break;
                case FIELD_MEASUREMENT_SITE_TABLE:
                    id = startElement.getAttributeByName(new QName(FIELD_ID_ATTR)).getValue();
                    version = startElement.getAttributeByName(new QName(FIELD_VERSION_ATTR)).getValue();
                    haveVersion = true;
                    break;
                default:
                    break;
                }
            }
            if (haveTime && haveVersion) {
                return true;
            }
        }
        return false;
    }

    public String getDateTime() {
        return dateTime;
    }
    
    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }
    
}

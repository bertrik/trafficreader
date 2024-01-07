package nl.bertriksikken.traffic.datex2;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import nl.bertriksikken.traffic.datex2.D2LogicalModel.MeasuredDataPublication;
import nl.bertriksikken.traffic.datex2.D2LogicalModel.MeasuredDataPublication.SiteMeasurements;

public final class AvgTrafficReaderTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(AvgTrafficReaderTest.class);
    
    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new XmlMapper();
        URL url = getClass().getClassLoader().getResource("trafficspeed.xml");

        LOG.info("Deserializing");
        JsonNode node = mapper.readValue(url, JsonNode.class);
        JsonNode d2LogicalModel = node.at("/Body/d2LogicalModel");
        D2LogicalModel model = mapper.treeToValue(d2LogicalModel, D2LogicalModel.class);
        
        LOG.info("Parsing");
        MeasuredDataPublication measuredDataPublication = (MeasuredDataPublication) model.payloadPublication;
        int numMeasurements = 0;
        int numSites = 0;
        for (SiteMeasurements measurements : measuredDataPublication.siteMeasurementsList) {
            for (MeasuredValue value : measurements.measuredValueList) {
                numMeasurements++;
            }
            numSites++;
        }
        
        LOG.info("Parsed {} sites, {} measurements", numSites, numMeasurements);
    }
    
}

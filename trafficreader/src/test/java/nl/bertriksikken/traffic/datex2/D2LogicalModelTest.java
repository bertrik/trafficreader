package nl.bertriksikken.traffic.datex2;

import java.time.Instant;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import nl.bertriksikken.traffic.datex2.D2LogicalModel.MeasuredDataPublication;
import nl.bertriksikken.traffic.datex2.D2LogicalModel.MeasuredDataPublication.SiteMeasurements;
import nl.bertriksikken.traffic.datex2.D2LogicalModel.MeasuredDataPublication.SiteMeasurements.Reference;
import nl.bertriksikken.traffic.datex2.MeasuredValue.TrafficFlow;
import nl.bertriksikken.traffic.datex2.MeasuredValue.TrafficSpeed;

public final class D2LogicalModelTest {

    @Test
    public void testSerialize() throws JsonProcessingException {
        MeasuredDataPublication measuredDataPublication = new MeasuredDataPublication(Instant.now());
        D2LogicalModel model = new D2LogicalModel(measuredDataPublication);

        Reference reference = new Reference("id", "version");
        SiteMeasurements siteMeasurements = new SiteMeasurements(reference, Instant.now());
        siteMeasurements.addMeasuredValue(new TrafficFlow(5, 1));
        siteMeasurements.addMeasuredValue(new TrafficSpeed(80, 1));
        measuredDataPublication.addSiteMeasurements(siteMeasurements);

        XmlMapper mapper = new XmlMapper();
        String xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
        System.out.println(xml);
    }
    
}

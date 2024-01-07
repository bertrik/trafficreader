package nl.bertriksikken.traffic.datex2;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import nl.bertriksikken.traffic.datex2.MeasuredValue.BasicData;

@JacksonXmlRootElement(localName = "d2LogicalModel")
@JsonIgnoreProperties(ignoreUnknown = true)
public final class D2LogicalModel {

    @JacksonXmlProperty(localName = "modelBaseVersion", isAttribute = true)
    String modelBaseVersion = "2";

    @JacksonXmlProperty(localName = "payloadPublication")
    PayloadPublication payloadPublication;
    
    // jackson constructor
    D2LogicalModel() {
        this(null);
    }
    
    public D2LogicalModel(PayloadPublication payloadPublication) {
        this.payloadPublication = payloadPublication;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @Type(value = MeasuredDataPublication.class, name = "MeasuredDataPublication") })
    public static abstract class PayloadPublication {
        String type;

        @JacksonXmlProperty(localName = "publicationTime")
        String publicationTime;

        // jackson creator
        PayloadPublication() {
        }
        
        PayloadPublication(String type, String publicationTime) {
            this.type = type;
            this.publicationTime = publicationTime;
        }

        PayloadPublication(String type, Instant publicationDateTime) {
            this(type, publicationDateTime.truncatedTo(ChronoUnit.SECONDS).toString());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MeasuredDataPublication extends PayloadPublication {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "siteMeasurements")
        List<SiteMeasurements> siteMeasurementsList = new ArrayList<>();

        public MeasuredDataPublication() {
            // jackson constructor
        }
    
        public MeasuredDataPublication(Instant publicationTime) {
            super("MeasuredDataPublication", publicationTime);
        }

        public void addSiteMeasurements(SiteMeasurements measurements) {
            siteMeasurementsList.add(measurements);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class SiteMeasurements {

            @JacksonXmlProperty(localName = "measurementSiteReference")
            Reference reference;

            @JacksonXmlProperty(localName = "measurementTimeDefault")
            String measurementTimeDefault = "";

            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "measuredValue")
            List<MeasuredValue> measuredValueList = new ArrayList<>();

            // jackson constructor
            SiteMeasurements() {
                this(null, "");
            }
            
            SiteMeasurements(Reference reference, String measurementTimeDefault) {
                this.reference = reference;
                this.measurementTimeDefault = measurementTimeDefault;
            }

            SiteMeasurements(Reference reference, Instant time) {
                this(reference, time.truncatedTo(ChronoUnit.SECONDS).toString());
            }

            public void addMeasuredValue(BasicData basicData) {
                int index = measuredValueList.size() + 1;
                measuredValueList.add(new MeasuredValue(index, basicData));
            }

            public static final class Reference {
                @JacksonXmlProperty(localName = "id", isAttribute = true)
                String id = "";

                @JacksonXmlProperty(localName = "version", isAttribute = true)
                String version = "";

                @JacksonXmlProperty(localName = "targetClass", isAttribute = true)
                String targetClass = "";

                // jackson constructor
                Reference() {
                    this("", "");
                }

                Reference(String id, String version) {
                    this.id = id;
                    this.version = version;
                }
            }
        }

    }
}

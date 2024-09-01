package de.hskl.itanalyst.alwi.osmmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.*;

import java.io.Serializable;

/**
 * XML-Node-Element of osm-File
 * located in osm-xml
 *
 * @author Alexander Wiltz
 * @version 0.1.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeXml implements Serializable {
    @XmlAttribute(name = "id")
    private Long id;

    @XmlAttribute(name = "lat")
    private Double lat;

    @XmlAttribute(name = "lon")
    private Double lon;
}

package de.hskl.itanalyst.alwi.osmmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * XML-Root-Element of osm-File
 *
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
@XmlRootElement(name = "osm")
public class OsmXml implements Serializable {
    @XmlElement(name="node")
    private List<NodeXml> nodes;

    @XmlElement(name="way")
    private List<WayXml> ways;
}

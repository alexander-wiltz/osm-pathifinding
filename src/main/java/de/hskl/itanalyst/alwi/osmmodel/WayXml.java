package de.hskl.itanalyst.alwi.osmmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * XML-Way-Element of osm-File
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
public class WayXml implements Serializable {

    @XmlAttribute(name = "id")
    private Long id;

    @XmlElement(name="nd")
    private List<NdXml> nds;

    @XmlElement(name="tag")
    private List<TagXml> tags;
}
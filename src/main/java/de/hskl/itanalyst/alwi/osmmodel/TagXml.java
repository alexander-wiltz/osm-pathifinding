package de.hskl.itanalyst.alwi.osmmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.*;

import java.io.Serializable;

/**
 * XML-Tag-Element of osm-File
 * located in way-xml
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
public class TagXml implements Serializable {
    @XmlAttribute(name = "k")
    private String key;

    @XmlAttribute(name = "v")
    private String value;
}

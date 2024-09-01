package de.hskl.itanalyst.alwi.osmmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.*;

import java.io.Serializable;

/**
 * XML-Nd-Element of osm-File
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
public class NdXml implements Serializable {
    @XmlAttribute(name = "ref")
    private Long ref;
}

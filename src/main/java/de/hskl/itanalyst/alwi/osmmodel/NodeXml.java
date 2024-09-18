package de.hskl.itanalyst.alwi.osmmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.*;

import java.io.Serializable;

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

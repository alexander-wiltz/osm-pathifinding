package de.hskl.itanalyst.alwi.entities;

import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "factory_edge",
        indexes = {
                @Index(columnList = "from_node_id"),
                @Index(columnList = "to_node_id")
        })
/**
 * FactoryEdge trägt alle Routing-relevanten Parameter (Richtung, Geschwindigkeit, Sperrung, Modusfilter).
 */
public class FactoryEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_node_id")
    private FactoryNode from;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_node_id")
    private FactoryNode to;

    /**
     * true → zusätzlich die Gegenrichtung erzeugen
     */
    @Column(nullable = false)
    private boolean bidirectional = true;

    /**
     * Optional: feste Länge (m); wenn null → aus (x,y) berechnen
     */
    private Double lengthM;

    /**
     * Optional: Geschwindigkeit (m/s); wenn null → Default
     */
    private Double speedMps;

    /**
     * Optional: fester Zeitwert (Sekunden) für diese Kante
     */
    private Double costOverrideSec;

    /**
     * Modusfilter, z.B. "ANY,AGV,FORKLIFT"
     */
    @Column(length = 64)
    private String allowedModes = "ANY";

    @Column(name = "is_blocked")
    private boolean blocked = false;
}

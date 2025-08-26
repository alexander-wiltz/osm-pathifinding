package de.hskl.itanalyst.alwi.entities;

import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "factory_edge")
public class FactoryEdge {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private FactoryNode fromNode;
    @ManyToOne(optional = false)
    private FactoryNode toNode;

    private boolean bidirectional = true;
    private Double lengthM;      // optional (falls nicht gesetzt -> aus (x,y) berechnen)
    private Double speedMps;     // optional (falls null -> default pro Modus/Zonenregel)
    private String allowedModes = "ANY";
    private Double widthM;
    private boolean isBlocked = false;
    private Double costOverride;
}

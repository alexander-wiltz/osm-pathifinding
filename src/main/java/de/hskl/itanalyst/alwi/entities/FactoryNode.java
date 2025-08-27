package de.hskl.itanalyst.alwi.entities;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "factory_node",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"}),
                @UniqueConstraint(columnNames = {"col_letter", "row_odd", "sub_index"})
        },
        indexes = {
                @Index(columnList = "col_letter,row_odd"),
                @Index(columnList = "name")
        })
/**
 * FactoryNode implementiert INode (Id) und XY (x/y), speichert den Originalcode (F089.4) plus die zerlegten Bestandteile für schnelle Suchen.
 */
public class FactoryNode implements INode, XY {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ursprünglicher Code wie "F089.4" (oder "F089")
     */
    @Column(nullable = false, length = 16, unique = true)
    private String code;

    /**
     * Optionaler Anzeigename, z.B. "Anfahrplatz 7"
     */
    @Column(length = 128)
    private String name;

    @Column(name = "col_letter", nullable = false, length = 1)
    private String colLetter;  // "F"

    @Column(name = "row_odd", nullable = false)
    private int rowOdd;        // 89

    @Column(name = "sub_index")
    private Integer subIndex;  // 1..6 oder null

    /**
     * Metrische Koordinaten (Meter, lokales CRS; A01 = 0/0)
     */
    @Column(name = "x_m", nullable = false)
    private double x;

    @Column(name = "y_m", nullable = false)
    private double y;

    /**
     * Optional: Etage/Zonen/Flags
     */
    @Column(length = 32)
    private String floor = "EG";
    @Column(length = 64)
    private String zone;
    @Column(name = "is_blocked")
    private boolean blocked = false;

    // --- INode & XY ---
    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    // INode:
    @Override
    public Long getId() {
        return id;
    }
}

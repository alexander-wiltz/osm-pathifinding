package de.hskl.itanalyst.alwi.entities;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "factory_node", uniqueConstraints = @UniqueConstraint(columnNames = {"col_letter", "row_odd"}))
public class FactoryNode implements INode, XY {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 16)
    private String label;      // "D-11"
    @Column(name = "col_letter", nullable = false, length = 1)
    private String colLetter; // "D"
    @Column(name = "row_odd", nullable = false)
    private int rowOdd; // 11
    @Column(name = "x_m", nullable = false)
    private double x;   // Meter
    @Column(name = "y_m", nullable = false)
    private double y;
    private String floor = "EG";
    private String zone;
    private String nodeType = "CROSSING";
    private boolean isBlocked = false;

    // INode:
    @Override
    public Long getId() {
        return id;
    }
}

package de.hskl.itanalyst.alwi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "streets", indexes = {
        @Index(name = "name_houseNumber_idx",  columnList="name,houseNumber")
})
public class Street implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isBuilding")
    private Boolean isBuilding;

    @Column(name = "name")
    private String name;

    @Column(name = "houseNumber")
    private String houseNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "street_node_relation",
            joinColumns = @JoinColumn(name = "street_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "node_id", referencedColumnName = "id"))
    private Set<Node> nodes;
}

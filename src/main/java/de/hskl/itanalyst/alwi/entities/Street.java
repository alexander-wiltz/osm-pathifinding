package de.hskl.itanalyst.alwi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "streets")
public class Street implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isBuilding")
    private Boolean isBuilding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Street parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Street> children;

    @Column(name = "street")
    private String street;

    @Column(name = "housenumber")
    private String housenumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "street_node_relation",
            joinColumns = @JoinColumn(name = "street_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "node_id", referencedColumnName = "id"))
    private Set<Node> nodes;
}

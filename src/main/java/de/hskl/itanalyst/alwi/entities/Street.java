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
@Table(name = "streets", indexes = {
        @Index(name = "street_housenumber_idx",  columnList="street,housenumber"),
        @Index(name = "fk_parent_idx", columnList = "parent")
})
public class Street implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isBuilding")
    private Boolean isBuilding;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent")
    private Street parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

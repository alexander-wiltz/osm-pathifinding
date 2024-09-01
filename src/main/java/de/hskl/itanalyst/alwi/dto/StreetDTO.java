package de.hskl.itanalyst.alwi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "streets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreetDTO {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "isBuilding")
    private Boolean isBuilding;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private StreetDTO parent;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<StreetDTO> children;

    @Column(name = "street")
    private String street;

    @Column(name = "housenumber")
    private String housenumber;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "street_node_relation",
            joinColumns = @JoinColumn(name = "street_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "node_id", referencedColumnName = "id"))
    private Set<NodeDTO> nodes;
}

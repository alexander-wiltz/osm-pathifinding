package de.hskl.itanalyst.alwi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ways")
public class Way {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isBuilding")
    private Boolean isBuilding;

    @Column(name = "isGarage")
    private Boolean isGarage;

    @Column(name = "highway")
    private String highway;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "housenumber")
    private String housenumber;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "street")
    private String street;

    @Column(name = "junction")
    private String junction;

    @Column(name = "surface")
    private String surface;

    @Column(name = "sport")
    private String sport;

    @Column(name = "amenity")
    private String amenity;

    @Column(name = "religion")
    private String religion;

    @Column(name = "denomination")
    private String denomination;

    @Column(name="refNode")
    private Long refNode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "node_way_relation",
            joinColumns = @JoinColumn(name = "way_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "node_id", referencedColumnName = "id"))
    private Set<Node> nodes;
}

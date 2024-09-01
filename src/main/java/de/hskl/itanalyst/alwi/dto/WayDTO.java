package de.hskl.itanalyst.alwi.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "ways")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WayDTO {

    @Id
    @Column(name = "id")
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

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "node_way_relation",
            joinColumns = @JoinColumn(name = "way_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "node_id", referencedColumnName = "id"))
    private Set<NodeDTO> nodes;
}

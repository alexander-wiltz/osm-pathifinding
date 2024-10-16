package de.hskl.itanalyst.alwi.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WayDTO implements Serializable {
    private Long id;
    private Boolean isBuilding;
    private Boolean isGarage;
    private String highway;
    private String name;
    private String city;
    private String country;
    private String housenumber;
    private String postcode;
    private String street;
    private String junction;
    private String surface;
    private String sport;
    private String amenity;
    private String religion;
    private String denomination;
    private Long refNode;
    private Set<NodeDTO> nodes;

    @Override
    public String toString() {
        return "WayDTO{" +
                "id=" + id + ", name='" + name + '\'' + ", street='" + street + '\'' +  ", highway='" + highway + "'}";
    }
}

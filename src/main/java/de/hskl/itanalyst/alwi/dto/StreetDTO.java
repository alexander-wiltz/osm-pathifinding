package de.hskl.itanalyst.alwi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Set;


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
    private StreetDTO parent;

    @ToString.Exclude
    @JsonManagedReference
    private Collection<StreetDTO> children;

    @Column(name = "street")
    private String street;

    @Column(name = "housenumber")
    private String housenumber;

    @JsonManagedReference
    private Set<NodeDTO> nodes;
}

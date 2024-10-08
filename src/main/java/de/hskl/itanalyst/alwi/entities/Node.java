package de.hskl.itanalyst.alwi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "nodes")
public class Node {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name ="lon", nullable = false)
    private Double longitude;

    @Column(name = "lat", nullable = false)
    private Double latitude;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "nodes")
    private Set<Street> streets;
}

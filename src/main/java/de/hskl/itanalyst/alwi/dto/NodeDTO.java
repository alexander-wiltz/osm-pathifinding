package de.hskl.itanalyst.alwi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "nodes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class NodeDTO {

    @Id
    @NonNull
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name ="lon")
    private Double longitude;

    @NonNull
    @Column(name = "lat")
    private Double latitude;

    @JsonBackReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "nodes")
    private Set<StreetDTO> streets;
}

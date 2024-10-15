package de.hskl.itanalyst.alwi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreetDTO implements Serializable {

    @NonNull
    private Long id;

    @NonNull
    private Boolean isBuilding;

    @JsonBackReference
    private StreetDTO parent;

    @JsonManagedReference
    private Collection<StreetDTO> children;

    @NonNull
    private String street;
    private String houseNumber;

    @JsonManagedReference
    private Set<NodeDTO> nodes;
}

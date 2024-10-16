package de.hskl.itanalyst.alwi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import lombok.*;

import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class NodeDTO implements Serializable, INode {

    @NonNull
    private Long id;
    @NonNull
    private Double longitude;
    @NonNull
    private Double latitude;
    @JsonBackReference
    private Set<StreetDTO> streets;

    @Override
    public Long getId() {
        return id;
    }
}

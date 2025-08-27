package de.hskl.itanalyst.alwi.dto;

import de.hskl.itanalyst.alwi.entities.FactoryEdge;

public record FactoryEdgeResponse(
        Long id,
        Long fromId,
        Long toId,
        boolean bidirectional
) {
    public static FactoryEdgeResponse of(FactoryEdge e) {
        return new FactoryEdgeResponse(e.getId(), e.getFrom().getId(), e.getTo().getId(), e.isBidirectional());
    }
}
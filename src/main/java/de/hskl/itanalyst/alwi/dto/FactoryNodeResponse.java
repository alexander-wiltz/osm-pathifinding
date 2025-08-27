package de.hskl.itanalyst.alwi.dto;

import de.hskl.itanalyst.alwi.entities.FactoryNode;

public record FactoryNodeResponse(
        Long id,
        String code,
        String name,
        double x,
        double y,
        String floor,
        String zone
) {
    public static FactoryNodeResponse of(FactoryNode factoryNode) {
        return new FactoryNodeResponse(factoryNode.getId(), factoryNode.getCode(), factoryNode.getName(), factoryNode.getX(), factoryNode.getY(), factoryNode.getFloor(), factoryNode.getZone());
    }
}

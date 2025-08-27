package de.hskl.itanalyst.alwi.dto;

public record CreateEdgeRequest(
        Long fromId, Long toId, Boolean bidirectional,
        Double speedMps, Double lengthM, Double costOverrideSec,
        String allowedModes, Boolean blocked) {
}

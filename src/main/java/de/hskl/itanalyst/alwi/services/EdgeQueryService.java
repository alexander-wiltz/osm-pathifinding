package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.FactoryEdgeDTO;
import de.hskl.itanalyst.alwi.entities.FactoryNode;
import de.hskl.itanalyst.alwi.repositories.FactoryEdgeRepository;

import de.hskl.itanalyst.alwi.entities.FactoryEdge;
import de.hskl.itanalyst.alwi.utilities.EdgeStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EdgeQueryService {

    private final FactoryEdgeRepository edgeRepo;

    // Defaults wie in deinem RoutingService
    private final double defaultEdgeSpeedMps = 1.8;

    public EdgeQueryService(FactoryEdgeRepository edgeRepo) {
        this.edgeRepo = edgeRepo;
    }

    public List<FactoryEdgeDTO> findAllWithStatus(Set<String> modes) {
        var edges = edgeRepo.findAll();
        return edges.stream().map(e -> toDto(e, modes)).collect(Collectors.toList());
    }

    public FactoryEdgeDTO findOneWithStatus(Long id, Set<String> modes) {
        var e = edgeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge nicht gefunden: " + id));
        return toDto(e, modes);
    }

    private FactoryEdgeDTO toDto(FactoryEdge e, Set<String> modes) {
        var dto = new FactoryEdgeDTO();
        dto.id = e.getId();
        dto.fromId = e.getFrom().getId();
        dto.toId = e.getTo().getId();
        dto.fromCode = safeCode(e.getFrom());
        dto.toCode = safeCode(e.getTo());
        dto.bidirectional = e.isBidirectional();
        dto.blocked = e.isBlocked();
        dto.allowedModes = e.getAllowedModes();
        dto.speedMps = e.getSpeedMps();
        dto.lengthM = e.getLengthM();
        dto.costOverrideSec = e.getCostOverrideSec();

        // Effektive Werte:
        dto.effectiveLengthM = effectiveLength(e);
        dto.effectiveSpeedMps = effectiveSpeed(e);
        dto.effectiveCostSec = effectiveCost(e);

        // Status:
        dto.status = deriveStatus(e, modes);

        return dto;
    }

    private String safeCode(FactoryNode n) {
        return n != null ? n.getCode() : null;
    }

    private double effectiveLength(FactoryEdge e) {
        if (e.getLengthM() != null) return e.getLengthM();
        var a = e.getFrom();
        var b = e.getTo();
        return Math.hypot(a.getX() - b.getX(), a.getY() - b.getY());
    }

    private double effectiveSpeed(FactoryEdge e) {
        var s = e.getSpeedMps();
        return (s != null && s > 0) ? s : defaultEdgeSpeedMps;
    }

    private double effectiveCost(FactoryEdge e) {
        if (e.getCostOverrideSec() != null) return e.getCostOverrideSec();
        return effectiveLength(e) / effectiveSpeed(e);
    }

    private EdgeStatus deriveStatus(FactoryEdge e, Set<String> modes) {
        if (e.isBlocked()) return EdgeStatus.BLOCKED_EDGE;
        if (e.getFrom().isBlocked()) return EdgeStatus.BLOCKED_NODE_FROM;
        if (e.getTo().isBlocked()) return EdgeStatus.BLOCKED_NODE_TO;

        if (modes != null && !modes.isEmpty()) {
            // Modusprüfung: mind. einer aus Anfrage muss in allowedModes matchen
            String allowed = e.getAllowedModes();
            if (allowed != null && !allowed.isBlank()) {
                var allowedSet = Arrays.stream(allowed.split(","))
                        .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
                boolean ok = modes.stream().anyMatch(allowedSet::contains);
                if (!ok) return EdgeStatus.DISALLOWED_MODE;
            }
        }
        return EdgeStatus.ACTIVE;
    }

}

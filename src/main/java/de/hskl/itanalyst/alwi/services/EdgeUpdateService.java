package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.repositories.FactoryEdgeRepository;

import de.hskl.itanalyst.alwi.entities.FactoryEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeUpdateService {

    private final FactoryEdgeRepository edgeRepo;

    public EdgeUpdateService(FactoryEdgeRepository edgeRepo) {
        this.edgeRepo = edgeRepo;
    }

    public static final class UpdateCmd {
        public Boolean bidirectional;
        public Boolean blocked;
        public String allowedModes;
        public Double speedMps;
        public Double lengthM;
        public Double costOverrideSec;
        public Long fromId; // optional: Richtung ändern / neu verdrahten
        public Long toId;
    }

    @Transactional
    public FactoryEdge update(Long id, UpdateCmd cmd) {
        var e = edgeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge nicht gefunden: " + id));

        if (cmd.bidirectional != null) e.setBidirectional(cmd.bidirectional);
        if (cmd.blocked != null) e.setBlocked(cmd.blocked);
        if (cmd.allowedModes != null) e.setAllowedModes(cmd.allowedModes);
        if (cmd.speedMps != null) e.setSpeedMps(cmd.speedMps);
        if (cmd.lengthM != null) e.setLengthM(cmd.lengthM);
        if (cmd.costOverrideSec != null) e.setCostOverrideSec(cmd.costOverrideSec);
        // Optionales Umverkabeln
        if (cmd.fromId != null && (e.getFrom() == null || !e.getFrom().getId().equals(cmd.fromId))) {
            e.getFrom().setId(cmd.fromId); // nur wenn du setId zulässt; sonst NodeService.getById + setFrom(node)
        }
        if (cmd.toId != null && (e.getTo() == null || !e.getTo().getId().equals(cmd.toId))) {
            e.getTo().setId(cmd.toId);
        }
        return edgeRepo.save(e);
    }

    @Transactional
    public FactoryEdge toggleBlocked(Long id, boolean blocked) {
        var e = edgeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge nicht gefunden: " + id));
        e.setBlocked(blocked);
        return edgeRepo.save(e);
    }

    @Transactional
    public FactoryEdge setBidirectional(Long id, boolean bidirectional) {
        var e = edgeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge nicht gefunden: " + id));
        e.setBidirectional(bidirectional);
        return edgeRepo.save(e);
    }

    @Transactional
    public FactoryEdge reverseDirection(Long id) {
        var e = edgeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edge nicht gefunden: " + id));
        var tmp = e.getFrom();
        e.setFrom(e.getTo());
        e.setTo(tmp);
        return edgeRepo.save(e);
    }
}

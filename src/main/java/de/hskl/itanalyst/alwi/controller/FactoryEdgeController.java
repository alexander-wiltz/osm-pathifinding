package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.FactoryEdgeResponse;
import de.hskl.itanalyst.alwi.services.FactoryEdgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factory/edges")
public class FactoryEdgeController {

    private final FactoryEdgeService edgeService;

    public FactoryEdgeController(FactoryEdgeService edgeService) {
        this.edgeService = edgeService;
    }

    public static record CreateEdgeRequest(
            Long fromId, Long toId, Boolean bidirectional,
            Double speedMps, Double lengthM, Double costOverrideSec,
            String allowedModes, Boolean blocked) {
    }

    @PostMapping
    public ResponseEntity<FactoryEdgeResponse> create(@RequestBody CreateEdgeRequest req) {
        var e = edgeService.createEdge(
                req.fromId(), req.toId(),
                req.bidirectional() != null ? req.bidirectional() : true,
                req.speedMps(), req.lengthM(), req.costOverrideSec(),
                req.allowedModes(), req.blocked()
        );
        return ResponseEntity.ok(FactoryEdgeResponse.of(e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        edgeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.FactoryEdgeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.hskl.itanalyst.alwi.services.EdgeQueryService;
import de.hskl.itanalyst.alwi.services.EdgeUpdateService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/factory/edges")
public class FactoryEdgeAdminController {

    private final EdgeQueryService query;
    private final EdgeUpdateService update;

    public FactoryEdgeAdminController(EdgeQueryService query, EdgeUpdateService update) {
        this.query = query;
        this.update = update;
    }

    // --- Anzeigen mit Status ---
    // GET /api/factory/edges/status?modes=ANY,AGV
    @GetMapping("/status")
    public ResponseEntity<List<FactoryEdgeDTO>> allWithStatus(@RequestParam(value = "modes", required = false) String modesCsv) {
        Set<String> modes = parseModes(modesCsv);
        return ResponseEntity.ok(query.findAllWithStatus(modes));
    }

    // GET /api/factory/edges/{id}/status?modes=AGV
    @GetMapping("/{id}/status")
    public ResponseEntity<FactoryEdgeDTO> oneWithStatus(@PathVariable Long id,
                                                        @RequestParam(value = "modes", required = false) String modesCsv) {
        Set<String> modes = parseModes(modesCsv);
        return ResponseEntity.ok(query.findOneWithStatus(id, modes));
    }

    // --- Bearbeiten ---
    public static final class UpdateRequest {
        public Boolean bidirectional;
        public Boolean blocked;
        public String allowedModes;
        public Double speedMps;
        public Double lengthM;
        public Double costOverrideSec;
        public Long fromId;
        public Long toId;
    }

    // PATCH /api/factory/edges/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<FactoryEdgeDTO> patch(@PathVariable Long id, @RequestBody UpdateRequest req) {
        var updateCmd = new EdgeUpdateService.UpdateCmd();
        updateCmd.bidirectional = req.bidirectional;
        updateCmd.blocked = req.blocked;
        updateCmd.allowedModes = req.allowedModes;
        updateCmd.speedMps = req.speedMps;
        updateCmd.lengthM = req.lengthM;
        updateCmd.costOverrideSec = req.costOverrideSec;
        updateCmd.fromId = req.fromId;
        updateCmd.toId = req.toId;

        var factoryEdge = update.update(id, updateCmd);
        // gleiche Status-Ansicht zurückgeben:
        return ResponseEntity.ok(query.findOneWithStatus(factoryEdge.getId(), null));
    }

    // POST /api/factory/edges/{id}/toggle-block?blocked=true
    @PostMapping("/{id}/toggle-block")
    public ResponseEntity<FactoryEdgeDTO> toggleBlock(@PathVariable Long id, @RequestParam boolean blocked) {
        var factoryEdge = update.toggleBlocked(id, blocked);
        return ResponseEntity.ok(query.findOneWithStatus(factoryEdge.getId(), null));
    }

    // POST /api/factory/edges/{id}/set-bidirectional?value=true
    @PostMapping("/{id}/set-bidirectional")
    public ResponseEntity<FactoryEdgeDTO> setBidirectional(@PathVariable Long id, @RequestParam("value") boolean value) {
        var factoryEdge = update.setBidirectional(id, value);
        return ResponseEntity.ok(query.findOneWithStatus(factoryEdge.getId(), null));
    }

    // POST /api/factory/edges/{id}/reverse
    @PostMapping("/{id}/reverse")
    public ResponseEntity<FactoryEdgeDTO> reverse(@PathVariable Long id) {
        var factoryEdge = update.reverseDirection(id);
        return ResponseEntity.ok(query.findOneWithStatus(factoryEdge.getId(), null));
    }

    private Set<String> parseModes(String csv) {
        if (csv == null || csv.isBlank()) return Set.of();
        var strings = new HashSet<String>();
        for (String t : csv.split(",")) {
            String v = t.trim();
            if (!v.isEmpty()) strings.add(v);
        }
        return strings;
    }

}

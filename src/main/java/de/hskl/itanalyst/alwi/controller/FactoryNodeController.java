package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.CreateNodeRequest;
import de.hskl.itanalyst.alwi.dto.FactoryNodeResponse;
import de.hskl.itanalyst.alwi.services.FactoryNodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factory/nodes")
public class FactoryNodeController {

    private final FactoryNodeService nodeService;

    public FactoryNodeController(FactoryNodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping
    public ResponseEntity<FactoryNodeResponse> create(@RequestBody CreateNodeRequest req) {
        var factoryNode = nodeService.createFactoryNode(req.code(), req.name(), req.floor(), req.zone());
        return ResponseEntity.ok(FactoryNodeResponse.of(factoryNode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactoryNodeResponse> get(@PathVariable Long id) {
        var factoryNode = nodeService.getById(id);
        return ResponseEntity.ok(FactoryNodeResponse.of(factoryNode));
    }

    @GetMapping
    public ResponseEntity<FactoryNodeResponse> getByCode(@RequestParam("code") String code) {
        var factoryNode = nodeService.getByCode(code);
        return ResponseEntity.ok(FactoryNodeResponse.of(factoryNode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nodeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.services.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/nodes")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NodeDTO> addNode(@Valid @RequestBody NodeDTO node) {
        NodeDTO nodeDTO = nodeService.saveNode(node);
        return ResponseEntity.ok().body(nodeDTO);
    }

    @Operation(summary = "Requesting all saved nodes in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found nodes.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NodeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid attributes supplied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nothing found in database.", content = @Content)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<NodeDTO> nodes = nodeService.findAllNodes();
        return ResponseEntity.ok().body(nodes);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable Long id) {
        Optional<NodeDTO> node = nodeService.findNodeById(id);
        return node.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

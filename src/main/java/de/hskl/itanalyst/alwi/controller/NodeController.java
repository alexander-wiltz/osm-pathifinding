package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.services.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name = "Nodes")
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @Operation(summary = "All nodes from database.")
    @ApiResponse(responseCode = "200", description = "Found nodes.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<NodeDTO> nodeDTOs = nodeService.findAllNodes();
        return ResponseEntity.ok().body(nodeDTOs);
    }

    @Operation(summary = "Get node by id from database.")
    @ApiResponse(responseCode = "200", description = "Node found.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable Long id) throws NodeNotFoundException {
        Optional<NodeDTO> nodeDTO = nodeService.findNodeById(id);
        return nodeDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

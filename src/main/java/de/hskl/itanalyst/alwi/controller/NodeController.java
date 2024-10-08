package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.entities.Node;
import de.hskl.itanalyst.alwi.services.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
public class NodeController extends BaseController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NodeService nodeService;

    @Operation(summary = "All nodes from database.")
    @ApiResponse(responseCode = "200", description = "Found nodes.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<Node> nodes = nodeService.findAllNodes();
        return ResponseEntity.ok().body(nodes.stream().map(this::convertToDto).toList());
    }

    @Operation(summary = "Get node by id from database.")
    @ApiResponse(responseCode = "200", description = "Node found.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable Long id) {
        Optional<Node> node = nodeService.findNodeById(id);
        Optional<NodeDTO> nodeDTO = node.map(this::convertToDto);
        return nodeDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add node.")
    @ApiResponse(responseCode = "201", description = "Node added successfully.")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NodeDTO> addNode(@Valid @RequestBody NodeDTO nodeDTO) {
        Node node = convertToEntity(nodeDTO);
        Node nodeSaved = nodeService.saveNode(node);
        return ResponseEntity.ok().body(convertToDto(nodeSaved));
    }

    private NodeDTO convertToDto(Node node) {
        return modelMapper.map(node, NodeDTO.class);
    }

    private Node convertToEntity(NodeDTO nodeDTO) {
        return modelMapper.map(nodeDTO, Node.class);
    }
}

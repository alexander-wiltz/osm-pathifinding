package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.algorithm.AStar;
import de.hskl.itanalyst.alwi.algorithm.Graph;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.geomodel.GeoJsonObject;
import de.hskl.itanalyst.alwi.localstorage.LocalStorage;
import de.hskl.itanalyst.alwi.services.GeoJsonService;
import de.hskl.itanalyst.alwi.services.NodeService;
import de.hskl.itanalyst.alwi.services.StreetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Computing of requested ways")
@RequestMapping("/pathfinding")
public class PathfindingController extends BaseController {

    @Autowired
    private GeoJsonService geoJsonService;

    @Autowired
    private AStar aStarAlgorithm;

    @Autowired
    private LocalStorage localStorage;

    @Operation(summary = "Compute way and respond with GeoJson-Object.")
    @ApiResponse(responseCode = "200", description = "GeoJson Object successfully created.")
    @ApiResponse(responseCode = "404", description = "Way not computable.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoJsonObject> getComputedWay(
            @RequestParam(name = "stStr") String startStreet,
            @RequestParam(name = "stNo") String startNumber,
            @RequestParam(name = "tgStr") String targetStreet,
            @RequestParam(name = "tgNo") String targetNumber) throws StreetNotFoundException, NodeNotFoundException, WayNotComputableException {

        List<StreetDTO> startStreets = localStorage.findStreetByName(startStreet);
        List<StreetDTO> targetStreets = localStorage.findStreetByName(targetStreet);

        NodeDTO startNode = localStorage.getNodeOfStreetObject(startStreet, startNumber, startStreets);
        NodeDTO targetNode = localStorage.getNodeOfStreetObject(targetStreet, targetNumber, targetStreets);

        // TODO initial graph building
        List<StreetDTO> streets = localStorage.getGlobalStreetDTOs();
        List<NodeDTO> nodes = localStorage.getGlobalNodeDTOs();
        Graph<NodeDTO> graph = aStarAlgorithm.prepareGraph(streets, nodes);

        List<NodeDTO> route = aStarAlgorithm.findRoute(graph, startNode.getId(), targetNode.getId());
        GeoJsonObject geoJsonObject = geoJsonService.createGeoJsonObject(startNode, targetNode, route);

        return ResponseEntity.ok(geoJsonObject);
    }

}

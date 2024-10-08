package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.algorithm.AStar;
import de.hskl.itanalyst.alwi.algorithm.Graph;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.geomodel.GeoJsonObject;
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
    private StreetService streetService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private GeoJsonService geoJsonService;

    @Autowired
    private AStar aStarAlgorithm;

    @Operation(summary = "Compute way and respond with GeoJson-Object.")
    @ApiResponse(responseCode = "200", description = "GeoJson Object successfully created.")
    @ApiResponse(responseCode = "404", description = "Way not computable.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoJsonObject> getComputedWay(
            @RequestParam(name = "stStr") String startStreet,
            @RequestParam(name = "stNo") String startNumber,
            @RequestParam(name = "tgStr") String targetStreet,
            @RequestParam(name = "tgNo") String targetNumber) throws StreetNotFoundException, NodeNotFoundException, WayNotComputableException {

        List<StreetDTO> startStreets = streetService.findStreets(startStreet);
        List<StreetDTO> targetStreets = streetService.findStreets(targetStreet);

        NodeDTO startNode = streetService.getNodeOfStreetObject(startStreet, startNumber, startStreets);
        NodeDTO targetNode = streetService.getNodeOfStreetObject(targetStreet, targetNumber, targetStreets);

        List<StreetDTO> streetDTOs = streetService.findAllStreets();
        List<NodeDTO> nodeDTOs = nodeService.findAllNodes();

        // TODO
        // Es werden zum Erstellen des Graphen immer alle Nodes und alle Straßen benötigt.
        // Sollte man die Abfrage beim Starten des Webservice einmalig machen und die Informationen im Cahce mitführen?
        // Die Suche der Adresse kann dann gegen die Daten die sich im Heap befinden erfolgen → PERFORMANCE GEWINN
        // LocalStorage Class mit Street Liste und Node Liste ?

        Graph<NodeDTO> graph = aStarAlgorithm.prepareGraph(streetDTOs, nodeDTOs);
        List<NodeDTO> route = aStarAlgorithm.findRoute(graph, startNode.getId(), targetNode.getId());

        GeoJsonObject geoJsonObject = geoJsonService.createGeoJsonObject(startNode, targetNode, route);

        return ResponseEntity.ok(geoJsonObject);
    }

}

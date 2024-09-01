package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.algorithm.AStar;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.geomodel.GeoJsonObject;
import de.hskl.itanalyst.alwi.services.GeoJsonService;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Computing of requested ways")
@RequestMapping("/pathfinding")
public class PathfindingController extends BaseController {

    @Autowired
    private StreetService streetService;

    @Autowired
    private GeoJsonService geoJsonService;

    @Operation(summary = "Compute way and respond with GeoJson-Object.")
    @ApiResponse(responseCode = "200", description = "GeoJson Object successfully created.")
    @ApiResponse(responseCode = "404", description = "Way not computable.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoJsonObject> getComputedWay(
            @RequestParam(name = "stStr") String startStreet,
            @RequestParam(name = "stNo") String startNumber,
            @RequestParam(name = "tgStr") String targetStreet,
            @RequestParam(name = "tgNo") String targetNumber) throws StreetNotFoundException, NodeNotFoundException, WayNotComputableException {

        List<StreetDTO> startStreets = streetService.findByStreet(startStreet);
        if (startStreets == null || startStreets.isEmpty()) {
            String errMsg = String.format("Street %s not found.", startStreet);
            log.error(errMsg);
            throw new StreetNotFoundException(errMsg);
        }

        List<StreetDTO> targetStreets = streetService.findByStreet(targetStreet);
        if (targetStreets == null || targetStreets.isEmpty()) {
            String errMsg = String.format("Street %s not found.", targetStreet);
            log.error(errMsg);
            throw new StreetNotFoundException(errMsg);
        }

        NodeDTO startNode = getStreetObject(startStreet, startNumber, startStreets);
        NodeDTO targetNode = getStreetObject(targetStreet, targetNumber, targetStreets);

        List<NodeDTO> route = new ArrayList<>();
        AStar aStar = new AStar();
        List<NodeDTO> path = aStar.findPath(null, startNode, targetNode);

        // TODO compute Route!
        route.add(new NodeDTO(1L, 6.707762, 49.234920));
        route.add(new NodeDTO(2L, 6.706711, 49.234899));
        route.add(new NodeDTO(3L, 6.706695, 49.235384));
        route.add(new NodeDTO(4L, 6.705992, 49.235391));
        route.add(new NodeDTO(5L, 6.705496, 49.235382));
        route.add(new NodeDTO(6L, 6.704935, 49.235380));
        route.add(new NodeDTO(7L, 6.704672, 49.235386));
        route.add(new NodeDTO(8L, 6.704667, 49.235508));
        route.add(new NodeDTO(9L, 6.703940, 49.235515));
        route.add(new NodeDTO(10L, 6.703921, 49.235876));
        route.add(new NodeDTO(11L, 6.703927, 49.235918));

        GeoJsonObject geoJsonObject = geoJsonService.createGeoJsonObject(startNode, targetNode, route);

        return ResponseEntity.ok(geoJsonObject);
    }

    /**
     * Taking a house from an address
     * @param street street
     * @param number housenumber
     * @param streets list of streets
     * @return NodeDTO
     * @throws NodeNotFoundException exception
     */
    private NodeDTO getStreetObject(String street, String number, List<StreetDTO> streets) throws NodeNotFoundException {
        StreetDTO address = streets.stream().filter(s -> s.getHousenumber() != null && s.getHousenumber().equals(number)).findFirst().orElse(null);
        NodeDTO node;
        if (address != null) {
            node = address.getNodes().stream().toList().get(0);
        } else {
            String errMsg = String.format("No match for number: %s in street: %s.", number, street);
            log.error(errMsg);
            throw new NodeNotFoundException(errMsg);
        }

        return node;
    }

}

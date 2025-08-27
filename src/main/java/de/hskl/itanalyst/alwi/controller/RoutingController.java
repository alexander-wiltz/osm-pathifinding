package de.hskl.itanalyst.alwi.controller;

import de.hskl.itanalyst.alwi.entities.FactoryNode;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.services.RoutingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/factory/route")
public class RoutingController {

    private final RoutingService routingService;

    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }

    /**
     * Beispiel-Body: { "from":"F089.4", "to":"H101.2", "modes":["ANY","AGV"] }
     */
    public static record RouteRequest(String from, String to, Set<String> modes) {
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> route(@RequestBody RouteRequest req) throws WayNotComputableException {
        List<FactoryNode> path = routingService.routeByCodes(req.from(), req.to(), req.modes());
        return ResponseEntity.ok(routingService.routeAsGeoJson(path));
    }

}

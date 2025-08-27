package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.algorithm.FactoryGraphBuilder;
import de.hskl.itanalyst.alwi.algorithm.PlanarEdgeCostScorer;
import de.hskl.itanalyst.alwi.algorithm.PlanarHeuristicScorer;
import de.hskl.itanalyst.alwi.algorithm.RouteFinder;
import de.hskl.itanalyst.alwi.entities.FactoryEdge;
import de.hskl.itanalyst.alwi.entities.FactoryNode;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.repositories.FactoryEdgeRepository;
import de.hskl.itanalyst.alwi.repositories.FactoryNodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
/**
 * RoutingService baut den Graphen + Gewichte, verdrahtet die Scorer und liefert Route bzw. GeoJSON.
 */
public class RoutingService {

    private final FactoryNodeRepository factoryNodeRepository;
    private final FactoryEdgeRepository factoryEdgeRepository;

    /**
     * Defaults (anpassbar über Config)
     */
    private final double defaultEdgeSpeedMps = 1.8;   // Standard-Kantengeschwindigkeit
    private final double heuristicMaxSpeedMps = 2.2;  // Obergrenze für zulässige Heuristik
    private final boolean useManhattan = false;

    public List<FactoryNode> routeByCodes(String fromCode, String toCode, Set<String> modes) throws WayNotComputableException {
        FactoryNode from = factoryNodeRepository.findByCode(fromCode.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Start unbekannt: " + fromCode));
        FactoryNode to = factoryNodeRepository.findByCode(toCode.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Ziel unbekannt: " + toCode));
        return route(from, to, modes);
    }

    public List<FactoryNode> route(FactoryNode from, FactoryNode to, Set<String> modes) throws WayNotComputableException {
        Collection<FactoryNode> nodes = factoryNodeRepository.findAll();
        Collection<FactoryEdge> edges = factoryEdgeRepository.findAll();

        FactoryGraphBuilder gBuilder = new FactoryGraphBuilder(defaultEdgeSpeedMps,
                (modes == null || modes.isEmpty()) ? Set.of("ANY") : modes);
        var res = gBuilder.build(nodes, edges);

        var nextScorer = new PlanarEdgeCostScorer<FactoryNode>(res.edgeSeconds);
        var targetScorer = new PlanarHeuristicScorer<FactoryNode>(heuristicMaxSpeedMps, useManhattan);

        var rf = new RouteFinder<FactoryNode>(res.graph, nextScorer, targetScorer);
        return rf.findRoute(from, to);
    }

    /**
     * Sehr schlanke GeoJSON-Ausgabe (LineString, lokales Meter-CRS).
     */
    public Map<String, Object> routeAsGeoJson(List<FactoryNode> path) {
        Map<String, Object> feature = new LinkedHashMap<>();
        feature.put("type", "Feature");
        Map<String, Object> geom = new LinkedHashMap<>();
        geom.put("type", "LineString");
        List<List<Double>> coords = new ArrayList<>();
        for (FactoryNode n : path) coords.add(List.of(n.getX(), n.getY()));
        geom.put("coordinates", coords);
        feature.put("geometry", geom);
        feature.put("properties", Map.of("count", path.size()));
        return Map.of("type", "FeatureCollection", "features", List.of(feature));
    }
}

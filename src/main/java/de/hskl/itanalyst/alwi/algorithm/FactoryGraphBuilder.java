package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;
import de.hskl.itanalyst.alwi.algorithm.PlanarEdgeCostScorer.EdgeKey;

import java.util.*;

import static java.lang.Math.hypot;

/**
 * Baut aus Fabrik-Nodes/Edges:
 * - Graph<T> (Adjazenz)
 * - Map<EdgeKey, seconds> (gerichtete Kantenkosten)
 * <br>
 * T muss INode & XY implementieren.
 * E muss EdgeLike<T> implementieren (siehe Interface unten).
 */
public class FactoryGraphBuilder<T extends INode & XY, E> {

    public interface EdgeLike<T> {
        T getFrom();
        T getTo();
        boolean isBidirectional();
        boolean isBlocked();
        Double getLengthM();       // optional (null → aus (x,y) berechnen)
        Double getSpeedMps();      // optional (null → defaultSpeedMps)
        Double getCostOverride();  // optional (null → length/speed)
        String getAllowedModes();  // optional, z.B. "ANY,AGV"
    }

    public static final class Result<T extends INode> {

        public final Graph<T> graph;
        public final Map<PlanarEdgeCostScorer.EdgeKey, Double> edgeSeconds;

        public Result(Graph<T> graph, Map<PlanarEdgeCostScorer.EdgeKey, Double> edgeSeconds) {
            this.graph = graph;
            this.edgeSeconds = edgeSeconds;
        }
    }

    private final double defaultSpeedMps;
    private final Set<String> allowedModesForRun;

    public FactoryGraphBuilder(double defaultSpeedMps, Set<String> allowedModesForRun) {
        if (defaultSpeedMps <= 0) throw new IllegalArgumentException("defaultSpeedMps must be > 0");
        this.defaultSpeedMps = defaultSpeedMps;
        this.allowedModesForRun = (allowedModesForRun == null || allowedModesForRun.isEmpty())
                ? new HashSet<>(List.of("ANY"))
                : new HashSet<>(allowedModesForRun);
    }

    public Result<T> build(Collection<T> nodes, Collection<? extends EdgeLike<T>> edges) {
        Set<T> nodeSet = new HashSet<>(nodes);

        // Adjazenz Matrix und Kantengewichte
        Map<Long, Set<Long>> adjacencyList = new HashMap<>();
        for (T node : nodes) adjacencyList.put(node.getId(), new HashSet<>());

        Map<EdgeKey, Double> weight = new HashMap<>();
        for (EdgeLike<T> edge : edges) {
            if (edge == null || edge.isBlocked()) continue;
            if (!isModeAllowed(edge.getAllowedModes())) continue;

            T a = edge.getFrom();
            T b = edge.getTo();
            if (a == null || b == null) continue;

            addDirected(adjacencyList, weight, a, b, edge);
            if (edge.isBidirectional()) addDirected(adjacencyList, weight, b, a, edge);
        }

        Graph<T> graph = new Graph<>(nodeSet, adjacencyList);
        return new Result<>(graph, weight);
    }

    private boolean isModeAllowed(String allowed) {
        if (allowed == null || allowed.trim().isEmpty()) return true;
        for (String token : allowed.split(",")) {
            if (allowedModesForRun.contains(token.trim())) return true;
        }
        return false;
    }

    private void addDirected(Map<Long, Set<Long>> adjacency, Map<EdgeKey, Double> weight, T from, T to, EdgeLike<T> edge) {
        adjacency.get(from.getId()).add(to.getId());

        // Länge bestimmen
        double lengthM = (edge.getLengthM() != null) ? edge.getLengthM() : hypot(((XY) from).getX() - ((XY) to).getX(), ((XY) from).getY() - ((XY) to).getY());

        // Geschwindigkeit bestimmen
        double speedMps = (edge.getSpeedMps() != null && edge.getSpeedMps() > 0) ? edge.getSpeedMps() : defaultSpeedMps;

        // Kosten in Sekunden berechnen
        double sec = (edge.getCostOverride() != null) ? edge.getCostOverride() : (lengthM / speedMps);

        weight.put(new EdgeKey(from.getId(), to.getId()), sec);
    }
}

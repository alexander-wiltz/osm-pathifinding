package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.algorithm.FactoryGraphBuilder.Result;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collection;

public class AStarFactory<T extends INode & XY, E extends FactoryGraphBuilder.EdgeLike<T>> {

    private final double defaultSpeedMps;       // Kanten-Default (falls Edge.speed null)
    private final double heuristicMaxSpeedMps;  // für zulässige Heuristik
    private final boolean heuristicUseManhattan;
    private final Set<String> allowedModes;

    public AStarFactory(double defaultSpeedMps, double heuristicMaxSpeedMps, boolean heuristicUseManhattan, Set<String> allowedModes) {
        this.defaultSpeedMps = defaultSpeedMps;
        this.heuristicMaxSpeedMps = heuristicMaxSpeedMps;
        this.heuristicUseManhattan = heuristicUseManhattan;
        this.allowedModes = (allowedModes == null || allowedModes.isEmpty()) ? new HashSet<>(List.of("ANY")) : new HashSet<>(allowedModes);
    }

    /**
     * End-to-End Routing: Nodes/Edges → Graph+Gewichte → RouteFinder.
     */
    public List<T> findRoute(Collection<T> nodes, Collection<E> edges, T from, T to) throws WayNotComputableException {
        FactoryGraphBuilder<T, E> builder = new FactoryGraphBuilder<>(defaultSpeedMps, allowedModes);
        Result<T> result = builder.build(nodes, edges);

        PlanarEdgeCostScorer<T> nextScorer = new PlanarEdgeCostScorer<>(result.edgeSeconds);
        PlanarHeuristicScorer<T> targetScorer = new PlanarHeuristicScorer<>(heuristicMaxSpeedMps, heuristicUseManhattan);

        RouteFinder<T> routeFinder = new RouteFinder<>(result.graph, nextScorer, targetScorer);
        return routeFinder.findRoute(from, to);
    }
}

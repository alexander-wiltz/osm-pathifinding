package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.IGraphNode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.IScorer;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class RouteFinder<T extends IGraphNode> {

    private final Graph<T> graph;
    private final IScorer<T> nextScorer;
    private final IScorer<T> targetScorer;

    public List<T> findRoute(T from, T to) throws WayNotComputableException {
        Map<T, RouteNode<T>> allNodes = new HashMap<>();
        Queue<RouteNode> openSet = new PriorityQueue<>();

        RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeDistance(from, to));
        allNodes.put(from, start);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            log.debug("Open Set contains: {}", openSet.stream().map(RouteNode::getCurrent).collect(Collectors.toSet()));
            RouteNode<T> next = openSet.poll();
            log.debug("Looking at node: {}", next);
            if (next.getCurrent().equals(to)) {
                log.debug("Found destination!");
                List<T> route = new ArrayList<>();
                RouteNode<T> current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPredecessor());
                } while (current != null);

                log.debug("Route: {}", route);
                return route;
            }

            graph.getConnections(next.getCurrent()).forEach(connection -> {
                RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
                allNodes.put(connection, nextNode);

                double newScore = next.getRouteScore() + nextScorer.computeDistance(next.getCurrent(), connection);
                if (nextNode.getRouteScore() > newScore) {
                    nextNode.setPredecessor(next.getCurrent());
                    nextNode.setRouteScore(newScore);
                    nextNode.setEstimatedScore(newScore + targetScorer.computeDistance(connection, to));
                    openSet.add(nextNode);
                    log.debug("Found better route to node: {}", nextNode);
                }
            });
        }

        throw new WayNotComputableException("No route found.");
    }
}

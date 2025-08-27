package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.IScorer;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class RouteFinder<T extends INode> {

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
            if (log.isDebugEnabled()) {
                log.debug("Open Set contains: {}", openSet.stream().map(RouteNode::getCurrent).collect(Collectors.toSet()));
            }

            RouteNode<T> next = openSet.poll();
            if (log.isDebugEnabled()) log.debug("Looking at node: {}", next);

            if (next.getCurrent().equals(to)) {
                if (log.isDebugEnabled()) log.debug("Found destination!");
                List<T> route = new ArrayList<>();
                RouteNode<T> current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPredecessor());
                } while (current != null);

                log.info("Route: {}", route);
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
                    if (log.isDebugEnabled()) log.debug("Found better route to node: {}", nextNode);
                }
            });
        }

        throw new WayNotComputableException("No route found.");
    }

    public List<T> findFactoryRoute(T from, T to) {
        Map<Long, RouteFactoryNode<T>> all = new HashMap<>();
        PriorityQueue<RouteFactoryNode<T>> open = new PriorityQueue<>(Comparator.comparingDouble(RouteFactoryNode::getEstimatedScore));

        RouteFactoryNode<T> start = getOrCreate(all, from, null, 0.0, targetScorer.computeDistance(from, to));
        start.setVisited(true);
        open.add(start);

        while (!open.isEmpty()) {
            RouteFactoryNode<T> next = open.poll();
            if (next.getCurrent().getId().equals(to.getId())) {
                return unwind(next);
            }

            graph.getConnections(next.getCurrent().getId()).forEach(connId -> {
                T conn = graph.getNodes().stream().filter(n -> n.getId().equals(connId)).findFirst().orElse(null);
                if (conn == null) return;

                double newScore = next.getRouteScore() + nextScorer.computeDistance(next.getCurrent(), conn);
                RouteFactoryNode<T> nextNode = getOrCreate(all, conn, next, newScore,
                        newScore + targetScorer.computeDistance(conn, to));

                if (!nextNode.isVisited()) {
                    nextNode.setVisited(true);
                    open.add(nextNode);
                }
            });
        }
        return List.of(); // kein Pfad
    }

    private RouteFactoryNode<T> getOrCreate(Map<Long, RouteFactoryNode<T>> all, T current, RouteFactoryNode<T> previous,
                                            double routeScore, double estimatedScore) {
        return all.compute(current.getId(), (id, rn) -> {
            if (rn == null || routeScore < rn.getRouteScore()) {
                rn = new RouteFactoryNode<>(current);
                rn.setPrevious(previous);
                rn.setRouteScore(routeScore);
                rn.setEstimatedScore(estimatedScore);
            }
            return rn;
        });
    }

    private List<T> unwind(RouteFactoryNode<T> n) {
        LinkedList<T> route = new LinkedList<>();
        RouteFactoryNode<T> cur = n;
        while (cur != null) {
            route.addFirst(cur.getCurrent());
            cur = cur.getPrevious();
        }
        return route;
    }
}

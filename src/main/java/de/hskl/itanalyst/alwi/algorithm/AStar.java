package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.dto.NodeDTO;

import java.util.*;

public class AStar {
    public List<NodeDTO> findPath(Graph graph, NodeDTO start, NodeDTO goal) {
        Map<NodeDTO, NodeDTO> cameFrom = new HashMap<>(); // um den Pfad zurückzuverfolgen
        Map<NodeDTO, Double> gScore = new HashMap<>(); // Kosten vom Startknoten bis zu einem bestimmten Knoten
        Map<NodeDTO, Double> fScore = new HashMap<>(); // gScore + Heuristik (Schätzung der Restkosten)

        // PriorityQueue für die offenen Knoten basierend auf der Kostensumme (f(n))
        PriorityQueue<NodeDTO> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> fScore.getOrDefault(n, Double.POSITIVE_INFINITY)));

        for (NodeDTO nodeDTO : graph.getNodes()) {
            gScore.put(nodeDTO, Double.POSITIVE_INFINITY);
            fScore.put(nodeDTO, Double.POSITIVE_INFINITY);
        }

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        openSet.add(start);

        while (!openSet.isEmpty()) {
            NodeDTO current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            for (NodeDTO neighbor : graph.getNeighbors(current)) {
                double tentativeGScore = gScore.get(current) + graph.getDistance(current, neighbor);

                if (tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // falls kein Pfad gefunden wurde
    }

    private List<NodeDTO> reconstructPath(Map<NodeDTO, NodeDTO> cameFrom, NodeDTO current) {
        List<NodeDTO> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private double heuristic(NodeDTO nodeDTO1, NodeDTO nodeDTO2) {
        return Heuristics.distanceTo(nodeDTO1, nodeDTO2);
    }
}

package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.dto.NodeDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Graph {

    private final Map<NodeDTO, Map<NodeDTO, Double>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addNode(NodeDTO nodeDTO) {
        adjacencyList.putIfAbsent(nodeDTO, new HashMap<>());
    }

    public void addEdge(NodeDTO nodeDTO1, NodeDTO nodeDTO2, double distance) {
        adjacencyList.get(nodeDTO1).put(nodeDTO2, distance);
        adjacencyList.get(nodeDTO2).put(nodeDTO1, distance);
    }

    public Set<NodeDTO> getNeighbors(NodeDTO nodeDTO) {
        return adjacencyList.get(nodeDTO).keySet();
    }

    public double getDistance(NodeDTO nodeDTO1, NodeDTO nodeDTO2) {
        return adjacencyList.get(nodeDTO1).get(nodeDTO2);
    }

    public Set<NodeDTO> getNodes() {
        return adjacencyList.keySet();
    }
}

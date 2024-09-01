package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.algorithm.Graph;
import de.hskl.itanalyst.alwi.algorithm.Heuristics;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GraphBuilderService {

     @Autowired
     private StreetService streetService;

    public Graph buildGraph() {
        List<StreetDTO> streets = streetService.findAllStreets();
        Graph graph = new Graph();

        for(StreetDTO street: streets) {
            List<NodeDTO> streetNodes = street.getNodes();
            for(NodeDTO node: streetNodes) {
                graph.addNode(node);
            }

            // Verbinde die Knoten der Straße untereinander (als Straßenverbindungen)
            for (int i = 0; i < streetNodes.size() - 1; i++) {
                NodeDTO node1 = streetNodes.get(i);
                NodeDTO node2 = streetNodes.get(i + 1);
                double distance = Heuristics.distanceTo(node1, node2);
                graph.addEdge(node1, node2, distance);
            }

            // Füge alle Gebäude der Straße hinzu und verbinde sie mit dem Straßennetz
            for (BuildingDTO building : street.getBuildings()) {
                NodeDTO buildingNode = building.getTargetNode();
                NodeDTO closestStreetNode = findClosestNode(buildingNode, streetNodes);

                graph.addNode(buildingNode);
                double distance = Heuristics.distanceTo(buildingNode, closestStreetNode);
                graph.addEdge(buildingNode, closestStreetNode, distance);
            }
        }

        return graph;
    }

    /**
     * Hilfsmethode zur Bestimmung des nächsten Knotens auf der Straße für ein Gebäude
     */
    private NodeDTO findClosestNode(NodeDTO targetNode, List<NodeDTO> streetNodes) {
        NodeDTO closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (NodeDTO streetNode : streetNodes) {
            double distance = Heuristics.distanceTo(targetNode, streetNode);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = streetNode;
            }
        }

        return closestNode;
    }
}

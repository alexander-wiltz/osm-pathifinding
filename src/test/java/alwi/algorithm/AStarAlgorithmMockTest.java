package alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.AStar;
import de.hskl.itanalyst.alwi.algorithm.Graph;
import de.hskl.itanalyst.alwi.algorithm.Heuristics;
import de.hskl.itanalyst.alwi.dto.NodeDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AStarAlgorithmMockTest {

    @Test
    public void test() {
        Graph graph = new Graph();

        NodeDTO nodeDTOA = new NodeDTO(1L, 52.5200, 13.4050); // Berlin
        NodeDTO nodeDTOB = new NodeDTO(2L, 48.8566, 2.3522);  // Paris
        NodeDTO nodeDTOC = new NodeDTO(3L, 50.1109, 8.6821);  // Frankfurt
        NodeDTO nodeDTOD = new NodeDTO(4L, 51.1657, 10.4515); // Somewhere in Germany

        graph.addNode(nodeDTOA);
        graph.addNode(nodeDTOB);
        graph.addNode(nodeDTOC);
        graph.addNode(nodeDTOD);

        // Kanten mit Distanzen hinzufügen
        graph.addEdge(nodeDTOA, nodeDTOC, Heuristics.distanceTo(nodeDTOA, nodeDTOC));
        graph.addEdge(nodeDTOC, nodeDTOB, Heuristics.distanceTo(nodeDTOC, nodeDTOB));
        graph.addEdge(nodeDTOA, nodeDTOD, Heuristics.distanceTo(nodeDTOA, nodeDTOD));
        graph.addEdge(nodeDTOD, nodeDTOB, Heuristics.distanceTo(nodeDTOD, nodeDTOB));

        AStar aStar = new AStar();
        List<NodeDTO> path = aStar.findPath(graph, nodeDTOA, nodeDTOB);

        System.out.println("Path:");
        for (NodeDTO nodeDTO : path) {
            System.out.println("Node ID: " + nodeDTO.getId());
        }
    }
}

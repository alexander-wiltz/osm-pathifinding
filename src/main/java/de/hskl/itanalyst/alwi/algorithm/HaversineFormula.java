package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.IScorer;
import de.hskl.itanalyst.alwi.dto.NodeDTO;

import java.util.List;

public class HaversineFormula implements IScorer<NodeDTO> {

    @Override
    public double computeDistance(NodeDTO start, NodeDTO target) {
        double R = 6372.8;
        double dLat = Math.toRadians(target.getLatitude() - start.getLatitude());
        double dLon = Math.toRadians(target.getLongitude() - start.getLongitude());
        double lat1 = Math.toRadians(start.getLatitude());
        double lat2 = Math.toRadians(target.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    /**
     * Find the closest Node from a building to the street
     *
     * @param streetNodes
     * @param targetNode
     * @return Closest Node
     */
    @Override
    public NodeDTO findClosestNode(NodeDTO targetNode, List<NodeDTO> streetNodes) {
        NodeDTO closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (NodeDTO streetNode : streetNodes) {
            double distance = computeDistance(targetNode, streetNode);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = streetNode;
            }
        }

        return closestNode;
    }
}

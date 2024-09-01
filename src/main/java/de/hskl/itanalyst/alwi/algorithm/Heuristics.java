package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.dto.NodeDTO;

public class Heuristics {

    public static double distanceTo(NodeDTO source, NodeDTO other) {
        final int R = 6371;

        double latDistance = Math.toRadians(other.getLatitude() - source.getLatitude());
        double lonDistance = Math.toRadians(other.getLongitude() - source.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(source.getLatitude())) * Math.cos(Math.toRadians(other.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

package de.hskl.itanalyst.alwi.geomodel;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class GeoJsonComputedWayCreator {

    private Double[] coordinatesStart;
    private Double[] coordinatesTarget;
    private Double[][] coordinatesComputed;

    public GeoJsonObject createGeoJsonObject() throws WayNotComputableException {
        if (coordinatesStart == null) {
            log.error("Start coordinates not set.");
            throw new WayNotComputableException("Start coordinates not set.");
        }
        if (coordinatesTarget == null) {
            log.error("Target coordinates not set.");
            throw new WayNotComputableException("Target coordinates not set.");
        }
        if (coordinatesComputed == null) {
            log.error("Computed coordinates not set.");
            throw new WayNotComputableException("Computed coordinates not set.");
        }

        GeoJsonFeature geoJsonFeatureStart = new GeoJsonFeature(
                new GeoJsonProperties(
                        new GeoJsonUmapOptions(
                                "#00FF00",
                                "Drop",
                                "./img/wandering_640.png",
                                null),
                        "Start"),
                new GeoJsonGeometry(
                        "Point",
                        coordinatesStart)
        );
        GeoJsonFeature geoJsonFeatureZiel = new GeoJsonFeature(
                new GeoJsonProperties(
                        new GeoJsonUmapOptions(
                                "#FF0000",
                                "Drop",
                                "/img/pictogram/embassy-24_3W8iGQL.png",
                                null),
                        "Ziel"),
                new GeoJsonGeometry(
                        "Point",
                        coordinatesTarget)
        );

        GeoJsonFeature geoJsonFeatureWay = new GeoJsonFeature(
                new GeoJsonProperties(
                        new GeoJsonUmapOptions(),
                        "Route"),
                new GeoJsonGeometry(
                        "LineString",
                        coordinatesComputed)
        );

        List<GeoJsonFeature> geoJsonFeatures = new ArrayList<>();
        geoJsonFeatures.add(geoJsonFeatureStart);
        geoJsonFeatures.add(geoJsonFeatureZiel);
        geoJsonFeatures.add(geoJsonFeatureWay);

        log.debug("GeoJsonObject creation successfully.");
        return new GeoJsonObject(geoJsonFeatures);
    }

    public void setNodeDtoAsStartPoint(NodeDTO startNode) {
        this.coordinatesStart = new Double[]{startNode.getLongitude(), startNode.getLatitude()};
    }

    public void setNodeDtoAsTargetPoint(NodeDTO targetNode) {
        this.coordinatesTarget = new Double[]{targetNode.getLongitude(), targetNode.getLatitude()};
    }

    public void setNodeDtoListAsComputedWay(List<NodeDTO> computedNodes) {
        List<Double[]> computedCoordinates = new ArrayList<>();
        for (NodeDTO node : computedNodes) {
            Double[] coords = new Double[]{node.getLongitude(), node.getLatitude()};
            computedCoordinates.add(coords);
        }

        Double[][] coords = new Double[][]{};
        this.coordinatesComputed = computedCoordinates.toArray(coords);
    }
}

package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import de.hskl.itanalyst.alwi.geomodel.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GeoJsonService {

    public GeoJsonObject createGeoJsonObject(@NonNull NodeDTO startNode, @NonNull NodeDTO targetNode, @NonNull List<NodeDTO> computedNodes) throws WayNotComputableException {
        if (computedNodes.isEmpty()) {
            log.error("Computed coordinates are empty.");
            throw new WayNotComputableException("Computed coordinates are empty.");
        }

        List<Double[]> computedCoordinates = new ArrayList<>();
        for (NodeDTO node : computedNodes) {
            Double[] coords = new Double[]{node.getLongitude(), node.getLatitude()};
            computedCoordinates.add(coords);
        }

        Double[][] coords = new Double[][]{};

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
                        new Double[]{startNode.getLongitude(), startNode.getLatitude()})
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
                        new Double[]{targetNode.getLongitude(), targetNode.getLatitude()})
        );

        GeoJsonFeature geoJsonFeatureWay = new GeoJsonFeature(
                new GeoJsonProperties(
                        new GeoJsonUmapOptions(),
                        "Route"),
                new GeoJsonGeometry(
                        "LineString",
                        computedCoordinates.toArray(coords))
        );

        List<GeoJsonFeature> geoJsonFeatures = new ArrayList<>();
        geoJsonFeatures.add(geoJsonFeatureStart);
        geoJsonFeatures.add(geoJsonFeatureZiel);
        geoJsonFeatures.add(geoJsonFeatureWay);

        if (log.isDebugEnabled()) {
            log.debug("GeoJsonObject created successfully.");
        }
        return new GeoJsonObject(geoJsonFeatures);
    }
}

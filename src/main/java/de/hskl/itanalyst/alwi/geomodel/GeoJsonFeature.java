package de.hskl.itanalyst.alwi.geomodel;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Just for Simple Geometries
 *
 * @author Alexander Wiltz
 * @version 0.1.0
 */
@Getter
@Setter
public class GeoJsonFeature {
    private String type;
    private GeoJsonProperties properties;
    private GeoJsonGeometry geometry;

    /**
     * LineString do not need properties.
     *
     * @param geometry geometry object
     */
    public GeoJsonFeature(@NonNull GeoJsonGeometry geometry) {
        this.type = "Feature";
        this.properties = new GeoJsonProperties();
        this.geometry = geometry;
    }

    /**
     * Start and End-points need properties, e.g. for flags, colors, symbols
     *
     * @param properties property object
     * @param geometry geometry object
     */
    public GeoJsonFeature(GeoJsonProperties properties, @NonNull GeoJsonGeometry geometry) {
        this.type = "Feature";
        this.properties = properties;
        this.geometry = geometry;
    }
}

package de.hskl.itanalyst.alwi.geomodel;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * <a href="https://geojson.org/">https://geojson.org/</a>
 * <a href="https://de.wikipedia.org/wiki/GeoJSON">https://de.wikipedia.org/wiki/GeoJSON</a>
 */
@Getter
@Setter
public class GeoJsonObject {
    private String type;
    private List<GeoJsonFeature> features;

    public GeoJsonObject(@NonNull List<GeoJsonFeature> features) {
        this.type = "FeatureCollection";
        this.features = features;
    }
}

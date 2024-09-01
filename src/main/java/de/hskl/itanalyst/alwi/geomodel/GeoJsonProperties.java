package de.hskl.itanalyst.alwi.geomodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoJsonProperties {
    private GeoJsonUmapOptions _umap_options;
    private String name;

    public GeoJsonProperties(GeoJsonUmapOptions _umap_options, String name) {
        this._umap_options = _umap_options;
        this.name = name;
    }
}

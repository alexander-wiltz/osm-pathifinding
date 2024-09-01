package de.hskl.itanalyst.alwi.geomodel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GeoJsonUmapOptions {
    private String color;
    private String iconClass;
    private String iconUrl;
    private String showLabel;
}

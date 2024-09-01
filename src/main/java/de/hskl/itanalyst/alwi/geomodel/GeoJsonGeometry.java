package de.hskl.itanalyst.alwi.geomodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeoJsonGeometry {
    private String type;
    private Object coordinates;

    /**
     * constructor for points
     * object just needs an array with a lat and lon, <br />
     * e.g. [6.707776, 49.234935]
     *
     * @param type        Point
     * @param coordinates Single dimensional Array (Double[])
     */
    public GeoJsonGeometry(String type, Double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    /**
     * constructor for line strings
     * object needs a multi-array with lats and lons, <br />
     * e.g. <br />
     * [<br />
     * [6.707762, 49.23492],<br />
     * [6.706711, 49.234899],<br />
     * [6.706695, 49.235384]<br />
     * ]<br />
     *
     * @param type        LineString
     * @param coordinates Multi dimensional Array (Double[][])
     */
    public GeoJsonGeometry(String type, Double[][] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    /**
     * constructor for polygons
     * object needs a three-dimensional multi-array with lats and lons, <br />
     * e.g. <br />
     * [ [<br />
     * [6.707762, 49.23492],<br />
     * [6.706711, 49.234899],<br />
     * [6.706695, 49.235384]<br />
     * ] ]<br />
     * <p>
     * Polygon with inner polygons example: <br />
     * [ [<br />
     * [6.707762, 49.23492],<br />
     * [6.706711, 49.234899]<br />
     * ], [<br />
     * [6.706695, 49.235384]<br />
     * ] ]<br />
     *
     * @param type        Polygon
     * @param coordinates Multi dimensional Array (Double[][][])
     */
    public GeoJsonGeometry(String type, Double[][][] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }
}

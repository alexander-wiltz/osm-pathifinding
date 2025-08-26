package de.hskl.itanalyst.alwi.algorithm;


import lombok.AllArgsConstructor;

/**
 * Mappt GridLabel ↔ metrische Koordinaten (Meter) im lokalen Koordinatensystem.
 * Damit erzwingst du die ungeraden Reihen, erzeugst sprechende Labels und bekommst deterministische (x,y).
 * In der UI kannst du Nodes über A-1, A-3, … schnell anlegen und verbinden.
 */
@AllArgsConstructor
public class GridMapper {

    private final double originX;
    private final double originY;
    private final double spacingX;
    private final double spacingY;

    public double toX(GridLabel l) {
        int colIndex = (l.col - 'A'); // 0-basiert
        return originX + colIndex * spacingX;
    }

    public double toY(GridLabel l) {
        int rowIdx = (l.rowOdd - 1) / 2; // 1,3,5 → 0,1,2
        return originY + rowIdx * spacingY;
    }

    public static final class Point {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{x=" + x + ", y=" + y + '}';
        }
    }
}

package de.hskl.itanalyst.alwi.algorithm;


import lombok.AllArgsConstructor;

import static de.hskl.itanalyst.alwi.algorithm.GridConstants.*;

/**
 * Mappt GridLabel ↔ metrische Koordinaten (Meter) im lokalen Koordinatensystem.
 * Damit erzwingst du die ungeraden Reihen, erzeugst sprechende Labels und bekommst deterministische (x,y).
 * In der UI kannst du Nodes über A-1, A-3, … schnell anlegen und verbinden.
 * GridCode parst F089.4 und erzwingt ungerade Reihen.
 * GridMapper rechnet daraus metrische Koordinaten: ohne Subindex → Ecke, mit Subindex → Mittelpunkt des 6er-Teilrasters.
 */
@AllArgsConstructor
public final class GridMapper {

    private final double originX; // Standard 0
    private final double originY; // Standard 0

    /**
     * Unten-links-Ecke des Hauptrechtecks (ohne Subindex).
     */
    public Point baseCorner(GridCode code) {
        double x = originX + code.colIndex() * CELL_WIDTH_M;
        double y = originY + code.rowIndex() * CELL_HEIGHT_M;
        return new Point(x, y);
    }

    /**
     * Mittelpunkt des Teilrechtecks (falls subIndex vorhanden),
     * sonst exakt die unten-links-Ecke des Hauptrechtecks.
     */
    public Point toXY(GridCode code) {
        Point base = baseCorner(code);
        if (code.subIndex == null) return base;

        int s = code.subIndex; // 1..6 (1..3 unten, 4..6 oben)
        int subRow = (s - 1) / GridConstants.SUB_COLS; // 0=unten, 1=oben
        int subCol = (s - 1) % GridConstants.SUB_COLS; // 0..2

        double cx = base.x + (subCol + 0.5) * SUBCELL_WIDTH_M;
        double cy = base.y + (subRow + 0.5) * SUBCELL_HEIGHT_M;
        return new Point(cx, cy);
    }

    public static final class Point {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}

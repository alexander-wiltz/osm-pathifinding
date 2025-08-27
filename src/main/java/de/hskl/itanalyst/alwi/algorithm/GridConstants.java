package de.hskl.itanalyst.alwi.algorithm;

public final class GridConstants {

    private GridConstants() {}
    public static final double CELL_WIDTH_M  = 15.0; // X: links→rechts
    public static final double CELL_HEIGHT_M = 20.0; // Y: unten→oben

    // 2 Reihen × 3 Spalten = 6 Teilrechtecke
    public static final int SUB_COLS = 3; // 1..3 unten → Spalten 0..2
    public static final int SUB_ROWS = 2; // 4..6 oben  → Zeilen 0..1

    public static final double SUBCELL_WIDTH_M  = CELL_WIDTH_M  / SUB_COLS; // 5 m
    public static final double SUBCELL_HEIGHT_M = CELL_HEIGHT_M / SUB_ROWS; // 10 m

}
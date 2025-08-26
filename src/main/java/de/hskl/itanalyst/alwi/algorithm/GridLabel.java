package de.hskl.itanalyst.alwi.algorithm;

/**
 * Repräsentiert ein Hallenraster-Label wie "D-11" (ungerade Reihe Pflicht).
 */
public final class GridLabel {

    public final char col;   // 'A'..'Z'
    public final int rowOdd; // 1,3,5,...

    public GridLabel(char col, int rowOdd) {
        this.col = Character.toUpperCase(col);
        if (rowOdd % 2 == 0) throw new IllegalArgumentException("Row must be odd");
        this.rowOdd = rowOdd;
    }

    @Override
    public String toString() {
        return col + "-" + rowOdd;
    }

    public static GridLabel parse(String label) {
        if (label == null) throw new IllegalArgumentException("label null");
        String s = label.trim().toUpperCase();
        String[] parts = s.split("[-_/ ]");
        if (parts.length != 2 || parts[0].length() != 1 || !Character.isLetter(parts[0].charAt(0)))
            throw new IllegalArgumentException("Expected <Letter>-<OddNumber>");
        int r = Integer.parseInt(parts[1]);
        return new GridLabel(parts[0].charAt(0), r);
    }
}

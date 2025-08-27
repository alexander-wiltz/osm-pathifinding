package de.hskl.itanalyst.alwi.algorithm;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Koordinaten-Parser für Codes wie "F089.4".
 * - Spalte: Buchstabe A..Z (A=0 → x=0 m)
 * - Reihe: ungerade Zahl mit führenden Nullen (01,03,05, ...), steigende Werte nach oben (A03 ist 20 m über A01)
 * - Optionaler Subindex 1..6: 1..3 unten (links→rechts), 4..6 oben (links→rechts)
 * <p>
 * Ohne Subindex → exakt die Ecke des Hauptrechtecks (unten links).
 * Mit Subindex → Mittelpunkt des Teilrechtecks.
 */
public final class GridCode {

    private static final Pattern CODE = Pattern.compile("^\\s*([A-Z])\\s*(\\d{2,3})(?:\\.(\\d))?\\s*$");

    public final char colLetter;   // 'A'..'Z'
    public final int rowOdd;       // 1,3,5,...
    public final Integer subIndex; // null oder 1..6

    private GridCode(char colLetter, int rowOdd, Integer subIndex) {
        this.colLetter = colLetter;
        this.rowOdd = rowOdd;
        this.subIndex = subIndex;
        if (rowOdd % 2 == 0) throw new IllegalArgumentException("Reihe muss ungerade sein (…01,03,05,…)!");
        if (subIndex != null && (subIndex < 1 || subIndex > 6))
            throw new IllegalArgumentException("Subindex muss 1..6 sein!");
    }

    public static GridCode parse(String code) {
        Objects.requireNonNull(code, "code");
        Matcher m = CODE.matcher(code.toUpperCase());
        if (!m.matches()) throw new IllegalArgumentException("Ungültiges Koordinatenformat. Erwartet z.B. F089.4");
        char col = m.group(1).charAt(0);
        int row = Integer.parseInt(m.group(2));
        Integer sub = (m.group(3) == null) ? null : Integer.valueOf(m.group(3));
        return new GridCode(col, row, sub);
    }

    /**
     * 0-basierter Spaltenindex: A→0, B→1, …
     */
    public int colIndex() {
        return colLetter - 'A';
    }

    /**
     * 0-basierter Zeilenindex (Hauptraster): 01→0, 03→1, 05→2, …
     */
    public int rowIndex() {
        return (rowOdd - 1) / 2;
    }
}

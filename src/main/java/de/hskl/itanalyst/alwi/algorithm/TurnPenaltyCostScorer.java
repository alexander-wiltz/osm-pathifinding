package de.hskl.itanalyst.alwi.algorithm;

import java.util.List;
import java.util.Map;

/**
 * Einfacher Aufschlag je Kante (z. B. fürs Anfahren/Anhalten).
 * Für echte Richtungswechsel-Penalties müsste der RouteFinder Kontext kennen.
 * Wenn du ohne Änderung am RouteFinder kleine Zusatzkosten je Kante brauchst (AGV-Handling usw.), ist das der schnellste Weg.
 * Richtungsabhängig ginge auch – bräuchte aber Anpassung am RouteFinder.
 */
public class TurnPenaltyCostScorer<T> extends PlanarEdgeCostScorer<T> {

    private final double perEdgePenaltySec;

    public TurnPenaltyCostScorer(Map<EdgeKey, Double> edgeSeconds, double perEdgePenaltySec) {
        super(edgeSeconds);
        this.perEdgePenaltySec = Math.max(0, perEdgePenaltySec);
    }

    @Override
    public double computeDistance(T from, T to) {
        return super.computeDistance(from, to) + perEdgePenaltySec;
    }

}

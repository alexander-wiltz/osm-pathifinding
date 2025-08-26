package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.IScorer;
import de.hskl.itanalyst.alwi.algorithm.interfaces.XY;

import java.util.List;

/**
 * Heuristik in Sekunden: (Luftlinie oder Manhattan) / maxSpeedMps.
 * T muss INode & XY implementieren.
 */
public class PlanarHeuristicScorer<T> implements IScorer<T> {

    private final double maxSpeedMps; // zB 2.0m/s für Stapler oä
    private final boolean useManhattan;

    public PlanarHeuristicScorer(double maxSpeedMps) {
        this(maxSpeedMps, false);
    }

    public PlanarHeuristicScorer(double maxSpeedMps, boolean useManhattan) {
        if (maxSpeedMps <= 0) throw new IllegalArgumentException("maxSpeedMps must be > 0");
        this.maxSpeedMps = maxSpeedMps;
        this.useManhattan = useManhattan;
    }

    @Override
    public double computeDistance(T from, T to) {
        XY a = (XY) from;
        XY b = (XY) to;
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double metric = useManhattan ? Math.abs(dx) + Math.abs(dy) : Math.hypot(dx, dy);
        return metric / maxSpeedMps; // Sekunden
    }

    @Override
    public T findClosestNode(T targetNode, List<T> candidates) {
        XY t = (XY) targetNode;
        T best = null;
        double bestD = Double.MAX_VALUE;

        for (T n : candidates) {
            XY p = (XY) n;
            double d = Math.hypot(p.getX() - t.getX(), p.getY() - t.getY());
            if (d < bestD) {
                bestD = d;
                best = n;
            }
        }
        return best;
    }
}

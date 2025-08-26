package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.algorithm.interfaces.IScorer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Liefert die Kantenkosten (Sekunden) aus einer gerichteten Edge-Map.
 * Der RouteFinder fragt über nextScorer.computeDistance(u, v) die Kantenkosten ab.
 * Hier liefert er die Zeit in Sekunden – vorbereitet in einer Map.
 */
public class PlanarEdgeCostScorer<T> implements IScorer<T> {

    /*
     * Key für gerichtete Kanten A->B.
     */
    public static final class EdgeKey {
        public final Long fromId;
        public final Long toId;

        public EdgeKey(Long fromId, Long toId) {
            this.fromId = fromId;
            this.toId = toId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EdgeKey)) return false;
            EdgeKey edgeKey = (EdgeKey) o;
            return Objects.equals(fromId, edgeKey.fromId) && Objects.equals(toId, edgeKey.toId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromId, toId);
        }

        @Override
        public String toString() {
            return "EdgeKey{" + fromId + "->" + toId + "}";
        }
    }

    private final Map<EdgeKey, Double> edgeSeconds;

    public PlanarEdgeCostScorer(Map<EdgeKey, Double> edgeSeconds) {
        this.edgeSeconds = Objects.requireNonNull(edgeSeconds, "edgeSeconds");
    }

    @Override
    public double computeDistance(T from, T to) {
        Long a = ((INode) from).getId();
        Long b = ((INode) to).getId();
        Double w = edgeSeconds.get(new EdgeKey(a, b));
        return (w != null) ? w : Double.POSITIVE_INFINITY;
    }

    @Override
    public T findClosestNode(T targetNode, List<T> ignored) {
        return targetNode;
    }
}

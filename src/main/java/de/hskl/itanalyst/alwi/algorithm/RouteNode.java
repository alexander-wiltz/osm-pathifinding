package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
public class RouteNode <T extends INode> implements Comparable<RouteNode> {

    private final T current;
    private T predecessor;
    private double routeScore;
    private double estimatedScore;

    public RouteNode(T current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public int compareTo(RouteNode other) {
        return Double.compare(this.estimatedScore, other.estimatedScore);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RouteNode.class.getSimpleName() + "[", "]")
                .add("current=" + current)
                .add("predecessor=" + predecessor)
                .add("routeScore=" + routeScore)
                .add("estimatedScore=" + estimatedScore)
                .toString();
    }
}

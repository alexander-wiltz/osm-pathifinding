package de.hskl.itanalyst.alwi.algorithm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class RouteFactoryNode<T> {

    private final T current;
    private RouteFactoryNode<T> previous;
    private double routeScore;
    private double estimatedScore;
    private boolean visited;

    RouteFactoryNode(T current) {
        this.current = current;
    }
}
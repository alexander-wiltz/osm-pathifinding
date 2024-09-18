package de.hskl.itanalyst.alwi.algorithm.interfaces;

import de.hskl.itanalyst.alwi.dto.NodeDTO;

import java.util.List;

public interface IScorer <T> {
    double computeDistance(T from, T to);
    T findClosestNode(T targetNode, List<T> streetNodes);
}

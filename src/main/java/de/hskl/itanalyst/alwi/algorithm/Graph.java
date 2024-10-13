package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.algorithm.interfaces.INode;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class Graph<T extends INode> {

    private final List<T> nodes;
    private final Map<Long, Set<Long>> connections;

    /**
     * Search node with nodeId
     *
     * @param id nodeId
     * @return generic node object
     */
    public T getNode(Long id) {
        try {
            return nodes.stream()
                    .filter(node -> node.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new NodeNotFoundException("Node not found."));
        } catch (NodeNotFoundException exception) {
            log.error(exception.getErrorMessage());
            throw new RuntimeException();
        }
    }

    /**
     * get connections of node
     *
     * @param node node object
     * @return list with generic node objects
     */
    public Set<T> getConnections(T node) {
        if(log.isDebugEnabled()) {
            log.debug("Looking for NodeId={}", node.getId());
        }
        return connections
                .get(node.getId())
                .stream()
                .map(this::getNode)
                .collect(Collectors.toSet());
    }
}

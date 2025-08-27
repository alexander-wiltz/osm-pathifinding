package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.entities.FactoryEdge;
import de.hskl.itanalyst.alwi.entities.FactoryNode;
import de.hskl.itanalyst.alwi.exceptions.WayNotComputableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Component
public class AStar {

    public List<NodeDTO> findRoute(Graph<NodeDTO> map, Long startNodeId, Long targetNodeId) throws WayNotComputableException {
        RouteFinder<NodeDTO> routeFinder = new RouteFinder<>(map, new HaversineFormula(), new HaversineFormula());
        return routeFinder.findRoute(map.getNode(startNodeId), map.getNode(targetNodeId));
    }

    @Cacheable(value = "graph")
    public Graph<NodeDTO> prepareGraph(List<StreetDTO> streets) {
        Map<Long, Set<Long>> connections = new HashMap<>();
        Set<NodeDTO> nodes = buildNodeContainer(streets);

        for (NodeDTO node : nodes) {
            Set<Long> nodeIds = new HashSet<>();
            for (StreetDTO street : streets) {
                // region Street is a building: Put all nodes from parent object and all other children in parent
                if (street.getIsBuilding()) {
                    Optional<NodeDTO> optionalNode = street.getNodes().stream().filter(nd -> nd.getId().equals(node.getId())).findFirst();
                    if (optionalNode.isPresent()) {
                        StreetDTO parent = street.getParent();
                        List<Long> nodesFromParent = parent.getNodes().stream().map(NodeDTO::getId).toList();
                        nodeIds.addAll(nodesFromParent);

                        for (StreetDTO child : parent.getChildren()) {
                            if (child.getId().equals(street.getId())) {
                                continue;
                            }
                            nodeIds.addAll(child.getNodes().stream().map(NodeDTO::getId).toList());
                        }

                        log.trace("Added {} elements for House={} {}", nodeIds.size(), street.getName(), street.getHouseNumber());
                    }
                }
                // endregion

                // region Street is not a building: Put all nodes to connections, which are connected
                if (!street.getIsBuilding()) {
                    Optional<NodeDTO> parentOptional = street.getNodes().stream().filter(nd -> nd.getId().equals(node.getId())).findFirst();
                    if (parentOptional.isPresent()) {
                        List<Long> ids = street.getNodes().stream().map(NodeDTO::getId).filter(id -> !id.equals(parentOptional.get().getId())).toList();
                        nodeIds.addAll(ids);

                        for (StreetDTO child : street.getChildren()) {
                            nodeIds.addAll(child.getNodes().stream().map(NodeDTO::getId).toList());
                        }
                        log.trace("Added {} elements for Street={}", nodeIds.size(), street.getName());
                    }
                }
                // endregion
            }

            connections.put(node.getId(), nodeIds);
        }

        return new Graph<>(nodes, connections);
    }

    /**
     * Neu (Fabrik)
     * Du nimmst FactoryNode & FactoryEdge und erzeugst Verbindungen nur aus factory_edge:
     * Wenn edge.isBlocked oder from.isBlocked/to.isBlocked → nicht verbinden.
     * Wenn bidirectional → beide Richtungen.
     *
     * @param nodes factory nodes
     * @param edges factory edges
     * @return graph
     */
    @Cacheable(value = "graph")
    public Graph<FactoryNode> buildFactoryGraph(Collection<FactoryNode> nodes, Collection<FactoryEdge> edges) {
        Map<Long, Set<Long>> connections = new HashMap<>();
        nodes.forEach(n -> connections.put(n.getId(), new HashSet<>()));

        for (FactoryEdge edge : edges) {
            if (edge.isBlocked() || edge.getFrom().isBlocked() || edge.getTo().isBlocked()) continue;
            connections.get(edge.getFrom().getId()).add(edge.getTo().getId());
            if (Boolean.TRUE.equals(edge.isBidirectional())) {
                connections.get(edge.getTo().getId()).add(edge.getFrom().getId());
            }
        }

        return new Graph<>(new HashSet<>(nodes), connections);
    }

    /**
     * Building Node containers from street objects
     *
     * @param streets street-objects
     * @return Set of used Nodes
     */
    @Cacheable(value = "nodeContainer")
    public Set<NodeDTO> buildNodeContainer(List<StreetDTO> streets) {
        Set<NodeDTO> nodeContainer = new HashSet<>();
        for (StreetDTO street : streets) {
            nodeContainer.addAll(street.getNodes());
        }

        if (log.isDebugEnabled()) {
            log.debug("Node-Container built successfully. Found {} nodes.", nodeContainer.size());
        }
        return nodeContainer;
    }
}

package de.hskl.itanalyst.alwi.algorithm;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
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
        /*
         TODO: Street Stack
         Wenn für jede Straße, deren Nodes bereits in den Connections sind, das Street-Objekt aus einem
         Stack abgeräumt werden würde, so würde dieses Street-Objekt bei den Folgeiterationen nicht mehr
         durchlaufen werden. Die Iterationsdauer sollte theoretisch logarithmisch abnehmen.

         Ansatz:
         1. Street-Objekt legt für jede Node eine Map an, deren Set leer ist.
         2. Zu der ersten Node müssten alle Nodes des Street-Objekts hinzugefügt werden.
         3. Für alle anderen, jeweils auch.
         4. Das Street-Objekt kann raus.
         5. Das nächste getroffene Objekt, bekommt ebenfalls alle Nodes hinzugefügt.
         6. Für alle weiteren wird angelegt, sofern nicht vorhanden, und gefüllt.
         7. Die bestehenden geprüft, ob es gemeinsame gibt und auch diese eingefügt.
         zu 4.
         */

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

                        log.trace("Added {} elements for House={} {}", nodeIds.size(), street.getStreet(), street.getHouseNumber());
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
                        log.trace("Added {} elements for Street={}", nodeIds.size(), street.getStreet());
                    }
                }
                // endregion
            }

            connections.put(node.getId(), nodeIds);
        }

        return new Graph<>(nodes, connections);
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

package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.osmmodel.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObjectHandlerService {

    /**
     * Prepare Street Objects
     * Create objects with relations to each other
     *
     * @param ways list of ways
     * @return sorted list of street objects
     */
    public List<StreetDTO> prepareStreetsFromWaysAndNodes(List<WayDTO> ways, List<NodeDTO> nodes) {
        LinkedList<StreetDTO> streets = new LinkedList<>();
        List<WayDTO> justWays = new ArrayList<>();
        List<WayDTO> justBuildings = new ArrayList<>();

        // Divide by way and building
        for (WayDTO way : ways) {
            if (way.getIsBuilding() != null && way.getIsGarage() != null && way.getName() != null
                    && !way.getIsBuilding() && !way.getIsGarage() && !way.getName().isEmpty()) {
                justWays.add(way);
            } else if (way.getIsBuilding() != null && way.getIsBuilding()) {
                justBuildings.add(way);
            }
        }

        // Add all the divided ways without the buildings
        for (WayDTO wayDTO : justWays) {
            StreetDTO streetDTO = new StreetDTO();
            streetDTO.setId(wayDTO.getId());
            streetDTO.setIsBuilding(wayDTO.getIsBuilding());
            streetDTO.setStreet(wayDTO.getName());
            streetDTO.setChildren(new ArrayList<>());
            streetDTO.setNodes(wayDTO.getNodes());

            streets.add(streetDTO);
        }
        log.debug("Built {} streets.", streets.size());

        for (WayDTO buildingFromWayObject : justBuildings) {
            if (!buildingFromWayObject.getIsBuilding()) {
                log.info("Accessed way object declared as building, but attribute is missing. Way={}", buildingFromWayObject.getId());
                continue;
            }
            if (buildingFromWayObject.getStreet() == null) {
                log.info("Accessed way object has no street-name, but is a building. Way={}", buildingFromWayObject.getId());
                continue;
            }
            if (buildingFromWayObject.getRefNode() == null) {
                log.info("Error in reference node. There should be something. Way={}", buildingFromWayObject.getId());
                continue;
            }

            NodeDTO node;
            Optional<NodeDTO> optionalNode = nodes.stream().filter(n -> n.getId().equals(buildingFromWayObject.getRefNode())).findFirst();
            if (optionalNode.isPresent()) {
                node = optionalNode.get();
            } else {
                log.error("Got Building, but node not found. Way={}", buildingFromWayObject.getId());
                continue;
            }

            Optional<StreetDTO> optionalStreetObject = streets.stream().filter(street -> !street.getIsBuilding() && street.getStreet().equals(buildingFromWayObject.getStreet())).findFirst();
            if (optionalStreetObject.isPresent()) {
                StreetDTO building = getStreetDTO(buildingFromWayObject, optionalStreetObject.get());
                building.getNodes().add(node);
                optionalStreetObject.get().getChildren().add(building);
                streets.add(building);
            } else {
                log.debug("Reference object not found. Way={}", buildingFromWayObject.getId());
            }
        }

        log.debug("Built {} street and buildings.", streets.size());
        return streets;
    }

    private @NotNull StreetDTO getStreetDTO(WayDTO buildingFromWayObject, StreetDTO parentStreet) {
        StreetDTO building = new StreetDTO();
        building.setId(buildingFromWayObject.getId());
        building.setIsBuilding(buildingFromWayObject.getIsBuilding());
        building.setParent(parentStreet);
        building.setStreet(buildingFromWayObject.getStreet());
        building.setHousenumber(buildingFromWayObject.getHousenumber());
        building.setNodes(new HashSet<>(1));

        return building;
    }

    /**
     * map all Nodes from osm-file to NodeDTO
     *
     * @param nodeXmls osm-formatted xml string
     * @return list of nodeDTOs
     */
    public List<NodeDTO> mapNodeXmlToNodeDto(@NonNull List<NodeXml> nodeXmls) {
        List<NodeDTO> nodeDTOs = new ArrayList<>();
        for (NodeXml nodeXml : nodeXmls) {
            NodeDTO nodeDTO = mapNode(nodeXml);
            nodeDTOs.add(nodeDTO);
        }

        if (nodeDTOs.isEmpty()) {
            log.error("No Nodes in List.");
            return null;
        }

        log.debug("Built {} nodes.", nodeDTOs.size());
        return nodeDTOs;
    }

    /**
     * map all the Ways from osm-file to WayDTO
     *
     * @param wayXmls osm-formatted xml string
     * @return list of wayDTOs
     */
    public List<WayDTO> mapWayXmlFromOsmXmlToWayDto(@NonNull List<WayXml> wayXmls, @NonNull List<NodeDTO> nodes) {
        List<WayDTO> wayDTOs = new ArrayList<>();

        for (WayXml wayXml : wayXmls) {
            WayDTO wayDTO = mapWay(wayXml, nodes);
            wayDTOs.add(wayDTO);
        }

        if (wayDTOs.isEmpty()) {
            log.error("No Ways in List.");
            return null;
        }

        log.debug("Built {} ways.", wayDTOs.size());
        return wayDTOs;
    }

    // region private Mapper

    /**
     * node mapper xml to dto
     *
     * @param nodeXml xml formatted osm string
     * @return nodeDTO
     */
    private NodeDTO mapNode(NodeXml nodeXml) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(nodeXml.getId());
        nodeDTO.setLatitude(nodeXml.getLat());
        nodeDTO.setLongitude(nodeXml.getLon());

        return nodeDTO;
    }

    /**
     * way mapper xml to dto
     *
     * @param wayXml xml formatted osm string
     * @param nodes  list of node-objects
     * @return wayDTO
     */
    private WayDTO mapWay(WayXml wayXml, List<NodeDTO> nodes) {
        WayDTO wayDTO = new WayDTO();
        wayDTO.setId(wayXml.getId());

        List<TagXml> tagXmls = wayXml.getTags();
        wayDTO.setHighway(getValueOfTagXmlByKey(tagXmls, "highway"));
        wayDTO.setName(getValueOfTagXmlByKey(tagXmls, "name"));
        wayDTO.setCity(getValueOfTagXmlByKey(tagXmls, "city"));
        wayDTO.setCountry(getValueOfTagXmlByKey(tagXmls, "country"));
        wayDTO.setHousenumber(getValueOfTagXmlByKey(tagXmls, "housenumber"));
        wayDTO.setPostcode(getValueOfTagXmlByKey(tagXmls, "postcode"));
        wayDTO.setStreet(getValueOfTagXmlByKey(tagXmls, "street"));

        wayDTO.setJunction(getValueOfTagXmlByKey(tagXmls, "junction")); // just to have it
        wayDTO.setSurface(getValueOfTagXmlByKey(tagXmls, "surface")); // just to have it, maybe for later reasons, calculating time to travel

        wayDTO.setSport(getValueOfTagXmlByKey(tagXmls, "sport")); // need for halls and places
        wayDTO.setAmenity(getValueOfTagXmlByKey(tagXmls, "amenity")); // need for schools, churches, kindergarten
        wayDTO.setReligion(getValueOfTagXmlByKey(tagXmls, "religion")); // need for churches
        wayDTO.setDenomination(getValueOfTagXmlByKey(tagXmls, "denomination"));

        String building = getValueOfTagXmlByKey(tagXmls, "building");
        if ((building != null) && building.equals("yes") && (wayDTO.getStreet() != null) && (wayDTO.getHousenumber() != null)) {
            wayDTO.setIsBuilding(true);
            wayDTO.setIsGarage(false);
        } else if (building != null && building.equals("garages")) {
            wayDTO.setIsGarage(true);
            wayDTO.setIsBuilding(false);
        } else {
            wayDTO.setIsBuilding(false);
            wayDTO.setIsGarage(false);
        }

        // Find the duplicated node in building object for defining a computable point
        if (wayDTO.getIsBuilding() != null && wayDTO.getIsBuilding()) {
            Long value = findDuplicatedNodeDtoByNdXmls(wayXml.getNds());
            wayDTO.setRefNode(value);
        }

        Set<NodeDTO> nodeDTOSet = findNodeDtoByNdXmls(nodes, wayXml.getNds());
        wayDTO.setNodes(nodeDTOSet);

        return wayDTO;
    }

    // endregion

    // region Helping Utilities
    /**
     * get the value of a tag object by a given key
     *
     * @param tagXmls source object
     * @param key     base info
     * @return value of object
     */
    private String getValueOfTagXmlByKey(List<TagXml> tagXmls, String key) {
        Optional<TagXml> tagXmlOptional = tagXmls.stream().filter(tag -> tag.getKey().contains(key)).findFirst();
        return tagXmlOptional.map(TagXml::getValue).orElse(null);
    }

    /**
     * method to find the NdXml objects from a wayXml object in the nodeXml Objects to prepare for building a relation.
     *
     * @param ndXmls Tags from WayXml
     * @param nodes  Nodes
     * @return Set of NodeXmls
     */
    private Set<NodeDTO> findNodeDtoByNdXmls(@NonNull List<NodeDTO> nodes, List<NdXml> ndXmls) {
        return nodes.stream()
                .filter(node -> ndXmls.stream().map(NdXml::getRef).anyMatch(id -> id.equals(node.getId())))
                .collect(Collectors.toSet());
    }

    /**
     * Find the node that is contained double times in way object
     *
     * @param ndXmls reference nodes
     * @return node that describe a house
     */
    private Long findDuplicatedNodeDtoByNdXmls(List<NdXml> ndXmls) {
        Set<Long> origins = new HashSet<>();
        for (NdXml ndXml : ndXmls) {
            if (origins.contains(ndXml.getRef())) {
                return ndXml.getRef();
            } else {
                origins.add(ndXml.getRef());
            }
        }

        return null;
    }

    //endregion
}

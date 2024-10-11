package de.hskl.itanalyst.alwi.globalcache;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
import de.hskl.itanalyst.alwi.services.NodeService;
import de.hskl.itanalyst.alwi.services.StreetService;
import de.hskl.itanalyst.alwi.services.WayService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Getter
@AllArgsConstructor
@Component
public class GlobalCache {

    private List<NodeDTO> globalNodeDTOs;
    private List<WayDTO> globalWayDTOs;
    private List<StreetDTO> globalStreetDTOs;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private WayService wayService;

    @Autowired
    private StreetService streetService;

    public void prepareInitialData() {
        globalNodeDTOs = nodeService.findAllNodes();
        globalWayDTOs = wayService.findAllWays();
        globalStreetDTOs = streetService.findAllStreets();
    }

    public void refreshCache() {
        globalNodeDTOs.clear();
        globalNodeDTOs = nodeService.findAllNodes();

        globalWayDTOs.clear();
        globalWayDTOs = wayService.findAllWays();

        globalStreetDTOs.clear();
        globalStreetDTOs = streetService.findAllStreets();
    }

    public List<StreetDTO> findStreetByName(String streetName) throws StreetNotFoundException {
        List<StreetDTO> streets = globalStreetDTOs.stream().filter(s -> s.getStreet().equals(streetName)).toList();
        if (streets.isEmpty()) {
            String errMsg = String.format("Street %s not found.", streetName);
            log.error(errMsg);
            throw new StreetNotFoundException(errMsg);
        }

        return streets;
    }

    /**
     * Taking a house from an address
     * @param street street
     * @param number house-number
     * @param streets list of streets
     * @return NodeDTO
     * @throws NodeNotFoundException exception
     */
    public NodeDTO getNodeOfStreetObject(String street, String number, List<StreetDTO> streets) throws NodeNotFoundException {
        StreetDTO address = streets.stream().filter(s -> s.getHousenumber() != null && s.getHousenumber().equals(number)).findFirst().orElse(null);
        NodeDTO node;
        if (address != null) {
            node = address.getNodes().stream().toList().get(0);
        } else {
            String errMsg = String.format("No match for number: %s in street: %s.", number, street);
            log.error(errMsg);
            throw new NodeNotFoundException(errMsg);
        }

        return node;
    }
}

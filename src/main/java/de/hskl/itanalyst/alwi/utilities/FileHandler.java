package de.hskl.itanalyst.alwi.utilities;


import de.hskl.itanalyst.alwi.dto.*;
import de.hskl.itanalyst.alwi.osmmodel.OsmXml;
import de.hskl.itanalyst.alwi.repositories.*;
import de.hskl.itanalyst.alwi.services.NodeService;
import de.hskl.itanalyst.alwi.services.StreetService;
import de.hskl.itanalyst.alwi.services.WayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Reading osm-files and preparing for database integration
 *
 * @author Alexander Wiltz
 * @version 0.1.0
 */
@Slf4j
@Component
public class FileHandler {

    @Autowired
    private NodeService nodeService;
    @Autowired
    private WayService wayService;
    @Autowired
    private StreetService streetService;

    /**
     * Read osm-file and unmarshall xml objects
     *
     * @param path path to source file
     * @return string
     */
    public String readOsmFile(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }

        scanner.close();

        return stringBuilder.toString();
    }

    /**
     * method need to handle a given osm file to save in a connected database
     *
     * @param filename name of file
     */
    public void saveFileDataToDatabase(String filename) throws FileNotFoundException {
        String context = readOsmFile(filename);

        OsmXml osmXml = XmlHandler.getObjectFromXmlString(context, OsmXml.class);
        if (osmXml == null) {
            log.error("Error parsing xml file.");
            return;
        }

        ObjectHandler objectHandler = new ObjectHandler();
        List<NodeDTO> nodeDTOs = objectHandler.mapNodeXmlToNodeDto(osmXml.getNodes());
        List<WayDTO> wayDTOs = objectHandler.mapWayXmlFromOsmXmlToWayDto(osmXml.getWays(), nodeDTOs);
        List<StreetDTO> streets = objectHandler.prepareStreetsFromWaysAndNodes(wayDTOs, nodeDTOs);

        log.debug("Found {} nodes.", nodeDTOs.size());
        log.debug("Found {} ways.", wayDTOs.size());
        log.debug("Found {} streets and buildings.", streets.size());

        nodeService.saveAllNodes(nodeDTOs);
        log.debug("Nodes saved.");

        wayService.saveAllWays(wayDTOs);
        log.debug("Ways saved.");

        streetService.saveAllStreets(streets);
        log.debug("Streets saved.");
    }
}

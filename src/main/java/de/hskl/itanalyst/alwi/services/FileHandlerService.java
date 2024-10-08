package de.hskl.itanalyst.alwi.services;


import de.hskl.itanalyst.alwi.dto.*;
import de.hskl.itanalyst.alwi.entities.Node;
import de.hskl.itanalyst.alwi.entities.Street;
import de.hskl.itanalyst.alwi.entities.Way;
import de.hskl.itanalyst.alwi.osmmodel.OsmXml;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class FileHandlerService {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private WayService wayService;

    @Autowired
    private StreetService streetService;

    @Autowired
    private ObjectHandlerService objectHandlerService;

    @Autowired
    private XmlHandlerService xmlHandlerService;

    @Autowired
    private ModelMapper modelMapper;

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

        OsmXml osmXml = xmlHandlerService.getObjectFromXmlString(context, OsmXml.class);
        if (osmXml == null) {
            log.error("Error parsing xml file.");
            return;
        }

        List<NodeDTO> nodeDTOs = objectHandlerService.mapNodeXmlToNodeDto(osmXml.getNodes());
        List<WayDTO> wayDTOs = objectHandlerService.mapWayXmlFromOsmXmlToWayDto(osmXml.getWays(), nodeDTOs);
        List<StreetDTO> streetDTOs = objectHandlerService.prepareStreetsFromWaysAndNodes(wayDTOs, nodeDTOs);

        log.debug("Found {} nodes.", nodeDTOs.size());
        log.debug("Found {} ways.", wayDTOs.size());
        log.debug("Found {} streets and buildings.", streetDTOs.size());

        List<Node> nodes = nodeDTOs.stream().map(this::convertNodeEntity).toList();
        nodeService.saveAllNodes(nodes);
        log.debug("Nodes saved.");

        List<Way> ways = wayDTOs.stream().map(this::convertWayEntity).toList();
        wayService.saveAllWays(ways);
        log.debug("Ways saved.");

        List<Street> streets = streetDTOs.stream().map(this::convertStreetEntity).toList();
        streetService.saveAllStreets(streets);
        log.debug("Streets saved.");
    }

    private Node convertNodeEntity (NodeDTO nodeDTO) {
        return modelMapper.map(nodeDTO, Node.class);
    }

    private Way convertWayEntity (WayDTO wayDTO) {
        return modelMapper.map(wayDTO, Way.class);
    }

    private Street convertStreetEntity (StreetDTO streetDTO) {
        return modelMapper.map(streetDTO, Street.class);
    }
}

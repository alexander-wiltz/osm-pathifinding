package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.entities.Node;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.repositories.INodeRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NodeService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private INodeRepository nodeRepository;

    @Transactional
    @Cacheable(cacheNames = "nodes", sync=true)
    public void saveAllNodes(List<NodeDTO> nodeDTOs) {
        List<Node> nodes = nodeDTOs.stream().map(this::convertToNodeEntity).toList();
        nodeRepository.saveAll(nodes);
    }

    @Cacheable(cacheNames = "nodes", sync=true)
    public List<NodeDTO> findAllNodes() {
        List<Node> nodes = nodeRepository.findAll();
        return nodes.stream().map(this::convertToNodeDto).toList();
    }

    @Cacheable(cacheNames = "nodes", sync=true)
    public Optional<NodeDTO> findNodeById(Long id) throws NodeNotFoundException {
        Optional<Node> node = nodeRepository.findById(id);
        if (node.isEmpty()) {
            String errMsg = String.format("No match for node with id: %s.", id);
            log.error(errMsg);
            throw new NodeNotFoundException(errMsg);
        }
        return node.map(this::convertToNodeDto);
    }

    private NodeDTO convertToNodeDto(Node node) {
        return modelMapper.map(node, NodeDTO.class);
    }

    private Node convertToNodeEntity(NodeDTO nodeDTO) {
        return modelMapper.map(nodeDTO, Node.class);
    }
}

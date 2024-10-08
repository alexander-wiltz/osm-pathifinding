package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.entities.Node;
import de.hskl.itanalyst.alwi.repositories.INodeRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class NodeService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private INodeRepository nodeRepository;

    @Transactional
    public NodeDTO saveNode(NodeDTO nodeDTO) {
        Node node = convertToNodeEntity(nodeDTO);
        Node savedNode = nodeRepository.save(node);
        return convertToNodeDto(savedNode);
    }

    @Transactional
    public void saveAllNodes(List<NodeDTO> nodeDTOs) {
        List<Node> nodes = nodeDTOs.stream().map(this::convertToNodeEntity).toList();
        nodeRepository.saveAll(nodes);
    }

    public List<NodeDTO> findAllNodes() {
        List<Node> nodes = nodeRepository.findAll();
        return nodes.stream().map(this::convertToNodeDto).toList();
    }

    public Optional<NodeDTO> findNodeById(Long id) {
        Optional<Node> node = nodeRepository.findById(id);
        return node.map(this::convertToNodeDto);
    }

    private NodeDTO convertToNodeDto(Node node) {
        return modelMapper.map(node, NodeDTO.class);
    }

    private Node convertToNodeEntity(NodeDTO nodeDTO) {
        return modelMapper.map(nodeDTO, Node.class);
    }
}

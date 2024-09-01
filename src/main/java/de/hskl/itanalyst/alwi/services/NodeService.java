package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.repositories.INodeRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
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
    private INodeRepository nodeRepository;

    @Transactional
    public NodeDTO saveNode(NodeDTO node) {
        entityManager.persist(node);
        return node;
    }

    @Transactional
    public void saveAllNodes(List<NodeDTO> nodes) {
        nodeRepository.saveAll(nodes);
    }

    public List<NodeDTO> findAllNodes() {
        return nodeRepository.findAll();
    }

    public Optional<NodeDTO> findNodeById(Long id) {
        return nodeRepository.findById(id);
    }
}

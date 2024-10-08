package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.entities.Node;
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
    public Node saveNode(Node node) {
        return nodeRepository.save(node);
    }

    @Transactional
    public void saveAllNodes(List<Node> nodes) {
        nodeRepository.saveAll(nodes);
    }

    public List<Node> findAllNodes() {
        return nodeRepository.findAll();
    }

    public Optional<Node> findNodeById(Long id) {
        return nodeRepository.findById(id);
    }
}

package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.algorithm.GridCode;
import de.hskl.itanalyst.alwi.algorithm.GridMapper;
import de.hskl.itanalyst.alwi.entities.FactoryNode;
import de.hskl.itanalyst.alwi.repositories.FactoryNodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/**
 * NodeService.createNode(code, name, …) parst den Code, berechnet x/y und speichert die Node.
 */
public class FactoryNodeService {

    private final FactoryNodeRepository nodeRepository;
    private final GridMapper gridMapper = new GridMapper(0, 0);

    public FactoryNodeService(FactoryNodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Transactional
    public FactoryNode createFactoryNode(String code, String name, String floor, String zone) {
        GridCode gridCode = GridCode.parse(code);
        var point = gridMapper.toXY(gridCode);

        if (nodeRepository.existsByColLetterAndRowOddAndSubIndex(String.valueOf(gridCode.colLetter), gridCode.rowOdd, gridCode.subIndex))
            throw new IllegalArgumentException("Node existiert bereits für " + code);

        FactoryNode factoryNode = new FactoryNode();
        factoryNode.setCode(code.toUpperCase());
        factoryNode.setName(name);
        factoryNode.setColLetter(String.valueOf(gridCode.colLetter));
        factoryNode.setRowOdd(gridCode.rowOdd);
        factoryNode.setSubIndex(gridCode.subIndex);
        factoryNode.setX(point.x);
        factoryNode.setY(point.y);
        if (floor != null) factoryNode.setFloor(floor);
        if (zone != null) factoryNode.setZone(zone);

        return nodeRepository.save(factoryNode);
    }

    public FactoryNode getByCode(String code) {
        return nodeRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Unbekannter Node-Code: " + code));
    }

    public FactoryNode getById(Long id) {
        return nodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unbekannter Node: " + id));
    }

    @Transactional
    public void delete(Long id) {
        nodeRepository.deleteById(id);
    }
}

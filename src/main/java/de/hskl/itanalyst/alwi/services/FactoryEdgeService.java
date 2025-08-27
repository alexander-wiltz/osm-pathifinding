package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.entities.FactoryEdge;
import de.hskl.itanalyst.alwi.entities.FactoryNode;
import de.hskl.itanalyst.alwi.repositories.FactoryEdgeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
/**
 * EdgeService legt gerichtete/bidirektionale Kanten an (optional mit fester Länge/Speed/Kosten).
 */
public class FactoryEdgeService {

    private final FactoryEdgeRepository edgeRepository;
    private final FactoryNodeService factoryNodeService;

    @Transactional
    public FactoryEdge createEdge(Long fromId, Long toId, boolean bidirectional,
                                  Double speedMps, Double lengthM, Double costOverrideSec,
                                  String allowedModes, Boolean blocked) {

        FactoryNode from = factoryNodeService.getById(fromId);
        FactoryNode to = factoryNodeService.getById(toId);

        FactoryEdge factoryEdge = new FactoryEdge();
        factoryEdge.setFrom(from);
        factoryEdge.setTo(to);
        factoryEdge.setBidirectional(bidirectional);
        factoryEdge.setSpeedMps(speedMps);
        factoryEdge.setLengthM(lengthM);
        factoryEdge.setCostOverrideSec(costOverrideSec);
        if (allowedModes != null) factoryEdge.setAllowedModes(allowedModes);
        if (blocked != null) factoryEdge.setBlocked(blocked);

        return edgeRepository.save(factoryEdge);
    }

    @Transactional
    public void delete(Long id) {
        edgeRepository.deleteById(id);
    }
}

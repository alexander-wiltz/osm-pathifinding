package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.repositories.IWayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WayService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IWayRepository wayRepository;

    @Transactional
    public WayDTO saveWay(WayDTO way) {
        entityManager.persist(way);
        return way;
    }

    @Transactional
    public void saveAllWays(List<WayDTO> ways) {
        wayRepository.saveAll(ways);
    }

    public List<WayDTO> findAllWays() {
        return wayRepository.findAll();
    }

    public Optional<WayDTO> findWayById(Long id) {
        return wayRepository.findById(id);
    }
}

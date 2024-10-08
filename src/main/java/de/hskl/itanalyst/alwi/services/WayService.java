package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.entities.Way;
import de.hskl.itanalyst.alwi.repositories.IWayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Autowired
    private IWayRepository wayRepository;

    @Transactional
    public Way saveWay(Way way) {
        return wayRepository.save(way);
    }

    @Transactional
    public void saveAllWays(List<Way> ways) {
        wayRepository.saveAll(ways);
    }

    public List<Way> findAllWays() {
        return wayRepository.findAll();
    }

    public Optional<Way> findWayById(Long id) {
        return wayRepository.findById(id);
    }

    protected WayDTO convertToDto(Way way) {
        return modelMapper.map(way, WayDTO.class);
    }

    protected Way convertToEntity(WayDTO wayDTO) {
        return modelMapper.map(wayDTO, Way.class);
    }
}

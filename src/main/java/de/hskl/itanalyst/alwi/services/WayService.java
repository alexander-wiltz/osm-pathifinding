package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.entities.Way;
import de.hskl.itanalyst.alwi.exceptions.WayNotFoundException;
import de.hskl.itanalyst.alwi.repositories.IWayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class WayService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IWayRepository wayRepository;

    @Transactional
    public void saveAllWays(List<WayDTO> wayDTOs) {
        List<Way> ways = wayDTOs.stream().map(this::convertToWayEntity).toList();
        wayRepository.saveAll(ways);
    }

    @Cacheable(cacheNames = "ways", sync = true)
    public List<WayDTO> findAllWays() {
        List<Way> ways = wayRepository.findAll();
        return ways.stream().map(this::convertToWayDto).toList();
    }

    @Cacheable(cacheNames = "ways", sync = true, key = "#id")
    public Optional<WayDTO> findWayById(Long id) throws WayNotFoundException {
        Optional<Way> way = wayRepository.findById(id);
        if (way.isEmpty()) {
            String errMsg = String.format("No match for way with id: %s.", id);
            log.error(errMsg);
            throw new WayNotFoundException(errMsg);
        }
        return way.map(this::convertToWayDto);
    }

    private WayDTO convertToWayDto(Way way) {
        return modelMapper.map(way, WayDTO.class);
    }

    private Way convertToWayEntity(WayDTO wayDTO) {
        return modelMapper.map(wayDTO, Way.class);
    }
}

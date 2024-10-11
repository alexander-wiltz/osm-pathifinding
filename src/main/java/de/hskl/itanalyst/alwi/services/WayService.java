package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.WayDTO;
import de.hskl.itanalyst.alwi.entities.Way;
import de.hskl.itanalyst.alwi.repositories.IWayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CachePut(value="ways")
    @Transactional
    public WayDTO saveWay(WayDTO wayDTO) {
        Way way = convertToWayEntity(wayDTO);
        Way savedWay = wayRepository.save(way);
        return convertToWayDto(savedWay);
    }

    @CachePut(value="ways")
    @Transactional
    public void saveAllWays(List<WayDTO> wayDTOs) {
        List<Way> ways = wayDTOs.stream().map(this::convertToWayEntity).toList();
        wayRepository.saveAll(ways);
    }

    @Cacheable("ways")
    public List<WayDTO> findAllWays() {
        List<Way> ways = wayRepository.findAll();
        return ways.stream().map(this::convertToWayDto).toList();
    }

    @Cacheable("ways")
    public Optional<WayDTO> findWayById(Long id) {
        Optional<Way> way = wayRepository.findById(id);
        return way.map(this::convertToWayDto);
    }

    private WayDTO convertToWayDto(Way way) {
        return modelMapper.map(way, WayDTO.class);
    }

    private Way convertToWayEntity(WayDTO wayDTO) {
        return modelMapper.map(wayDTO, Way.class);
    }
}

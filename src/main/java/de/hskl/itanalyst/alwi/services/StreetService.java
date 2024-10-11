package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.entities.Street;
import de.hskl.itanalyst.alwi.repositories.IStreetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StreetService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IStreetRepository streetRepository;

    @Transactional
    public StreetDTO saveStreet(StreetDTO streetDTO) {
        Street savedStreet = convertToStreetEntity(streetDTO);
        streetRepository.save(savedStreet);
        return convertToStreetDto(savedStreet);
    }

    @Transactional
    public void saveAllStreets(List<StreetDTO> streetsDTOs) {
        List<Street> streets = streetsDTOs.stream().map(this::convertToStreetEntity).toList();
        // At first load child objects and save into database without parent relation
        Set<Street> childStreets = streets.stream().filter(Street::getIsBuilding).collect(Collectors.toSet());
        for (Street child : childStreets) {
            child.setParent(null);
            entityManager.persist(child);
            entityManager.flush();
            entityManager.clear();
        }

        // Load parent objects and build up the relation between child objects and top level elements
        Set<Street> parentStreets = streets.stream().filter(street -> !street.getIsBuilding()).collect(Collectors.toSet());
        for (Street parent : parentStreets) {
            entityManager.merge(parent);
        }
    }

    public List<StreetDTO> findAllStreets() {
        List<Street> streets = streetRepository.findAll();
        return streets.stream().map(this::convertToStreetDto).toList();
    }

    public Optional<StreetDTO> findStreetById(Long id) {
        Optional<Street> street = streetRepository.findById(id);
        return street.map(this::convertToStreetDto);
    }

    public List<StreetDTO> findByStreet(String name) {
        List<Street> streets = streetRepository.findByStreet(name);
        return streets.stream().map(this::convertToStreetDto).toList();
    }

    private StreetDTO convertToStreetDto(Street street) {
        return modelMapper.map(street, StreetDTO.class);
    }

    private Street convertToStreetEntity(StreetDTO streetDTO) {
        return modelMapper.map(streetDTO, Street.class);
    }
}

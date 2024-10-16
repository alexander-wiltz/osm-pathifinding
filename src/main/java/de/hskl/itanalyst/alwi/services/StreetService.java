package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.entities.Street;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
import de.hskl.itanalyst.alwi.repositories.IStreetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Cacheable(value = "streets", sync = true)
    public void saveAllStreets(List<StreetDTO> streetsDTOs) {
        List<Street> streets = streetsDTOs.stream().map(this::convertToStreetEntity).toList();
        streetRepository.saveAll(streets);
    }

    @Cacheable(value = "streets", sync = true)
    public List<StreetDTO> findAllStreets() {
        List<Street> streets = streetRepository.findAll();
        List<StreetDTO> streetDTOs = streets.stream().map(this::convertToStreetDto).toList();

        return updateStreetRelations(streetDTOs);
    }

    @Cacheable(value = "streets", sync = true)
    public List<StreetDTO> findListOfAllStreets() {
        List<StreetDTO> streetDTOs = findAllStreets();
        return streetDTOs.stream().filter(st -> !st.getIsBuilding()).filter(st -> !st.getChildren().isEmpty()).toList();
    }

    @Cacheable(value = "streets", sync = true)
    public List<StreetDTO> findListOfAllObjects() {
        List<StreetDTO> streetDTOs = findAllStreets();
        return streetDTOs.stream().filter(StreetDTO::getIsBuilding).toList();
    }

    @Cacheable(value = "streets", key = "#id", sync = true)
    public Optional<StreetDTO> findStreetById(Long id) throws StreetNotFoundException {
        Optional<Street> street = streetRepository.findById(id);
        if (street.isEmpty()) {
            String errMsg = String.format("No match for street with id: %s.", id);
            log.error(errMsg);
            throw new StreetNotFoundException(errMsg);
        }
        return street.map(this::convertToStreetDto);
    }

    @Cacheable(value = "streets", key = "#name", sync = true)
    public List<StreetDTO> findByStreet(String name) throws StreetNotFoundException {
        List<Street> streets = streetRepository.findByNameIgnoreCase(name);
        if (streets.isEmpty()) {
            String errMsg = String.format("No match for street: %s.", name);
            log.error(errMsg);
            throw new StreetNotFoundException(errMsg);
        }
        return streets.stream().map(this::convertToStreetDto).toList();
    }

    private StreetDTO convertToStreetDto(Street street) {
        return modelMapper.map(street, StreetDTO.class);
    }

    private Street convertToStreetEntity(StreetDTO streetDTO) {
        return modelMapper.map(streetDTO, Street.class);
    }

    private List<StreetDTO> updateStreetRelations(List<StreetDTO> streets) {
        List<StreetDTO> streetDTOs = new ArrayList<>();
        for (StreetDTO streetDTO : streets) {
            if (streetDTO.getIsBuilding()) {
                StreetDTO parent = streetDTOs.stream().filter(st -> st.getName().equals(streetDTO.getName()) && !st.getIsBuilding()).findFirst().get();
                streetDTO.setParent(parent);
                streetDTO.setChildren(null);
                streetDTO.getParent().getChildren().add(streetDTO);
            } else {
                streetDTO.setParent(null);
                if (streetDTO.getChildren() == null) {
                    streetDTO.setChildren(new ArrayList<>());
                }
            }

            streetDTOs.add(streetDTO);
        }

        return streetDTOs;
    }
}

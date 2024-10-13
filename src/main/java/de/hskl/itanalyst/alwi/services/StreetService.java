package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.entities.Street;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.repositories.IStreetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Cacheable(value = "streets", key = "#id", sync = true)
    public Optional<StreetDTO> findStreetById(Long id) {
        Optional<Street> street = streetRepository.findById(id);
        return street.map(this::convertToStreetDto);
    }

    @Cacheable(value = "streets", key = "#name", sync = true)
    public List<StreetDTO> findByStreet(String name) {
        List<Street> streets = streetRepository.findByStreet(name);
        return streets.stream().map(this::convertToStreetDto).toList();
    }

    // TODO Implementation idea when street object could be loaded without node information
    @Cacheable(value = "streets", sync = true)
    public NodeDTO getExplicitNodeOfStreetObject(StreetDTO streetDTO) throws NodeNotFoundException {
        if (streetDTO.getParent() == null) {
            Optional<Street> street = streetRepository.findById(streetDTO.getId());
            if (street.isPresent()) {
                StreetDTO streetDTOValue = convertToStreetDto(street.get());
                return streetDTOValue.getNodes().stream().findFirst().orElse(null);
            }
        }

        String errMsg = String.format("No match for street: %s.", streetDTO.getId());
        log.error(errMsg);
        throw new NodeNotFoundException(errMsg);
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
            if(streetDTO.getIsBuilding()) {
                StreetDTO parent = streetDTOs.stream().filter(st -> st.getStreet().equals(streetDTO.getStreet()) && !st.getIsBuilding()).findFirst().get();
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

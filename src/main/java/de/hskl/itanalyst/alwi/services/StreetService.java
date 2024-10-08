package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.entities.Street;
import de.hskl.itanalyst.alwi.exceptions.NodeNotFoundException;
import de.hskl.itanalyst.alwi.exceptions.StreetNotFoundException;
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

@Slf4j
@Service
public class StreetService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IStreetRepository streetRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public Street saveStreet(Street street) {
        return streetRepository.save(street);
    }

    @Transactional
    public void saveAllStreets(List<Street> streets) {
        streetRepository.saveAll(streets);
    }

    public List<Street> findAllStreets() {
        return streetRepository.findAll();
    }

    public Optional<Street> findStreetById(Long id) {
        return streetRepository.findById(id);
    }

    public List<Street> findByStreet(String name) {
        return streetRepository.findByStreet(name);
    }

    public List<StreetDTO> findStreetsAndConvert(String street) throws StreetNotFoundException {
        List<Street> streets = streetRepository.findByStreet(street);
        List<StreetDTO> streetsDTOs = streets.stream().map(this::convertToStreetDto).toList();

        if (streetsDTOs.isEmpty()) {
            String errMsg = String.format("Street %s not found.", street);
            log.error(errMsg);
            throw new StreetNotFoundException(errMsg);
        }

        return streetsDTOs;
    }

    /**
     * Taking a house from an address
     * @param street street
     * @param number housenumber
     * @param streets list of streets
     * @return NodeDTO
     * @throws NodeNotFoundException exception
     */
    public NodeDTO getNodeOfStreetObjectAndConvert(String street, String number, List<StreetDTO> streets) throws NodeNotFoundException {
        StreetDTO address = streets.stream().filter(s -> s.getHousenumber() != null && s.getHousenumber().equals(number)).findFirst().orElse(null);
        NodeDTO node;
        if (address != null) {
            node = address.getNodes().stream().toList().get(0);
        } else {
            String errMsg = String.format("No match for number: %s in street: %s.", number, street);
            log.error(errMsg);
            throw new NodeNotFoundException(errMsg);
        }

        return node;
    }

    private StreetDTO convertToStreetDto(Street street) {
        return modelMapper.map(street, StreetDTO.class);
    }
}

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
        streetRepository.saveAll(streets);
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

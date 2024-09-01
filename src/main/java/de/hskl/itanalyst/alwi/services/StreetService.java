package de.hskl.itanalyst.alwi.services;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import de.hskl.itanalyst.alwi.repositories.IStreetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StreetService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IStreetRepository streetRepository;

    @Transactional
    public StreetDTO saveStreet(StreetDTO street) {
        entityManager.persist(street);
        return street;
    }

    @Transactional
    public void saveAllStreets(List<StreetDTO> streets) {
        streetRepository.saveAll(streets);
    }

    public List<StreetDTO> findAllStreets() {
        return streetRepository.findAll();
    }

    public Optional<StreetDTO> findStreetById(Long id) {
        return streetRepository.findById(id);
    }
}

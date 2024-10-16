package de.hskl.itanalyst.alwi.repositories;

import de.hskl.itanalyst.alwi.entities.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStreetRepository extends JpaRepository<Street, Long> {
    List<Street> findByStreetIgnoreCase(String name);
}

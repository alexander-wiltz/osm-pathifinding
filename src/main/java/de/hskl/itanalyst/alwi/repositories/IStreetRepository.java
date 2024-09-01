package de.hskl.itanalyst.alwi.repositories;

import de.hskl.itanalyst.alwi.dto.StreetDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStreetRepository extends JpaRepository<StreetDTO, Long> {
}

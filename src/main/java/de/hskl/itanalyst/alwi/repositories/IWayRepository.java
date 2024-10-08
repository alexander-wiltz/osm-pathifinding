package de.hskl.itanalyst.alwi.repositories;

import de.hskl.itanalyst.alwi.entities.Way;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWayRepository extends JpaRepository<Way, Long> {
}

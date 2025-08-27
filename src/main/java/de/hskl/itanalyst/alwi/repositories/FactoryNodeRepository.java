package de.hskl.itanalyst.alwi.repositories;

import de.hskl.itanalyst.alwi.entities.FactoryNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactoryNodeRepository extends JpaRepository<FactoryNode, Long> {
    Optional<FactoryNode> findByCode(String code);
    boolean existsByColLetterAndRowOddAndSubIndex(String colLetter, int rowOdd, Integer subIndex);
}

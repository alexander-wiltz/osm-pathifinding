package de.hskl.itanalyst.alwi.repositories;

import de.hskl.itanalyst.alwi.dto.NodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INodeRepository extends JpaRepository<NodeDTO, Long> {
}

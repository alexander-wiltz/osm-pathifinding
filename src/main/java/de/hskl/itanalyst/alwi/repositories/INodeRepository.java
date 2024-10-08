package de.hskl.itanalyst.alwi.repositories;

import de.hskl.itanalyst.alwi.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INodeRepository extends JpaRepository<Node, Long> {
}

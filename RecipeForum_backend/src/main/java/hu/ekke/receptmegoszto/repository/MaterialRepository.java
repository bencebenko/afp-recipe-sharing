package hu.ekke.receptmegoszto.repository;

import hu.ekke.receptmegoszto.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}